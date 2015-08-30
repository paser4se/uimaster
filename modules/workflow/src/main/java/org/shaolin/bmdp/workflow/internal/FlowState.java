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

import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.spi.Event;
import org.shaolin.bmdp.workflow.internal.type.NodeInfo;
import org.shaolin.bmdp.workflow.spi.IWorkflowService;
import org.shaolin.bmdp.workflow.spi.WorkflowSession;
import org.shaolin.javacc.context.DefaultEvaluationContext;

/**
 * Save the current flow context to DB.
 * 
 * @author wushaol
 * 
 */
public final class FlowState implements java.io.Serializable {

	private static final long serialVersionUID = -6527068273849323236L;

	private final String appName;
	private final String flowName;
	private final String nodeName;
	transient NodeInfo currentNode;
	DefaultEvaluationContext globalVariables;
	DefaultEvaluationContext localVariables;
	final List<String> globalVarNames;
	final Set<String> globalVarNamesSet;

	long taskId;
	Event event;
	WorkflowSession session;
	String sessionId;
	String engineId;
	boolean waitResponse;
	boolean responseBack;
	boolean recoverable;
	private String sappName;
	private String sflowName;
	private String snodeName;
	transient NodeInfo startNode;
	private String eappName;
	private String eflowName;
	private String enodeName;
	transient NodeInfo eventNode;

	public FlowState(NodeInfo currnode, List<String> globalVarNames,
			Set<String> globalVarNamesSet,
			DefaultEvaluationContext globalVariables) {
		this.currentNode = currnode;
		this.nodeName = currnode.getName();
		this.appName = currnode.getFlow().getApp().getName();
		this.flowName = currnode.getFlow().getName();
		this.globalVarNames = globalVarNames;
		this.globalVarNamesSet = globalVarNamesSet;
		this.globalVariables = globalVariables;
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

	public void recover() {
		IWorkflowService service = AppContext.get().getService(
				IWorkflowService.class);
		this.currentNode = service.getFlowObject(appName).getNode(appName,
				flowName, nodeName);
		if (this.snodeName != null) {
			this.startNode = service.getFlowObject(sappName).getNode(sappName,
					sflowName, snodeName);
		}
		if (this.enodeName != null) {
			this.eventNode = service.getFlowObject(eappName).getNode(eappName,
					eflowName, enodeName);
		}
	}
}