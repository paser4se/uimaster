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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.datamodel.page.UITableColumnType;
import org.shaolin.bmdp.datamodel.page.UITableSelectModeType;
import org.shaolin.bmdp.datamodel.page.UITableStatsType;
import org.shaolin.bmdp.json.JSONArray;
import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.be.IBusinessEntity;
import org.shaolin.bmdp.runtime.be.IPersistentEntity;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.bmdp.utils.StringUtil;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.DefaultParsingContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.javacc.exception.ParsingException;
import org.shaolin.uimaster.page.AjaxActionHelper;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.DisposableBfString;
import org.shaolin.uimaster.page.IJSHandlerCollections;
import org.shaolin.uimaster.page.ajax.json.IDataItem;
import org.shaolin.uimaster.page.javacc.UIVariableUtil;
import org.shaolin.uimaster.page.od.ODContext;
import org.shaolin.uimaster.page.report.ImportTableToExcel;
import org.shaolin.uimaster.page.widgets.HTMLImageType;

/**
 * 
 * @author Shaolin Wu
 */
public class Table extends Widget<Table> implements Serializable {
	private static final long serialVersionUID = -1744731434666233557L;

	private TableConditions conditions = TableConditions.createCondition();

	private transient List<Object> listData;

	private boolean isAppendRowMode;
	
	private boolean isEditableCell;
	
	private boolean isSliderMode;
	
	private boolean disableRefreshClear;
	
	public static final ExpressionType statsExpr = new ExpressionType();
	static{
		statsExpr.setExpressionString("import org.shaolin.bmdp.analyzer.dao.AanlysisModelCust; {\n"
				+ "return AanlysisModelCust.INSTANCE.stats($tableName,$conditions);\n"
				+ "}");
		OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
		DefaultParsingContext pContext = new DefaultParsingContext();
		pContext.setVariableClass("tableName", String.class);
		pContext.setVariableClass("conditions", Map.class);
		
		ooeeContext.setDefaultParsingContext(pContext);
		ooeeContext.setParsingContextObject("$", pContext);
		try {
			statsExpr.parse(ooeeContext);
		} catch (ParsingException e) {
			Logger.getLogger(Table.class.getClass()).warn("Table statistic function is disabled due to :" + e.getMessage(), e);
		}
	}
	
	private ExpressionType queryExpr;

	private List<UITableColumnType> columns;
	
	private UITableSelectModeType selectMode;
	
	private UITableStatsType stats;

	public Table(String tableId, HttpServletRequest request) {
		super(tableId, null);
	}

	public Table(String id, Layout layout) {
		super(id, layout);
		this._setWidgetLabel(id);
	}

	public void setSelectMode(UITableSelectModeType selectMode) {
		this.selectMode = selectMode == null ? UITableSelectModeType.MULTIPLE
				: selectMode;
	}
	
	public void setStatistic(UITableStatsType stats) {
		this.stats = stats;
	}
	
	public Table addAttribute(String name, Object value, boolean update)
    {
		if ("selectedIndex".equals(name)) {
			conditions.setCurrentSelectedIndex(Integer.valueOf(value.toString()));
		} else if ("selectedIndexs".equals(name)) {
			if (!"".equals(value)) {
				String[] values = ((String)value).split(",");
				ArrayList<Integer> intValues = new ArrayList<Integer>(values.length);
				for (int i=0; i<values.length; i++) {
					try {
						Integer v = Integer.valueOf(values[i]);
						// filtered the duplications.
						if (!intValues.contains(v)) {
							intValues.add(v);
						}
					} catch (NumberFormatException e) {
						// filtered the illegal number.
					}
				}
				conditions.setSelectedIndex(intValues.toArray(new Integer[intValues.size()]));
			}
		} else if ("conditions".equals(name)) {
			try {
				JSONArray array = new JSONArray(value.toString());
				int length = array.length();
				for (int i=0; i<length; i++) {
					JSONObject item = array.getJSONObject(i);
					String _name = item.getString("name");
					String _value = item.getString("value");
					this.updateFilter(_name.trim(), _value.trim());
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Table conditions updated: " + conditions.getCondition());
				}
			} catch (JSONException e) {
				logger.error("error occurrs while synchronizing the value from the page.", e);
			}
		} else if ("bodyJson".equals(name)) {
			try {
				// sync the body updated cells.
				JSONArray array = new JSONArray(value.toString());
				int length = array.length();
				for (int i=0; i<length; i++) {
					JSONObject item = array.getJSONObject(i);
					if (!item.has("updated")) {
						continue;
					}
					Object rowObject = this.listData.get(i);
					for (UITableColumnType col : columns) {
						if (item.has(col.getBeFieldId())) {
							this.updateCell(col, col.getBeFieldId(), item.getString(col.getBeFieldId()).trim(), rowObject);
						}
					}
				}
			} catch (JSONException e) {
				logger.error("error occurrs while synchronizing the value from the page.", e);
			}
		} else {
			super.addAttribute(name, value, update);
		}
		return this;
    }
	
