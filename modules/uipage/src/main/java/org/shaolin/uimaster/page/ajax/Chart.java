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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.datamodel.page.UITableColumnType;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.shaolin.uimaster.page.AjaxActionHelper;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.IJSHandlerCollections;
import org.shaolin.uimaster.page.ajax.json.IDataItem;
import org.shaolin.uimaster.page.ajax.json.JSONArray;
import org.shaolin.uimaster.page.ajax.json.JSONException;
import org.shaolin.uimaster.page.ajax.json.JSONObject;
import org.shaolin.uimaster.page.od.ODContext;
import org.shaolin.uimaster.page.widgets.HTMLChartBarType;
import org.shaolin.uimaster.page.widgets.HTMLChartDoughnutType;
import org.shaolin.uimaster.page.widgets.HTMLChartLinearType;
import org.shaolin.uimaster.page.widgets.HTMLChartPieType;
import org.shaolin.uimaster.page.widgets.HTMLChartPolarPieType;
import org.shaolin.uimaster.page.widgets.HTMLChartRadarType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shaolin Wu
 */
public class Chart extends Widget implements Serializable {
	
	private static final long serialVersionUID = -1744731434666233557L;
	
	private static final Logger logger = LoggerFactory.getLogger(Chart.class);
	
	private final Class type;
	
	private Object condition;
	
	private ExpressionType queryExpr;
	
	private List<UITableColumnType> columns;
	
	public Chart(String id, Layout layout, Class type) {
		super(id, layout);
		this.type = type;
	}
	
	public void setColumns(List<UITableColumnType> columns, ExpressionType queryExpr, Object condition) {
		this.columns = columns;
		this.queryExpr = queryExpr;
		this.condition = condition;
	}
	
	public void refresh() {
		IDataItem dataItem = AjaxActionHelper.createDataItem();
		dataItem.setUiid(this.getId());
		dataItem.setJsHandler(IJSHandlerCollections.CHART_ADDDATA);
		dataItem.setData(this.refresh0());
		dataItem.setFrameInfo(this.getFrameInfo());

		AjaxContext ajaxContext = AjaxActionHelper.getAjaxContext();
		ajaxContext.addDataItem(dataItem);
	}
	
	public void revert(int popCount) {
		IDataItem dataItem = AjaxActionHelper.createDataItem();
		dataItem.setUiid(this.getId());
		dataItem.setJsHandler(IJSHandlerCollections.CHART_REMOVEDATA);
		dataItem.setData("{popCount: "+popCount+"}");
		dataItem.setFrameInfo(this.getFrameInfo());

		AjaxContext ajaxContext = AjaxActionHelper.getAjaxContext();
		ajaxContext.addDataItem(dataItem);
	}

	public void addAttribute(String name, Object value, boolean update) {
		if ("conditions".equals(name)) {
			try {
				JSONArray array = new JSONArray(value.toString());
				int length = array.length();
				for (int i=0; i<length; i++) {
					JSONObject item = array.getJSONObject(i);
					String _name = item.getString("name");
					String _value = item.getString("value");
					this.updateFilter(_name.trim(), _value.trim());
				}
			} catch (JSONException e) {
				logger.error("error occurrs while synchronizing the value from the page.", e);
			}
		}
    }
	
	private void updateFilter(String field, String value) {
		for (UITableColumnType col : columns) {
			if (col.getBeFieldId().equals(field) 
					|| (col.getUiType().getType().equals("DateRange") 
							&& (col.getUiType().getStartCondition().equals(field) 
									|| col.getUiType().getEndCondition().equals(field)))){
				try {
					OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
					DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
					evaContext.setVariableValue("rowBE", condition);
					evaContext.setVariableValue("value", value);
					evaContext.setVariableValue("filterId", field);
					ooeeContext.setDefaultEvaluationContext(evaContext);
					ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
					
					if (col.getUpdateCondition() != null) {
						col.getUpdateCondition().getExpression().evaluate(ooeeContext);
					}
				} catch (Exception e) {
					logger.error("error occurrs while updating table conditions."  + this.getId(), e);
				}
				break;
			}
		}
	}
	
	private String refresh0() {
		try {
			OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
			DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
			evaContext.setVariableValue("page", AjaxActionHelper.getAjaxContext());
			evaContext.setVariableValue("condition", condition);
			
			ooeeContext.setDefaultEvaluationContext(evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
	
			Object value = queryExpr.evaluate(ooeeContext);
			StringBuilder all = new StringBuilder();
			if (this.type == HTMLChartPieType.class 
					|| this.type == HTMLChartDoughnutType.class
					|| this.type == HTMLChartPolarPieType.class) {
				Map<String, Integer> listData = (Map<String, Integer>)value;
				if (listData != null && !listData.isEmpty()) {
					StringBuffer sb = new StringBuffer("datasets: [{ data: [");
					Set<String> keys = listData.keySet();
					for (String key : keys) {
						sb.append(listData.get(key)).append(",");
					}
					sb.deleteCharAt(sb.length()-1);
					sb.append("]}]");
					all.append(sb.toString());
				}
			} else if (this.type == HTMLChartRadarType.class 
					|| this.type == HTMLChartLinearType.class
					|| this.type == HTMLChartBarType.class) {
				List<Object> listData = (List<Object>)value;
				if (!listData.isEmpty() && listData.size() > 0) {
					all.append("datasets: [");
					StringBuilder sb = new StringBuilder();
					// vertical iterator.
					for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
						sb.append("{");
						sb.append(" data:[");
						for (int i = 0; i < listData.size(); i++) {
							evaContext.setVariableValue("rowBE", listData.get(i));
							ooeeContext.setDefaultEvaluationContext(evaContext);
							ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
							Object aPoint = columns.get(columnIndex).getRowExpression().getExpression().evaluate(
									ooeeContext);
							sb.append(aPoint).append(",");
						}
						sb.deleteCharAt(sb.length()-1);
						sb.append("]},");
					}
					sb.deleteCharAt(sb.length()-1);
					all.append(sb.toString());
					all.append("]");
				}
			}
			return all.toString();
		} catch (Exception e) {
			logger.error("error occurrs while refreshing chart: " + this.getId(), e);
		}
		return "";
	}
}
