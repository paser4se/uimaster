package org.shaolin.bmdp.workflow.internal;

import java.util.HashMap;

import org.shaolin.bmdp.runtime.perf.ConcludeStats;
import org.shaolin.bmdp.runtime.perf.KPICollector;
import org.shaolin.bmdp.runtime.perf.StatisticUnit;
import org.shaolin.bmdp.runtime.perf.TimeRangeStats;
import org.shaolin.bmdp.workflow.internal.type.NodeInfo;
import org.shaolin.uimaster.page.monitor.RestUIPerfMonitor;

public class PerfMonitor {
	
	public static final String MissionNode = "MissionNode";
	public static final String MissionNode_TIME = "MissionNode_TIME";
	
	private static final HashMap<String, KPICollector> collectors = new HashMap<String, KPICollector>();
	
	public static void updateMissionNodeKPI(final NodeInfo currentNode, final long value) {
		if (currentNode == null) {
			return;
		}
		KPICollector collector;
		if (collectors.containsKey(currentNode.toString())) {
			collector = collectors.get(currentNode.toString());
		} else {
			collector = new KPICollector("WorkflowKPI--" + currentNode.toString());
			collector.addKPI(MissionNode_TIME, new TimeRangeStats(MissionNode_TIME, StatisticUnit.Milliseconds));
			collector.addKPI(MissionNode, new ConcludeStats(MissionNode));
			collectors.put(currentNode.toString(), collector);
			RestUIPerfMonitor.registerCollector(collector);
		}
		collector.updateKPI(MissionNode_TIME, value);
		collector.updateKPI(MissionNode, value);
		
	}
	
}
