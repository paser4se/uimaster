package org.shaolin.uimaster.page.monitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shaolin.bmdp.persistence.PerfMonitor;
import org.shaolin.bmdp.runtime.perf.ConcludeStats;
import org.shaolin.bmdp.runtime.perf.KPICollector;
import org.shaolin.bmdp.runtime.perf.SingleKPI;
import org.shaolin.bmdp.runtime.perf.StatisticUnit;
import org.shaolin.bmdp.runtime.perf.ThroughputStats;
import org.shaolin.bmdp.runtime.perf.TimeRangeStats;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class RestUIPerfMonitor {
	
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
	
	public static final String AJAX_DATA_TO_UI_TPS = "AJAX-TPS";
	public static final String AJAX_DATA_TO_UI_COUNT = "AJAX-Count";
	public static final String AJAX_DATA_TO_UI_ERROR_COUNT = "AJAX-Error-Count";
	public static final String AJAX_DATA_TO_UI_PROCESS_TIME = "AJAX-ProcessTime";
	
	private static final KPICollector collector = new KPICollector("UIKPIs");

	private static final List<KPICollector> collectors = new ArrayList<KPICollector>();
	
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
		
		collector.addKPI(AJAX_DATA_TO_UI_TPS, new ThroughputStats(AJAX_DATA_TO_UI_TPS));
		collector.addKPI(AJAX_DATA_TO_UI_COUNT, new ConcludeStats(AJAX_DATA_TO_UI_COUNT));
		collector.addKPI(AJAX_DATA_TO_UI_ERROR_COUNT, new ConcludeStats(AJAX_DATA_TO_UI_ERROR_COUNT));
		collector.addKPI(AJAX_DATA_TO_UI_PROCESS_TIME, new TimeRangeStats(AJAX_DATA_TO_UI_PROCESS_TIME, StatisticUnit.Milliseconds));
	}
	
	public static void updateKPI(String kipName, long value) {
		collector.updateKPI(kipName, value);
	}
	
	public static void addKPI(SingleKPI kpi) {
		collector.addKPI(kpi.getKpiName(), kpi);
	}
	
	public static KPICollector getKPICollector() {
		return collector;
	}
	
	public static void registerCollector(KPICollector coll) {
		collectors.add(coll);
	}
	
	private static final String charset = "UTF-8";
	
	@RequestMapping("/perf")
	protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
		if (request.getProtocol().compareTo("HTTP/1.0") == 0) {
			response.setHeader("Pragma", "no-cache");
		} else if (request.getProtocol().compareTo("HTTP/1.1") == 0) {
			response.setHeader("Cache-Control", "no-cache");
		}
		response.setDateHeader("Expires", 0);
		response.setContentType("json");
		response.setCharacterEncoding(charset);
		request.setCharacterEncoding(charset);
		
		StringBuffer sb = new StringBuffer();
		sb.append("<html><body>");
		sb.append("<UL> UI Statistic KPIs");
		Map<String, SingleKPI> items = RestUIPerfMonitor.getKPICollector().getAllKIPs();
        for (Map.Entry<String, SingleKPI> item : items.entrySet()) {
        	sb.append("<LI>").append(item.getValue()).append("</LI>");
        }
        sb.append("</UL>");
        sb.append("<UL> Persistence Statistic KPIs");
        items = PerfMonitor.getKPICollector().getAllKIPs();
        for (Map.Entry<String, SingleKPI> item : items.entrySet()) {
        	sb.append("<LI>").append(item.getValue()).append("</LI>");
        }
        sb.append("</UL>");
        
        if (!collectors.isEmpty()) {
        	for (KPICollector collector: collectors) {
        		sb.append("<UL> "+collector.getKpiSetName()+" Statistic KPIs");
                items = collector.getAllKIPs();
                for (Map.Entry<String, SingleKPI> item : items.entrySet()) {
                	sb.append("<LI>").append(item.getValue()).append("</LI>");
                }
                sb.append("</UL>");
        	}
        }
        
        sb.append("</body></html>");
		PrintWriter out = response.getWriter();
		out.print(sb.toString());
    }
	
}