	public List<Object> getSelectedRows() {
		if (conditions.getSelectedIndex() == null 
				|| conditions.getSelectedIndex().length == 0) {
			return Collections.emptyList();
		}
		List<Object> selectedRows = new ArrayList<Object>();
		for (int i : conditions.getSelectedIndex()) {
			selectedRows.add(listData.get(i));
		}
		return selectedRows;
	}
	
	public Object getSelectedRow() {
		if (conditions.getCurrentSelectedIndex() < 0 
				|| conditions.getCurrentSelectedIndex() > listData.size()) {
			return null;
		}
		if (!listData.isEmpty()) {
			return listData.get(conditions.getCurrentSelectedIndex());
		} 
		return null;
	}

	public int getSelectedIndex() {
		return conditions.getCurrentSelectedIndex();
	}

	public boolean isSliderMode() {
		return this.isSliderMode || (UserContext.isMobileRequest() && !isEditableCell());
	}
	
	public void markSliderMode() {
		this.isSliderMode = true;
	}
	
	public void disableRefreshClear() {
		this.disableRefreshClear = true;
	}
	
	public List<Object> getListData() {
		if (listData == null) {
			listData = new ArrayList<Object>();
		}
		return listData;
	}

	public void setListData(List<Object> listData) {
		setListData(listData, false);
	}

	public void setListData(List<Object> listData, boolean update) {
		if (update) {
			this.clear();
		}
		this.listData = listData;
		if (update) {
			this.refresh(this.listData);
		}
	}
	
	public boolean isAppendRowMode() {
		return isAppendRowMode;
	}

	public void setAppendRowMode(boolean isAppendRowMode) {
		this.isAppendRowMode = isAppendRowMode;
	}
	
	public void setEditableCell(boolean isEditableCell) {
		this.isEditableCell = isEditableCell;
	}
	
	public boolean isEditableCell() {
		return this.isEditableCell;
	}
	
	public TableConditions getConditions() {
		return conditions;
	}

	public void setConditions(TableConditions conditions) {
		if (conditions != null) {
			this.conditions = conditions;
		}
	}
	
	public void setColumns(List<UITableColumnType> columns) {
		this.columns = columns;
	}
	
	public void setQueryExpr(ExpressionType queryExpr) {
		this.queryExpr = queryExpr;
	}

	public String getColumnId(int index) {
		int count=0;
		for (UITableColumnType col : columns) {
			if (count++ == index){
				String v = col.getBeFieldId();
				return v.substring(v.lastIndexOf(".") + 1);
			}
		}
		return null;
	}
	
