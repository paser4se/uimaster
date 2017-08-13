package org.shaolin.bmdp.persistence;

import org.shaolin.bmdp.runtime.perf.ConcludeStats;
import org.shaolin.bmdp.runtime.perf.KPICollector;
import org.shaolin.bmdp.runtime.perf.SingleKPI;
import org.shaolin.bmdp.runtime.perf.StatisticUnit;
import org.shaolin.bmdp.runtime.perf.TimeRangeStats;

public class PerfMonitor {
	
	public static final String SQLQUERY = "SQLQUERY";
	public static final String SQLQUERY_TIME = "SQLQUERY_TIME";
	
	private static final KPICollector collector = new KPICollector("PersistenceKPIs");

	static {
		collector.addKPI(SQLQUERY_TIME, new TimeRangeStats(SQLQUERY_TIME, StatisticUnit.Milliseconds));
		collector.addKPI(SQLQUERY, new ConcludeStats(SQLQUERY));
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
	
}
