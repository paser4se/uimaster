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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.json.JSONArray;
import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.AjaxContextHelper;
import org.shaolin.uimaster.page.IJSHandlerCollections;
import org.shaolin.uimaster.page.ajax.json.IDataItem;
import org.shaolin.uimaster.page.cache.PageCacheManager;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.od.ODContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shaolin Wu
 */
public class Tree extends Widget<Tree> implements Serializable {
	private static final long serialVersionUID = -1744731434666233557L;
	
	private static final Logger logger = LoggerFactory.getLogger(Tree.class);

	private final TreeConditions conditions = new TreeConditions();

	private transient List<String> dataModel = new ArrayList<String>();
	
	private String selectedParentNode;
	
	private String selectedNodeName;
	
	private transient ExpressionType expandExpr;
	
	public Tree(String tableId, HttpServletRequest request) {
		super(tableId, null);
		this.expandExpr = null;
	}

	public Tree(String id, Layout layout, ExpressionType expandExpr) {
		super(id, layout);
		this._setWidgetLabel(id);
		this.expandExpr = expandExpr;
	}

	public void setDataModel(List<String> newModel) {
		this.dataModel.clear();
		this.dataModel.addAll(newModel);
	}
	
	public Tree addAttribute(String name, Object value, boolean update) {
		if ("selectedNode".equals(name)) {
			conditions.setSelectedId(value.toString());
		} else if ("selectedParentNode".equals(name)) {
			selectedParentNode = value.toString();
		} else if ("selectedNodeName".equals(name)) {
			selectedNodeName = value.toString();
		} else {
			return super.addAttribute(name, value, update);
		}
		return this;
	}
	
	/**
	 * Unsupported!
	 * 
	 * @return
	 */
	public Object getSelectedObject() {
		return null;
	}
	
	public String getSelectedNodeName() {
		return selectedNodeName;
	}
	
	public String getSelectedParentItemId() {
		return selectedParentNode;
	}

	public String getSelectedItemId() {
		return conditions.getSelectedId();
	}

	public List<String> getSelectedItems() {
		// TODO:
		return null;
	}

	public TreeConditions getConditions() {
		return conditions;
	}

	/**
	 * After when called addRow,removeRow,removeAll,updateRow, we have to call
	 * this method refreshing data set.
	 */
	public void refresh() {
		try {
			OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
			DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
			evaContext.setVariableValue("treeCondition", conditions);
			evaContext.setVariableValue("page", AjaxContextHelper.getAjaxContext());
			evaContext.setVariableValue("selectedNode", this.getSelectedItemId());
			ooeeContext.setDefaultEvaluationContext(evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
			List<TreeItem> result = (List<TreeItem>)expandExpr.evaluate(ooeeContext);
			
			JSONArray jsonArray = new JSONArray(result);
			IDataItem dataItem = AjaxContextHelper.createDataItem();
			dataItem.setUiid(this.getId());
			dataItem.setJsHandler(IJSHandlerCollections.TREE_REFRESH);
			dataItem.setData(jsonArray.toString());
			dataItem.setFrameInfo(this.getFrameInfo());

			AjaxContext ajaxContext = AjaxContextHelper.getAjaxContext();
			ajaxContext.addDataItem(dataItem);
		} catch (Exception e) {
			logger.error("error occurrs while refreshing tree: " + this.getId(), e);
		}
	}
	
	public String expand() {
		if (expandExpr == null) {
			return "";
		}
		if (this.getSelectedItemId() == null) {
			logger.warn("The selected node must not be null form " + this.getId() + " tree.");
			return "";
		}
		
		try {
			OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
			DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
			evaContext.setVariableValue("treeCondition", conditions);
			evaContext.setVariableValue("page", AjaxContextHelper.getAjaxContext());
			evaContext.setVariableValue("tree", this);
			evaContext.setVariableValue("selectedNode", this.getSelectedItemId());
			ooeeContext.setDefaultEvaluationContext(evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
			List<TreeItem> result = (List<TreeItem>)expandExpr.evaluate(ooeeContext);
			
			JSONArray jsonArray = new JSONArray(result);
			return jsonArray.toString();
		} catch (Exception e) {
			logger.error("error occurrs while expanding tree: " + this.getId(), e);
		}
		return "[]";
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject json = super.toJSON();
		json.put("dataModel", dataModel);
		if (this.selectedNodeName != null) {
			json.put("selectedNode", selectedNodeName);
		}
		if (this.selectedParentNode != null) {
			json.put("selectedPNode", selectedParentNode);
		}
		return json;
	}
	
	@SuppressWarnings("unchecked")
	public void fromJSON(JSONObject json) throws Exception {
		String entityName = json.getString("entity");
		UIFormObject formObject = PageCacheManager.getUIForm(entityName);
		Map<String, Object> attributes = formObject.getComponentProperty(this.getId(), true);
		this.expandExpr = (ExpressionType)attributes.get("expandExpr");
		this.dataModel = json.getJSONArray("dataModel").toList();
		this.selectedNodeName = json.has("selectedNode") ? json.getString("selectedNode") : null;
		this.selectedParentNode = json.has("selectedPNode") ? json.getString("selectedPNode") : null;
		super.fromJSON(json);
	}
}
