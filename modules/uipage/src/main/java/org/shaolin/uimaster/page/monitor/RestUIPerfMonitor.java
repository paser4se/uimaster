package org.shaolin.uimaster.page.monitor;

import org.shaolin.bmdp.runtime.perf.ConcludeStats;
import org.shaolin.bmdp.runtime.perf.KPICollector;
import org.shaolin.bmdp.runtime.perf.StatisticUnit;
import org.shaolin.bmdp.runtime.perf.ThroughputStats;
import org.shaolin.bmdp.runtime.perf.TimeRangeStats;

//import javax.ws.rs.DefaultValue;  
//import javax.ws.rs.GET;  
//import javax.ws.rs.Path;  
//import javax.ws.rs.PathParam;  
//import javax.ws.rs.Produces;  
//import javax.ws.rs.QueryParam;  
//import javax.ws.rs.core.MediaType;  
//import javax.ws.rs.core.Response;  
//
//@Path("/uiperf")  
public class RestUIPerfMonitor {
	
	public static final String PAGE_DATA_TO_UI_TPS = "PageDataToUI-TPS";
	public static final String PAGE_DATA_TO_UI_COUNT = "PageDataToUI-Count";
	public static final String PAGE_DATA_TO_UI_ERROR_COUNT = "PageDataToUI-Error-Count";
	public static final String PAGE_DATA_TO_UI_PROCESS_TIME = "PageDataToUI-ProcessTime";
	
	public static final String PAGE_UI_TO_DATA_COUNT = "PageUIToData-Count";
	public static final String PAGE_UI_TO_DATA_ERROR_COUNT = "PageUIToData-Error-Count";
	public static final String PAGE_UI_TO_DATA_PROCESS_TIME = "PageUIToData-ProcessTime";
	
	public static final String PAGE_RENDERING = "PageRendering-ProcessTime";
	public static final String PAGE_RENDERING_COUNT = "PageRendering-Count";
	public static final String PAGE_RENDERING_ERROR_COUNT = "PageRendering-Error-Count";
	
	private static final KPICollector collector = new KPICollector("UIKPIs");

	static {
		collector.addKPI(PAGE_DATA_TO_UI_PROCESS_TIME, new TimeRangeStats(PAGE_DATA_TO_UI_PROCESS_TIME, StatisticUnit.Milliseconds));
		collector.addKPI(PAGE_UI_TO_DATA_PROCESS_TIME, new TimeRangeStats(PAGE_UI_TO_DATA_PROCESS_TIME, StatisticUnit.Milliseconds));
		collector.addKPI(PAGE_DATA_TO_UI_COUNT, new ConcludeStats(PAGE_DATA_TO_UI_COUNT));
		collector.addKPI(PAGE_UI_TO_DATA_COUNT, new ConcludeStats(PAGE_UI_TO_DATA_COUNT));
		collector.addKPI(PAGE_DATA_TO_UI_TPS, new ThroughputStats(PAGE_DATA_TO_UI_TPS));
		collector.addKPI(PAGE_DATA_TO_UI_ERROR_COUNT, new ConcludeStats(PAGE_DATA_TO_UI_ERROR_COUNT));
		collector.addKPI(PAGE_UI_TO_DATA_ERROR_COUNT, new ConcludeStats(PAGE_UI_TO_DATA_ERROR_COUNT));
		
		collector.addKPI(PAGE_RENDERING, new TimeRangeStats(PAGE_UI_TO_DATA_ERROR_COUNT, StatisticUnit.Milliseconds));
		collector.addKPI(PAGE_RENDERING_COUNT, new ConcludeStats(PAGE_RENDERING_COUNT));
		collector.addKPI(PAGE_RENDERING_ERROR_COUNT, new ConcludeStats(PAGE_RENDERING_ERROR_COUNT));
		
	}
	
	
	public static void updateKPI(String kipName, long value) {
		collector.updateKPI(kipName, value);
	}
	
	public static KPICollector getKPICollector() {
		return collector;
	}
	
}
