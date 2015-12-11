package org.shaolin.bmdp.analyzer.internal;

import java.util.List;

import org.apache.log4j.Logger;
import org.shaolin.bmdp.analyzer.be.ChartStatisticImpl;
import org.shaolin.bmdp.analyzer.be.IChartStatistic;
import org.shaolin.bmdp.analyzer.dao.AanlysisModel;
import org.shaolin.bmdp.datamodel.page.UITableStatsType;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.entity.EntityNotFoundException;
import org.shaolin.bmdp.runtime.spi.ILifeCycleProvider;
import org.shaolin.bmdp.runtime.spi.IServiceProvider;
import org.shaolin.uimaster.page.cache.PageCacheManager;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.cache.UIPageObject;

public class AnalyzerServiceImpl implements ILifeCycleProvider, IServiceProvider {

	@Override
	public Class getServiceInterface() {
		return AnalyzerServiceImpl.class;
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
		    				Logger.getLogger(AnalyzerServiceImpl.class).error("Error to load the dynamic UI items: " + e.getMessage(), e);
		    			} 
		    		} 
				}
			}
		}
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
		
	}

}
