/*
 * Copyright 2015 The UIMaster Project
 *
 * The UIMaster Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.shaolin.bmdp.workflow.internal;

import java.util.List;
import java.util.Set;

import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.workflow.internal.cache.FlowObject;
import org.shaolin.bmdp.workflow.internal.type.NodeInfo;
import org.shaolin.bmdp.workflow.spi.IWorkflowService;
import org.shaolin.javacc.context.DefaultEvaluationContext;

/**
 * Save the current flow context to DB.
 * 
 * @author wushaol
 * 
 */
public final class FlowState implements java.io.Serializable {

	private static final long serialVersionUID = -6527068273849323236L;

	private String appName;
	private String flowName;
	private String nodeName;
	public transient NodeInfo startNode;
	public transient NodeInfo currentNode;
	public transient NodeInfo eventNode;

	public List<String> globalVarNames;
	public Set<String> globalVarNamesSet;

	public long taskId;
	public String sessionId;
	public String engineId;
	public String eventId;
	public String eventConsumer;
	public boolean waitResponse;
	public boolean responseBack;
	public boolean recoverable;
	private String sappName;
	private String sflowName;
	private String snodeName;
	private String eappName;
	private String eflowName;
	private String enodeName;
	
	public FlowState() {}
	
	public FlowState(NodeInfo currnode, List<String> globalVarNames,
			Set<String> globalVarNamesSet,
			DefaultEvaluationContext globalVariables) {
		this.currentNode = currnode;
		this.nodeName = currnode.getName();
		this.appName = currnode.getFlow().getApp().getName();
		this.flowName = currnode.getFlow().getName();
		this.globalVarNames = globalVarNames;
		this.globalVarNamesSet = globalVarNamesSet;
//		this.globalVariables = globalVariables;
	}

	public void ready() {
		if (startNode != null) {
			this.snodeName = startNode.getName();
			this.sappName = startNode.getFlow().getApp().getName();
			this.sflowName = startNode.getFlow().getName();
		}
		if (eventNode != null) {
			this.enodeName = eventNode.getName();
			this.eappName = eventNode.getFlow().getApp().getName();
			this.eflowName = eventNode.getFlow().getName();
		}
	}

	public void recover(final JSONObject json) throws JSONException {
		this.engineId = json.getString("engineId");
		this.eventConsumer = json.getString("eventConsumer");
		this.eventId  = json.getString("eventId");
		this.sessionId = json.getString("sessionId");
		this.taskId = json.getLong("taskId");
		this.nodeName = json.getString("nodeName");
		this.appName = json.getString("appName");
		this.flowName = json.getString("flowName");
		this.responseBack = json.getBoolean("responseBack");
		this.recoverable = json.getBoolean("recoverable");
		this.waitResponse = json.getBoolean("waitResponse");
		if (json.has("snodeName") && json.getString("snodeName") != null && !json.getString("snodeName").equals("null")) {
			this.snodeName = json.getString("snodeName");
			this.sappName = json.getString("sappName");
			this.sflowName = json.getString("sflowName");
		}
		if (json.has("enodeName") && json.getString("enodeName") != null && !json.getString("enodeName").equals("null")) {
			this.enodeName = json.getString("enodeName");
			this.eappName = json.getString("eappName");
			this.eflowName = json.getString("eflowName");
		}
		IWorkflowService service = AppContext.get().getService(
				IWorkflowService.class);
		try {
			this.currentNode = service.getFlowObject(appName).getNode(appName,
					flowName, nodeName);
			if (this.snodeName != null && this.sappName != null) {
				this.startNode = service.getFlowObject(sappName).getNode(sappName,
						sflowName, snodeName);
			}
			if (this.enodeName != null && this.eappName != null) {
				FlowObject flowObject = service.getFlowObject(eappName);
				if (flowObject != null) {
					this.eventNode = flowObject.getNode(eappName,
							eflowName, enodeName);
				}
			}
		} catch (NullPointerException e) {
			throw new IllegalStateException("an NPE issue found: appName=" + appName + ", flowName=" + flowName + ", nodeName=" + nodeName + ", json: "+ json, e);
		}
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getEngineId() {
		return engineId;
	}

	public void setEngineId(String engineId) {
		this.engineId = engineId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventConsumer() {
		return eventConsumer;
	}

	public void setEventConsumer(String eventConsumer) {
		this.eventConsumer = eventConsumer;
	}

	public boolean isWaitResponse() {
		return waitResponse;
	}

	public void setWaitResponse(boolean waitResponse) {
		this.waitResponse = waitResponse;
	}

	public boolean isResponseBack() {
		return responseBack;
	}

	public void setResponseBack(boolean responseBack) {
		this.responseBack = responseBack;
	}

	public boolean isRecoverable() {
		return recoverable;
	}

	public void setRecoverable(boolean recoverable) {
		this.recoverable = recoverable;
	}

	public String getSappName() {
		return sappName;
	}

	public void setSappName(String sappName) {
		this.sappName = sappName;
	}

	public String getSflowName() {
		return sflowName;
	}

	public void setSflowName(String sflowName) {
		this.sflowName = sflowName;
	}

	public String getSnodeName() {
		return snodeName;
	}

	public void setSnodeName(String snodeName) {
		this.snodeName = snodeName;
	}

	public String getEappName() {
		return eappName;
	}

	public void setEappName(String eappName) {
		this.eappName = eappName;
	}

	public String getEflowName() {
		return eflowName;
	}

	public void setEflowName(String eflowName) {
		this.eflowName = eflowName;
	}

	public String getEnodeName() {
		return enodeName;
	}

	public void setEnodeName(String enodeName) {
		this.enodeName = enodeName;
	}

	public String getAppName() {
		return appName;
	}

	public String getFlowName() {
		return flowName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public List<String> getGlobalVarNames() {
		return globalVarNames;
	}

	public Set<String> getGlobalVarNamesSet() {
		return globalVarNamesSet;
	}
	
}