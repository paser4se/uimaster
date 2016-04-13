package org.shaolin.bmdp.analyzer.internal;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.shaolin.bmdp.analyzer.IAnalyzerService;
import org.shaolin.bmdp.analyzer.be.ChartStatisticImpl;
import org.shaolin.bmdp.analyzer.be.IChartStatistic;
import org.shaolin.bmdp.analyzer.be.IJavaCCJob;
import org.shaolin.bmdp.analyzer.be.JavaCCJobImpl;
import org.shaolin.bmdp.analyzer.ce.JavaCCJobStatusType;
import org.shaolin.bmdp.analyzer.dao.AanlysisModel;
import org.shaolin.bmdp.datamodel.common.DiagramType;
import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.datamodel.page.UITableStatsType;
import org.shaolin.bmdp.datamodel.rdbdiagram.ColumnType;
import org.shaolin.bmdp.datamodel.rdbdiagram.TableType;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.entity.EntityAddedEvent;
import org.shaolin.bmdp.runtime.entity.EntityManager;
import org.shaolin.bmdp.runtime.entity.EntityNotFoundException;
import org.shaolin.bmdp.runtime.entity.EntityUpdatedEvent;
import org.shaolin.bmdp.runtime.entity.IEntityEventListener;
import org.shaolin.bmdp.runtime.spi.ILifeCycleProvider;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.bmdp.runtime.spi.IServiceProvider;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.shaolin.uimaster.page.cache.PageCacheManager;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.cache.UIPageObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AnalyzerServiceImpl implements ILifeCycleProvider, IServiceProvider, IAnalyzerService {

	// make this for whole system, not only for one application instance.
	private ScheduledExecutorService pool;
		
	private final Map<Long, ScheduledFuture<?>> futures = new HashMap<Long, ScheduledFuture<?>>();
	
	final List<TableType> tables = new ArrayList<TableType>();
	
	private static final Logger logger = LoggerFactory.getLogger(AnalyzerServiceImpl.class);
	
	@Override
	public Class getServiceInterface() {
		return IAnalyzerService.class;
	}

	@Override
	public int getRunLevel() {
		return 10;
	}

	@Override
	public boolean readyToStop() {
		return true;
	}

	@Override
	public void reload() {
		if (AppContext.isMasterNode()) {
			// only master node allows to run job.
			// make this shared
			this.pool = IServerServiceManager.INSTANCE.getSchedulerService()
					.createScheduler("system", "data-analyzer", 1);
			
			ChartStatisticImpl stats = new ChartStatisticImpl();
			List<IChartStatistic> result = AanlysisModel.INSTANCE.searchChartStats(stats, null, 0, -1);
			if (!result.isEmpty()) {
				for (IChartStatistic statsItem: result) {
					UITableStatsType uiStatsType = convert(statsItem);
					try {
						UIFormObject uiCache = PageCacheManager.getUIFormObject(statsItem.getActionOnUIFrom());
		    			uiCache.addStatsAction(uiStatsType);
		    		} catch (EntityNotFoundException e) {
		    			try {
		    				UIPageObject uiCache = PageCacheManager.getUIPageObject(statsItem.getActionOnUIFrom());
		    				UIFormObject uiForm = uiCache.getUIForm();
		    				uiForm.addStatsAction(uiStatsType);
		    			} catch (Exception e1) {
		    				logger.error("Error to load the dynamic UI items: " + e.getMessage(), e);
		    			} 
		    		} 
				}
			}
			
			//TODO: check the distribution status of java cc jobs.
			JavaCCJobImpl jobCondition = new JavaCCJobImpl();
			jobCondition.setEnabled(true);
			jobCondition.setStatus(JavaCCJobStatusType.START);
			List<IJavaCCJob> jobs = AanlysisModel.INSTANCE.searchJavaCCJob(jobCondition, null, 0, -1);
			for (IJavaCCJob job : jobs) {
				this.startJob(job);
			}
		}
		
		this.tables.clear();
		IServerServiceManager.INSTANCE.getEntityManager().executeListener(new IEntityEventListener<TableType, DiagramType>() {
			@Override
			public Class getEventType() {
				return TableType.class;
			}
			@Override
			public void notify(EntityAddedEvent<TableType, DiagramType> arg0) {
				tables.add(arg0.getEntity());
			}
			@Override
			public void notify(EntityUpdatedEvent<TableType, DiagramType> arg0) {
			}
			@Override
			public void notifyAllLoadFinish() {
			}
			@Override
			public void notifyLoadFinish(DiagramType arg0) {
			}
			@Override
			public void setEntityManager(EntityManager arg0) {
			}
		});
	}
	
	public UITableStatsType convert(IChartStatistic statsItem) {
		UITableStatsType type = new UITableStatsType();
		type.setTableName(statsItem.getStatsTableName());
		type.setUiFrom(statsItem.getStatsUIFrom());
		type.setUiid(statsItem.getActionOnWidgetId());
		type.getCharts().add(statsItem.getChartType());
		return type;
	}

	@Override
	public void startService() {
		this.reload();
	}

	@Override
	public void stopService() {
		if (this.pool != null) {
			Set<Long> tasks = futures.keySet();
			for (Long task: tasks) {
				ScheduledFuture<?> future = futures.get(task);
				if (future != null && !future.isDone()) {
					future.cancel(true);
				}
			}
			futures.clear();
		} 
	}
	
	@Override
	public void startJob(final IJavaCCJob job) {
		if (this.pool == null || (job.getExecuteDays() == 0 && job.getExecuteTime() == 0)) {
			return;
		}
        
		long daysMillis = 0;
		if (job.getExecuteDays() > 0) {
    		daysMillis = job.getExecuteDays() * 24 * 60 * 60 * 1000;
    	}
    	long hoursMillis = 0;
    	if (job.getExecuteTime() > 0) {
    		hoursMillis = job.getExecuteTime() * 60 * 60 * 1000;
    	} 
    	long nextExecutedTime = System.currentTimeMillis() + daysMillis + hoursMillis;
    	job.setStatus(JavaCCJobStatusType.START);
    	job.setRealExecutedTime(new Date(nextExecutedTime));
    	AanlysisModel.INSTANCE.update(job, true);
    	
    	nextExecutedTime = nextExecutedTime/1000000;
		final String script = job.getScript();
		final long nextDiff = daysMillis + hoursMillis;
		ScheduledFuture<?> f = pool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
		            DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
		            ooeeContext.setDefaultEvaluationContext(evaContext);
		            ExpressionType expr = new ExpressionType();
		            expr.setExpressionString(script);
		            expr.parse(ooeeContext);
		            expr.evaluate(ooeeContext);
		            
		            job.setCount(job.getCount() + 1);
		            job.setRealExecutedTime(new Date(System.currentTimeMillis() + nextDiff));
		            AanlysisModel.INSTANCE.update(job, true);
				} catch (Exception e) {
					logger.warn("Error occurred while executing JavaCC job!", e);
				}
			}
		}, nextExecutedTime, nextExecutedTime, TimeUnit.SECONDS);
		futures.put(job.getId(), f);
	}

	@Override
	public void stopJob(IJavaCCJob job) {
		if (this.pool == null) {
			return;
		}
		
		if (futures.containsKey(job.getId())) {
			futures.remove(job.getId()).cancel(true);
			
			job.setStatus(JavaCCJobStatusType.STOP);
            AanlysisModel.INSTANCE.update(job, true);
		}
	}
	
	@Override
	public List<String> getAllTableList() {
		final List<String> tableNames = new ArrayList<String>();
		for (TableType t : tables) {
			tableNames.add(t.getEntityName());
		}
		Collections.sort(tableNames, Collator.getInstance(java.util.Locale.CHINA));
		return tableNames;
	}
	
	@Override
	public List<String> getTableColumns(String name) {
		final List<String> columnNames = new ArrayList<String>();
		for (TableType t : tables) {
			if(t.getEntityName().equals(name)) {
				List<ColumnType> columns  = t.getColumns();
				for (ColumnType c : columns) {
					columnNames.add(c.getName() + " : " + c.getType());
				}
				break;
			}
		}
		return columnNames;
	}
	
	public List<String> getTableColumnIds(String name) {
		final List<String> columnNames = new ArrayList<String>();
		for (TableType t : tables) {
			if(t.getEntityName().equals(name)) {
				List<ColumnType> columns  = t.getColumns();
				for (ColumnType c : columns) {
					columnNames.add(c.getName());
				}
				break;
			}
		}
		return columnNames;
	}
}
