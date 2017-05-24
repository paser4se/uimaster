package org.shaolin.uimaster.page.monitor;

import java.util.Queue;

import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.perf.SingleKPI;

public class QueueKPI implements SingleKPI {

	private final String kpiName;
	
	private final Queue<?> queue;
	
	public QueueKPI(String kpiName, Queue<?> queue) {
		this.kpiName = kpiName;
		this.queue = queue;
	}
	
	@Override
	public String getKpiName() {
		return kpiName;
	}

	@Override
	public void reset() {
		
	}
	
	public String toString() {
    	JSONObject json = new JSONObject();
        try {
        	json.put("KPIName", this.getKpiName());
        	json.put("queueSize", this.queue.size());
        } catch (Exception e) {}
        return json.toString();
    }
}
