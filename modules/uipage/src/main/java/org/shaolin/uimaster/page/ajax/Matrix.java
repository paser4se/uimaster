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
package org.shaolin.uimaster.page.ajax;

import java.io.Serializable;

import org.shaolin.bmdp.runtime.ce.IConstantEntity;

/**
 * @author Shaolin Wu
 */
public class Matrix extends Widget<Matrix> implements Serializable {
	
	private static final long serialVersionUID = -1744731434666233557L;
	
	private String selectedNode;

	private String selectedNodeId;
	
	private int selectedX = 0;
	
	private int selectedY = 0;
	
	public Matrix(String id, Layout layout) {
		super(id, layout);
		this._setWidgetLabel(id);
	}

	public Matrix addAttribute(String name, Object value, boolean update) {
		if ("selectedNode".equals(name)) {
			this.selectedNode = value.toString();
		} else if ("selectedNodeId".equals(name)) {
			this.selectedNodeId = value.toString();
		} else if ("selectedX".equals(name)) {
			this.selectedX = Integer.parseInt(value.toString());
		} else if ("selectedY".equals(name)) {
			this.selectedY = Integer.parseInt(value.toString());
		} 
		return this;
	}
	
	public void setCEType(String ceType) {
		//TODO:
	}
	
	public IConstantEntity getSelectedCEItem() {
		return null;
	}
	
	public int getSelectedX() {
		return selectedX;
	}

	public int getSelectedY() {
		return selectedY;
	}

	public String getSelectedNode() {
		return selectedNode;
	}

	public String getSelectedNodeId() {
		return selectedNodeId;
	}
	
}
