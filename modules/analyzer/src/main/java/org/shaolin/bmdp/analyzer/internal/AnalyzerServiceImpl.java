package org.shaolin.bmdp.analyzer.internal;

import java.net.InetAddress;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.shaolin.bmdp.analyzer.IAnalyzerService;
import org.shaolin.bmdp.analyzer.be.IChartStatistic;
import org.shaolin.bmdp.analyzer.be.IJavaCCJob;
import org.shaolin.bmdp.analyzer.ce.JavaCCJobStatusType;
import org.shaolin.bmdp.analyzer.dao.AanlysisModel;
import org.shaolin.bmdp.analyzer.distributed.ZKDistributedJobEngine;
import org.shaolin.bmdp.datamodel.common.DiagramType;
import org.shaolin.bmdp.datamodel.page.UITableStatsType;
import org.shaolin.bmdp.datamodel.rdbdiagram.ColumnType;
import org.shaolin.bmdp.datamodel.rdbdiagram.TableType;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.ddc.client.ZooKeeperFactory;
import org.shaolin.bmdp.runtime.entity.EntityAddedEvent;
import org.shaolin.bmdp.runtime.entity.EntityManager;
import org.shaolin.bmdp.runtime.entity.EntityUpdatedEvent;
import org.shaolin.bmdp.runtime.entity.IEntityEventListener;
import org.shaolin.bmdp.runtime.spi.ILifeCycleProvider;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.bmdp.runtime.spi.IServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AnalyzerServiceImpl implements ILifeCycleProvider, IServiceProvider, IAnalyzerService {
	
	final List<TableType> tables = new ArrayList<TableType>();
	
	private static final Logger logger = LoggerFactory.getLogger(AnalyzerServiceImpl.class);

	private ZKDistributedJobEngine javaCCJobEngine;

	private String nodeName ;

	public AnalyzerServiceImpl() {
		try {
			nodeName = InetAddress.getLocalHost().getHostName()+System.currentTimeMillis();
		}catch (Exception e) {
			nodeName = UUID.randomUUID().toString();
		}

		
	}
	
	private synchronized ZKDistributedJobEngine getJavaCCJobEngine() {
	    if (javaCCJobEngine == null) {	        
	        javaCCJobEngine = new ZKDistributedJobEngine(ZooKeeperFactory.getInstance().getCachedZookeeper(),nodeName);
	    } 
	    return javaCCJobEngine;
	}

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
	    logger.debug("-------------------------------starting Analyzer--------------------------------------");
		if (AppContext.isMasterNode()) {
		    getJavaCCJobEngine().startService();
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
	    getJavaCCJobEngine().stopService();
	}
	
	@Override
	public void startJob(final IJavaCCJob job) {
		if (job.getCronExp() == null) {
			return;
		}
        
    	job.setStatus(JavaCCJobStatusType.START);
    	//job.setRealExecutedTime(new Date(nextExecutedTime));
    	getJavaCCJobEngine().startJob(job);
    	AanlysisModel.INSTANCE.update(job, true);
    	//TODO:
	}

	@Override
	public void stopJob(IJavaCCJob job) {
		if (job.getStatus() != JavaCCJobStatusType.STOP) {
			job.setStatus(JavaCCJobStatusType.STOP);
			getJavaCCJobEngine().stopJob(job);
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
