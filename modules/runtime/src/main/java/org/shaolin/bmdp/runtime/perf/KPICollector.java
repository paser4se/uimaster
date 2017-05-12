package org.shaolin.bmdp.runtime.perf;

import java.util.HashMap;
import java.util.Map;

public class KPICollector {

	private final String kpiSetName;
	
	private final Map<String, SingleKPI> kpis = new HashMap<String, SingleKPI>();
	
	public KPICollector(String kpiSetName) {
		this.kpiSetName = kpiSetName;
	}
	
	public void addKPI(String kipName, SingleKPI kipInfo) {
		this.kpis.put(kipName, kipInfo);
	}
	
	public void updateKPI(String kipName, long value) {
		if (this.kpis.containsKey(kipName)) {
			SingleKPI kip = this.kpis.get(kipName);
			if (kip instanceof ConcludeStats) {
				((ConcludeStats)kip).addNew(value);
			} else if (kip instanceof TimeRangeStats) {
				((TimeRangeStats)kip).arrange(value);
			} else if (kip instanceof ThroughputStats) {
				((ThroughputStats)kip).increment();
				((ThroughputStats)kip).updateAndGetThroughput();
			} 
		}
	}
	
	/**
	 * Get all information.
	 * 
	 * @return
	 */
	public Map<String, SingleKPI> getAllKIPs() {
		return this.kpis;
	}
	
	public String getKpiSetName() {
		return this.kpiSetName;
	}
	
	@Override
	public String toString() {
		return this.kpiSetName;
	}
}