	private void updateCell(UITableColumnType col, String field, String value, Object rowObject) {
		try {
			OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
			DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
			evaContext.setVariableValue("rowBE", rowObject);
			evaContext.setVariableValue("value", value);
			evaContext.setVariableValue("filterId", field);
			ooeeContext.setDefaultEvaluationContext(evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
			
			if (col.getUpdateCondition() != null) {
				col.getUpdateCondition().getExpression().evaluate(ooeeContext);
			}
		} catch (Exception e) {
			logger.error("error occurrs while updating table conditions. "  + this.getId() 
					+ ", expr: " + col.getUpdateCondition().getExpression().getExpressionString(), e);
		}
	}
	
	public Table updateFilter(String field, String value) {
		for (UITableColumnType col : columns) {
			if (col.getBeFieldId().equals(field) 
					|| (col.getUiType().getType().equals("DateRange") 
							&& (col.getUiType().getStartCondition().equals(field) 
									|| col.getUiType().getEndCondition().equals(field)))){
				if (col.getUiType().getType().equalsIgnoreCase("label")) {
					break;
				}
				try {
					OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
					DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
					evaContext.setVariableValue("rowBE", 
								conditions.getCondition() != null 
								? conditions.getCondition() : conditions.getAdditionalCondition());
					evaContext.setVariableValue("value", value);
					evaContext.setVariableValue("filterId", field);
					ooeeContext.setDefaultEvaluationContext(evaContext);
					ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
					
					if (col.getUpdateCondition() != null) {
						if (logger.isDebugEnabled()) {
							logger.debug("Update table condition field: " + field + ", value: " + value);
						}
						col.getUpdateCondition().getExpression().evaluate(ooeeContext);
					}
				} catch (Exception e) {
					logger.error("error occurrs while updating table conditions."  + this.getId(), e);
				}
				break;
			}
		}
		return this;
	}
	
	public Table updateFilter(Object rowObject, String field, String value) {
		for (UITableColumnType col : columns) {
			if (col.getBeFieldId().equals(field) 
					|| (col.getUiType().getType().equals("DateRange") 
							&& (col.getUiType().getStartCondition().equals(field) 
									|| col.getUiType().getEndCondition().equals(field)))){
				try {
					OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
					DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
					evaContext.setVariableValue("rowBE", rowObject);
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
		return this;
	}
	
	public Table setRow(int index, Object object) {
		this.listData.set(index, object);
		return this;
	}
	
	public Table addRow(Object object) {
		this.addRow(object, true);
		return this;
	}
	
	public Table addRow(Object object, boolean needUpdate) {
		this.listData.add(object);
		if (needUpdate) {
			IDataItem dataItem = AjaxActionHelper.createDataItem();
			dataItem.setUiid(this.getId());
			dataItem.setJsHandler(IJSHandlerCollections.TABLE_UPDATE);
			if (this.isSliderMode()) {
				dataItem.setData(this.refreshPull0(this.listData));
			} else {
				dataItem.setData(this.refreshTable0(this.listData));
			}
			dataItem.setFrameInfo(this.getFrameInfo());

			AjaxContext ajaxContext = AjaxActionHelper.getAjaxContext();
			ajaxContext.addDataItem(dataItem);
		}
		return this;
	}
	
	public Object deleteRow(int index) {
		if (this.listData.size() <= index) {
			return null;
		}
		Object obj = this.listData.remove(index);
		
		IDataItem dataItem = AjaxActionHelper.createDataItem();
		dataItem.setUiid(this.getId());
		dataItem.setJsHandler(IJSHandlerCollections.TABLE_UPDATE);
		if (this.isSliderMode()) {
			dataItem.setData(this.refreshPull0(this.listData));
		} else {
			dataItem.setData(this.refreshTable0(this.listData));
		}
		dataItem.setFrameInfo(this.getFrameInfo());

		AjaxContext ajaxContext = AjaxActionHelper.getAjaxContext();
		ajaxContext.addDataItem(dataItem);
		
		return obj;
	}
	
	public Table clear() {
		this.listData.clear();
		
		this.refresh(this.listData);
		return this;
	}
	
	/**
	 * Refresh from the query expression.
	 */
	public Table refresh() {
		if (this.isSliderMode()) {
			conditions.setPullAction("filter");
		}
		IDataItem dataItem = AjaxActionHelper.createDataItem();
		dataItem.setUiid(this.getId());
		dataItem.setJsHandler(IJSHandlerCollections.TABLE_UPDATE);
		dataItem.setData(this.refresh0());
		dataItem.setFrameInfo(this.getFrameInfo());
        AjaxActionHelper.getAjaxContext().addDataItem(dataItem);
        return this;
	}
	
	/**
	 * Refresh from the giving rows
	 * 
	 * @param rows
	 */
	public Table refresh(List rows) {
		IDataItem dataItem = AjaxActionHelper.createDataItem();
		dataItem.setUiid(this.getId());
		dataItem.setJsHandler(IJSHandlerCollections.TABLE_UPDATE);
		if (this.isSliderMode()) {
			dataItem.setData(this.refreshPull0(rows));
		} else {
			dataItem.setData(this.refreshTable0(rows));
		}
		dataItem.setFrameInfo(this.getFrameInfo());
        AjaxActionHelper.getAjaxContext().addDataItem(dataItem);
        return this;
	}
	
	public String refresh0() {
		boolean isMobPulling = false;
		if (this.isSliderMode()) {
			long value = 0;
			for (Object be : listData) {
				if (be instanceof IPersistentEntity) {
					long id = ((IPersistentEntity)be).getId();
					if (value == 0) {
						value = id;
						continue;
					}
					if (conditions.getPullAction().equals("new") && (value < id)) {
						value = id; //get the biggest id
					} else if (conditions.getPullAction().equals("history") && (value > id)) {
						value = id; //get the smallest id
					}
				}
			}
			isMobPulling = true;
			conditions.setPullId(value);
			UserContext.getUserContext().setPullAction(conditions.getPullAction());
			UserContext.getUserContext().setPullId(value);
		} else {
			if (!this.disableRefreshClear && this.listData != null) {
				this.listData.clear();
			}
		}
		
		try {
			OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
			DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
			evaContext.setVariableValue("tableCondition", conditions);
			evaContext.setVariableValue("page", AjaxActionHelper.getAjaxContext());
			evaContext.setVariableValue("table", this);
			evaContext.setVariableValue("formId", AjaxActionHelper.getAjaxContext().getEntityPrefix());
			ooeeContext.setDefaultEvaluationContext(evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.GLOBAL_TAG, evaContext);
			
			List<Object> rows = (List<Object>)queryExpr.evaluate(ooeeContext);
			if (rows == null) {
				rows = Collections.emptyList();
			}
			if (isMobPulling) {
				//TODO:
				String newRows = refreshPull0(rows);
				if (UserContext.getUserContext().isPullNew()) {
					rows.addAll(this.listData);
					this.listData = rows;
				} else if (UserContext.getUserContext().isPullRefresh()) {
					this.listData = rows;
				} else {
					this.listData.addAll(rows);
				}
				return newRows;
			} else {
				this.listData = rows;
				return refreshTable0(rows);
			}
		} catch (EvaluationException e) {
			logger.error("error occurrs while refreshing table: " + this.getId(), e);
			return "";
		} finally {
			UserContext.getUserContext().setPullId(0);
			UserContext.getUserContext().setPullAction(null);
		}
	}
	
	private String refreshPull0(List rows) {
		try {
			OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
			DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
			evaContext.setVariableValue("tableCondition", conditions);
			evaContext.setVariableValue("page", AjaxActionHelper.getAjaxContext());
			evaContext.setVariableValue("table", this);
			evaContext.setVariableValue("formId", AjaxActionHelper.getAjaxContext().getEntityPrefix());
			ooeeContext.setDefaultEvaluationContext(evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.GLOBAL_TAG, evaContext);
			
			long totalCount = 0;
			if (rows != null && rows.size() > 0) {
				Object firstItem = rows.get(0);
				if (firstItem instanceof IBusinessEntity) {
					Object v = ((IBusinessEntity)firstItem).get_extField().get("count");
					totalCount = (v == null? 0 : (long)v);
				} else {
					totalCount = rows.size();
				}
			} 
			if (totalCount == 0) {
				totalCount = rows.size();
			}
			
			StringBuilder sb = DisposableBfString.getBuffer();
			try {
	        sb.append("{\"totalCount\":");
	        sb.append(totalCount);
	        sb.append(",");
	        sb.append("\"rows\":[");
	        int count = 0;
	        for (Object be : rows) {
	        	evaContext.setVariableValue("rowBE", be);
	        	evaContext.setVariableValue("index", count);
        		StringBuilder imageSB = new StringBuilder();
        		StringBuilder attrsSB = new StringBuilder();
        		StringBuilder htmlAttrsSB = new StringBuilder();
        		StringBuilder swiperSB = new StringBuilder();
        		swiperSB.append("<div class='swiper-slide'>");
        		for (UITableColumnType col : columns) {
        			if ("Image".equalsIgnoreCase(col.getUiType().getType())) {
        				imageSB.append("<div class='p'>");
        				Object value = col.getRowExpression().getExpression().evaluate(
								ooeeContext);
		        		if (value == null) {
							value = "";
						}
		        		value = HTMLImageType.generateSimple(
		        				AjaxActionHelper.getAjaxContext().getRequest(), value.toString(), 100, 100);
		        		imageSB.append(value);
		        		imageSB.append("</div>");
        			} else if ("HTML".equalsIgnoreCase(col.getUiType().getType())
        					|| "HTMLItem".equals(col.getUiType().getType())) {
        				htmlAttrsSB.append("<div class='d'>");
        				Object value = col.getRowExpression().getExpression().evaluate(
								ooeeContext);
		        		if (value == null) {
							value = "";
						}
		        		value = value.toString();
		        		htmlAttrsSB.append(value);
		        		htmlAttrsSB.append("</div>");
        			} else {
		        		Object value = col.getRowExpression().getExpression().evaluate(
								ooeeContext);
		        		if (value == null) {
							value = "";
						}
		        		attrsSB.append("<div class='di'>");
		        		attrsSB.append(UIVariableUtil.getI18NProperty(col.getTitle())).append(":").append(value);
		        		attrsSB.append("</div>");
        			}
	        	}
        		swiperSB.append(imageSB.toString());
        		if (attrsSB.length() > 0) {
        			swiperSB.append("<div class=\"d\">");
        			swiperSB.append(attrsSB.toString());
        			swiperSB.append("</div>");
    			}
        		swiperSB.append(htmlAttrsSB.toString());
        		swiperSB.append("</div>");
        		sb.append("{\"value\":\"").append(StringUtil.escapeHtmlToBytes(swiperSB.toString())).append("\"},");
	        	
	        	count++;
	        }
	        if (rows.size() > 0) {
	        	sb.deleteCharAt(sb.length()-1);
	        }
	        sb.append("]}");
	        
	        return sb.toString();
			} finally {
				DisposableBfString.release(sb);
			}
		} catch (Exception e) {
			logger.error("error occurrs while refreshing table: " + this.getId(), e);
		}
		return "";
	}
	
	/**
	 * After when called addRow,removeRow,removeAll,updateRow, we have to call
	 * this method refreshing data set.
	 */
	private String refreshTable0(List rows) {
		try {
			OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
			DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
			evaContext.setVariableValue("tableCondition", conditions);
			evaContext.setVariableValue("page", AjaxActionHelper.getAjaxContext());
			evaContext.setVariableValue("table", this);
			ooeeContext.setDefaultEvaluationContext(evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.GLOBAL_TAG, evaContext);
			
			long totalCount = 0;
			if (rows != null && rows.size() > 0) {
				Object firstItem = rows.get(0);
				if (firstItem instanceof IBusinessEntity) {
					Object v = ((IBusinessEntity)firstItem).get_extField().get("count");
					totalCount = (v == null? 0 : (long)v);
				} else {
					totalCount = rows.size();
				}
			} 
			
			StringBuilder sb = DisposableBfString.getBuffer();
			try {
	        sb.append("{\"recordsFiltered\":");
	        sb.append(totalCount);
	        sb.append(",\"recordsTotal\":");
	        sb.append(totalCount);
	        sb.append(",\"selectedIndex\":");
	        sb.append(this.getSelectedIndex());
	        sb.append(",\"pageIndex\":");
	        sb.append(this.conditions.getOffset());
	        sb.append(",");
	        sb.append("\"data\":[");
	        int count = 0;
	        for (Object be : rows) {
	        	evaContext.setVariableValue("rowBE", be);
	        	evaContext.setVariableValue("index", count);
	        	
	        	sb.append("[");
	        	if (this.selectMode == UITableSelectModeType.MULTIPLE) {
	        		sb.append("\"checkbox,"+count+"\",");
	        	} else if (this.selectMode == UITableSelectModeType.SINGLE) {
	        		sb.append("\"radio,"+count+"\",");
	        	} else {
	        		sb.append("\"\",");
	        	}
	        	for (UITableColumnType col : columns) {
	        		Object value = col.getRowExpression().getExpression().evaluate(
							ooeeContext);
	        		if (value == null) {
						value = "";
					}
	        		sb.append("\"");
	        		if ("Image".equalsIgnoreCase(col.getUiType().getType())) {
    					sb.append(StringUtil.escapeHtmlToBytes(HTMLImageType.generateSimple(
    							AjaxActionHelper.getAjaxContext().getRequest(), value.toString(), 60, 60)));
	        		} else {
	        			sb.append(StringUtil.escapeHtmlToBytes(value.toString()));
	        		}
	        		sb.append("\",");
	        	}
	        	sb.deleteCharAt(sb.length()-1);
	        	sb.append("],");
	        	
	        	count++;
	        }
	        if (rows.size() > 0) {
	        	sb.deleteCharAt(sb.length()-1);
	        }
	        sb.append("]}");
	        
	        return sb.toString();
			} finally {
				DisposableBfString.release(sb);
			}
		} catch (Exception e) {
			logger.error("error occurrs while refreshing table: " + this.getId(), e);
		}
		return "";
	}
	
	private List<Object[]> getEvaluatedResult() {
		List<Object[]> result = new ArrayList<Object[]>();
		try {
			OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
			DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
			evaContext.setVariableValue("tableCondition", conditions);
			evaContext.setVariableValue("page", AjaxActionHelper.getAjaxContext());
			evaContext.setVariableValue("table", this);
			ooeeContext.setDefaultEvaluationContext(evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.GLOBAL_TAG, evaContext);

	        List<Object> rows = (List<Object>)queryExpr.evaluate(ooeeContext);
	        this.listData = rows;
	        for (Object be : rows) {
	        	evaContext.setVariableValue("rowBE", be);
	        	
				int i = 0;
	        	Object[] rowValues = new Object[columns.size()];
	        	for (UITableColumnType col : columns) {
	        		Object cellValue = col.getRowExpression().getExpression().evaluate(
							ooeeContext);
	        		if (cellValue == null) {
						cellValue = "";
					}
	        		rowValues[i++] = cellValue;
	        	}
	        	result.add(rowValues);
	        }
		} catch (Exception e) {
			logger.error("error occurrs while evaluating table values.", e);
		}
		return result;
	}
	
	public void importAsExcel() {
		
	}
	
	public void exportAsExcel(OutputStream output) throws IOException {
		int i = 0;
		String[] columnTitles = new String[columns.size()];
		for (UITableColumnType col : columns) {
			columnTitles[i++] = UIVariableUtil.getI18NProperty(col.getTitle());
		}
		String name = "Data Report";
		ImportTableToExcel importTable = new ImportTableToExcel(getEvaluatedResult());
		importTable.createWorkbook(name, columnTitles).write(output);
	}
	
	public void showStatistic() {
		if (this.stats != null && this.stats.getTableName() != null 
				&& this.stats.getTableName().trim().length() > 0) {
			try {
				OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
				DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
				evaContext.setVariableValue("tableName", this.stats.getTableName());
				evaContext.setVariableValue("conditions", new HashMap());
				ooeeContext.setDefaultEvaluationContext(evaContext);
				ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
				List data = (List) statsExpr.evaluate(ooeeContext);
				HashMap<String, Object> input = new HashMap<String, Object>();
				input.put("data", data);
	            RefForm form = new RefForm("statsChartFrom", this.stats.getUiFrom(), input);
	            AjaxActionHelper.getAjaxContext().addElement(form);
	            
	            form.openInWindows("Chart Analysis Report", null, 690, 400);
			} catch (Exception e) {
				logger.error("error occurrs while showing chart on table: " + this.getId(), e);
			}
		}
	}
	
	public void show() {
		IDataItem dataItem = AjaxActionHelper.createDataItem();
		dataItem.setUiid(this.getId());
		dataItem.setJsHandler(IJSHandlerCollections.JAVASCRIPT);
		dataItem.setJs("{ $($(elementList['"+this.getId()+"']).parent().parent().parent()).css(\"display\",\"block\");}");
		dataItem.setFrameInfo(this.getFrameInfo());
        AjaxActionHelper.getAjaxContext().addDataItem(dataItem);
	}
	
	public void hide() {
		IDataItem dataItem = AjaxActionHelper.createDataItem();
		dataItem.setUiid(this.getId());
		dataItem.setJsHandler(IJSHandlerCollections.JAVASCRIPT);
		dataItem.setJs("{ $($(elementList['"+this.getId()+"']).parent().parent().parent()).css(\"display\",\"none\");}");
		dataItem.setFrameInfo(this.getFrameInfo());
        AjaxActionHelper.getAjaxContext().addDataItem(dataItem);
	}

}
