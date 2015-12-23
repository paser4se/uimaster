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
package org.shaolin.uimaster.page.widgets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.datamodel.page.UITableActionGroupType;
import org.shaolin.bmdp.datamodel.page.UITableActionType;
import org.shaolin.bmdp.datamodel.page.UITableColumnType;
import org.shaolin.bmdp.datamodel.page.UITableSelectModeType;
import org.shaolin.bmdp.datamodel.page.UITableStatsType;
import org.shaolin.bmdp.runtime.be.BEUtil;
import org.shaolin.bmdp.runtime.be.IBusinessEntity;
import org.shaolin.bmdp.runtime.ce.CEUtil;
import org.shaolin.bmdp.runtime.ce.IConstantEntity;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.DefaultParsingContext;
import org.shaolin.javacc.context.EvaluationContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.uimaster.page.HTMLSnapshotContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.ajax.Layout;
import org.shaolin.uimaster.page.ajax.Table;
import org.shaolin.uimaster.page.ajax.TableConditions;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.UIVariableUtil;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.shaolin.uimaster.page.od.ODContext;
import org.shaolin.uimaster.page.od.mappings.ComponentMappingHelper;
import org.shaolin.uimaster.page.security.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLTableType extends HTMLContainerType {
	
	private static final long serialVersionUID = 8795894198097039771L;

	private static final Logger logger = LoggerFactory
			.getLogger(HTMLTableType.class);

	private boolean hasStatistic;
	
	public HTMLTableType() {
	}

	public HTMLTableType(HTMLSnapshotContext context) {
		super(context);
	}

	public HTMLTableType(HTMLSnapshotContext context, String id) {
		super(context, id);
	}

	public List<IBusinessEntity> getListData() {
		return null;
	}

	public void generateBeginHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth) {
		try {
			generateWidget(context);
			super.generateBeginHTML(context, ownerEntity, depth);

			UITableSelectModeType selectMode = (UITableSelectModeType)this.removeAttribute("selectMode");
			if (selectMode == null) {
				selectMode = UITableSelectModeType.SINGLE;
			}
			int defaultRowSize = (Integer)this.removeAttribute("defaultRowSize");
			String totalCount = String.valueOf(this.removeAttribute("totalCount"));
			Boolean isShowActionBar = (Boolean)this.removeAttribute("isShowActionBar");
			Boolean isEditableCell = (Boolean)this.removeAttribute("isEditableCell");
			Boolean isShowBigItem = (Boolean)this.removeAttribute("isShowBigItem");
			if (isShowBigItem==null) {
				isShowBigItem = Boolean.FALSE;
			}
			List<UITableColumnType> columns = (List<UITableColumnType>)this.removeAttribute("columns");
			if (columns == null || columns.size() == 0) {
				return;
			}
			
			String htmlPrefix = this.getPrefix().replace('.', '_');
			String htmlId = this.getPrefix().replace('.', '_') + this.getUIID();
			HTMLUtil.generateTab(context, depth + 1);
			context.generateHTML("<div id='" + htmlId + "ActionBar' class=\"ui-widget-header ui-corner-all\">");
			List<UITableActionType> defaultActions = (List<UITableActionType>)this.removeAttribute("defaultActionGroup");
			if (defaultActions != null) {
				HTMLUtil.generateTab(context, depth + 2);
				String defaultBtnSet = "defaultBtnSet_" + htmlId;
				context.generateHTML("<span id=\""+defaultBtnSet+"\" style=\"display:none;\">");
				for (UITableActionType action: defaultActions){
					HTMLUtil.generateTab(context, depth + 3);
					context.generateHTML("<input type=\"radio\" name=\""+defaultBtnSet+"\" id=\""+ htmlPrefix + action.getUiid());
					if ("refreshTable".equals(action.getFunction())) {
						context.generateHTML("\" onclick=\"javascript:defaultname." + this.getPrefix() + this.getUIID() + ".refresh");
					} else if ("statistic".equals(action.getFunction()) && this.hasStatistic) {
						context.generateHTML("\" onclick=\"javascript:defaultname." + this.getPrefix() + this.getUIID() + ".statistic");
					} else if ("importData".equals(action.getFunction())) {
						context.generateHTML("\" onclick=\"javascript:defaultname." + this.getPrefix() + this.getUIID() + ".importData");
					} else if ("exportData".equals(action.getFunction())) {
						context.generateHTML("\" onclick=\"javascript:defaultname." + this.getPrefix() + this.getUIID() + ".exportData");
					} else {
						context.generateHTML("\" onclick=\"javascript:defaultname.");
						context.generateHTML(this.getPrefix() + action.getFunction());
					}
					context.generateHTML("('" + this.getPrefix() + this.getUIID() + "');\" title='");
					String i18nProperty = UIVariableUtil.getI18NProperty(action.getTitle());
					context.generateHTML(i18nProperty);
					context.generateHTML("' icon=\""+action.getIcon()+"\"><label for=\""+htmlPrefix + action.getUiid()+"\">");
					context.generateHTML(i18nProperty);
					context.generateHTML("</label></input>");
				}
				HTMLUtil.generateTab(context, depth + 2);
				context.generateHTML("</span>");
			}
			List<UITableActionGroupType> actionGroups = (List<UITableActionGroupType>)this.removeAttribute("actionGroups");
			if (actionGroups !=null && actionGroups.size() > 0) {
				for (UITableActionGroupType a : actionGroups) {
					HTMLUtil.generateTab(context, depth + 2);
					int count = 0;
					String btnSetName = "btnSet_" + htmlId + (count++);
					context.generateHTML("<span id=\""+btnSetName+"\" style=\"display:none;\">");
					for (UITableActionType action: a.getActions()){
						HTMLUtil.generateTab(context, depth + 3);
						if("button".equals(a.getType())) {
							context.generateHTML("<button");
						} else if("radio".equals(a.getType())) {
							context.generateHTML("<input type='radio' name='"+btnSetName+"'");
						} else if("checkbox".equals(a.getType())) {
							context.generateHTML("<input type='checkbox'");
						}
						context.generateHTML(" id=\""+htmlPrefix+action.getUiid()+"\" onclick=\"javascript:defaultname.");
						context.generateHTML(this.getPrefix() + action.getFunction());
						context.generateHTML("('" + this.getPrefix() + this.getUIID() + "');\" title='");
						String i18nProperty = UIVariableUtil.getI18NProperty(action.getTitle());
						context.generateHTML(i18nProperty);
						context.generateHTML("' icon=\""+action.getIcon()+"\">");
						
						if("button".equals(a.getType())) {
							context.generateHTML(i18nProperty);
							context.generateHTML("</button>");
						} else if("radio".equals(a.getType())) {
							context.generateHTML("<label for=\""+htmlPrefix+action.getUiid()+"\">");
							context.generateHTML(i18nProperty);
							context.generateHTML("</label></input>");
						} else if("checkbox".equals(a.getType())) {
							context.generateHTML("<label for=\""+action.getUiid()+"\">");
							context.generateHTML(i18nProperty);
							context.generateHTML("</label></input>");
						}
					}
					HTMLUtil.generateTab(context, depth + 2);
					context.generateHTML("</span>");
				}
			}
			HTMLUtil.generateTab(context, depth + 1);
			context.generateHTML("</div>");
			
			HTMLUtil.generateTab(context, depth + 1);
			context.generateHTML("<table id=\"");
			context.generateHTML(getName());
			context.generateHTML("\" class=\"uimaster_table display dataTable\" recordsFiltered='");
			context.generateHTML(totalCount + "");
			context.generateHTML("' recordsTotal='");
			context.generateHTML(totalCount+"' selectMode=\"");
			context.generateHTML(selectMode.value());
			context.generateHTML("\">");

			// generate thead.
			generateTableHead(selectMode, isShowBigItem, context, depth, columns);
			
			// generate tbody.
			HTMLUtil.generateTab(context, depth + 2);
			context.generateHTML("<tbody id=\"\" >");
			List<Object> listData = (List<Object>)this.removeAttribute("query");
			
			// FIXME: here is an issue while accessing the list as Hibernate PersistenList.
			// org.hibernate.collection.internal.Collections
			// org.hibernate.collection.internal.PersistentList
			// org.hibernate.AssertionFailure: collection owner not associated with session:
			if (!listData.isEmpty()) {
				if (!UserContext.isMobileRequest() && isShowBigItem==Boolean.TRUE) {
					generateBigItemBody(context, depth, columns, listData);
				} else {
					generateNormalBody(depth, selectMode, listData, columns);
				}
			}
			HTMLUtil.generateTab(context, depth + 2);
			context.generateHTML("</tbody>");
			HTMLUtil.generateTab(context, depth + 2);
			
			Boolean showFilter = (Boolean)this.removeAttribute("isShowFilter");
			if (!UserContext.isMobileRequest() && (showFilter == Boolean.TRUE || isEditableCell.booleanValue())) {
				context.generateHTML("<tfoot");
				if (isEditableCell.booleanValue()) {
					context.generateHTML(" style=\"display:none;\" editablecell=\"true\" ");
				}
				context.generateHTML(">");
				HTMLUtil.generateTab(context, depth + 3);
				context.generateHTML("<tr>");
				//if (selectMode != UITableSelectModeType.NORMAL) {
				HTMLUtil.generateTab(context, depth + 3);
				context.generateHTML("<th></th>");
				
				String beElement = (String)this.removeAttribute("beElememt");
				DefaultParsingContext pContext = new DefaultParsingContext();
				Class beClass = null;
				try {
        			beClass = BEUtil.getBEImplementClass(beElement);
        		} catch (ClassNotFoundException e) {
        			beClass = Class.forName(beElement);
        		}
				pContext.setVariableClass("rowBE", beClass);
				
				OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
				DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
				ooeeContext.setDefaultEvaluationContext(evaContext);
				ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
				for (UITableColumnType col : columns) {
					HTMLUtil.generateTab(context, depth + 3);
					context.generateHTML("<th>");
					if ("Text".equalsIgnoreCase(col.getUiType().getType())) {
						HTMLTextFieldType textField = new HTMLTextFieldType(context, col.getBeFieldId());
						textField.addAttribute("placeholder", "Search " + UIVariableUtil.getI18NProperty(col.getTitle()));
						textField.addAttribute("title", UIVariableUtil.getI18NProperty(col.getTitle()));
						textField.addStyle("width", "100%");
						textField.generateBeginHTML(context, ownerEntity, depth+1);
						textField.generateEndHTML(context, ownerEntity, depth+1);
					} else if ("ComBox".equalsIgnoreCase(col.getUiType().getType())) {
						List<String> optionValues = new ArrayList<String>();
						List<String> optionDisplayValues = new ArrayList<String>();
						if (col.getComboxExpression() != null) {
							List[] values = (List[])col.getComboxExpression().getExpression().evaluate(
									ooeeContext);
							optionValues = values[0];
							optionDisplayValues = values[1];
						} else {
							Class clazz = ComponentMappingHelper.getComponentPathClass(col.getBeFieldId(), pContext);
							List<IConstantEntity> items = CEUtil.getConstantEntities(clazz.getName());
							for (IConstantEntity item: items) {
								optionValues.add(item.getIntValue() + "");
							}
							for (IConstantEntity item: items) {
								optionDisplayValues.add(item.getDisplayName());
							}
						}
						HTMLComboBoxType combox = new  HTMLComboBoxType(context, col.getBeFieldId());
						combox.setOptionValues(optionValues);
	 					combox.setOptionDisplayValues(optionDisplayValues);
						combox.addStyle("width", "100%");
						combox.generateBeginHTML(context, ownerEntity, depth+1);
						combox.generateEndHTML(context, ownerEntity, depth+1);
					} else if ("CheckBox".equalsIgnoreCase(col.getUiType().getType())) {
						HTMLCheckBoxType checkBox = new HTMLCheckBoxType(context, col.getBeFieldId());
						checkBox.addAttribute("title", UIVariableUtil.getI18NProperty(col.getTitle()));
						checkBox.addAttribute("label", "");
						checkBox.generateBeginHTML(context, ownerEntity, depth+1);
						checkBox.generateEndHTML(context, ownerEntity, depth+1);
					} else if ("Date".equalsIgnoreCase(col.getUiType().getType())) {
						HTMLDateType date = new HTMLDateType(context, col.getBeFieldId());
						date.generateBeginHTML(context, ownerEntity, depth+1);
						date.generateEndHTML(context, ownerEntity, depth+1);
					} else if ("DateRange".equalsIgnoreCase(col.getUiType().getType())) {
						HTMLDateType start = new HTMLDateType(context, col.getUiType().getStartCondition());
						start.setRange(true);
						start.addStyle("width", "100px");
						HTMLDateType end = new HTMLDateType(context, col.getUiType().getEndCondition());
						end.setRange(true);
						end.addStyle("width", "100px");
						start.generateBeginHTML(context, ownerEntity, depth+1);
						start.generateEndHTML(context, ownerEntity, depth+1);
						context.generateHTML("&nbsp;&nbsp;");
						end.generateBeginHTML(context, ownerEntity, depth+1);
						end.generateEndHTML(context, ownerEntity, depth+1);
					} else if ("Label".equalsIgnoreCase(col.getUiType().getType())) {
						//Label column does not need to look for search.
					} 
					context.generateHTML("</th>");
				}
				HTMLUtil.generateTab(context, depth + 3);
				context.generateHTML("</tr>");
				HTMLUtil.generateTab(context, depth + 2);
				context.generateHTML("</tfoot>");
			}
			
			HTMLUtil.generateTab(context, depth + 1);
			context.generateHTML("</table>");
			generateEndWidget(context);

		} catch (Exception e) {
			logger.error("error. in entity: " + getUIEntityName(), e);
		}
	}

	private void generateTableHead(UITableSelectModeType selectMode, Boolean isShowBigItem, 
			HTMLSnapshotContext context, int depth, List<UITableColumnType> columns) {
		HTMLUtil.generateTab(context, depth + 2);
		context.generateHTML("<thead "+(isShowBigItem==Boolean.TRUE?"style='display:none;'":"")+">");
		HTMLUtil.generateTab(context, depth + 3);
		context.generateHTML("<tr>");
		
		// generate selection at the first column
		HTMLUtil.generateTab(context, depth + 3);
		context.generateHTML("<th id=\"");
		context.generateHTML(getName());
		context.generateHTML("_SelectColumn\" name=\"\" orderable=\"false\" htmlType=\"");
		context.generateHTML(selectMode.value());
		context.generateHTML("\" title=\"\" style=\"width:10px;padding:0px;");
		if (selectMode == UITableSelectModeType.NORMAL) {
			context.generateHTML("display:none;\">");
		} else {
			context.generateHTML("\">");
		}
		if (selectMode == UITableSelectModeType.MULTIPLE) {
			context.generateHTML("<input type=\"checkbox\" name=\"\" onclick=\"\" />");
		} else {
			context.generateHTML("");
		}
		context.generateHTML("</th>");
		
		if (UserContext.isMobileRequest()) {
			HTMLUtil.generateTab(context, depth + 3);
			for (UITableColumnType col : columns) {
				// find image column.
				if ("Image".equals(col.getUiType().getType())) {
					context.generateHTML("<th id=\"");
					context.generateHTML(col.getBeFieldId());
					context.generateHTML("\" htmlType=\"");
					context.generateHTML(col.getUiType().getType());
					context.generateHTML("\" title=\"");
					context.generateHTML(UIVariableUtil.getI18NProperty(col.getTitle()));
					context.generateHTML("\">");
					context.generateHTML(UIVariableUtil.getI18NProperty(col.getTitle()));
					context.generateHTML("</th>");
				}
			}
			context.generateHTML("<th id=\"attColumn\" htmlType=\"Label\" title=\"\">");
			context.generateHTML("</th>");
			// find html column.
			for (UITableColumnType col : columns) {
				if ("HTML".equals(col.getUiType().getType())) {
					context.generateHTML("<th id=\"");
					context.generateHTML(col.getBeFieldId());
					context.generateHTML("\" htmlType=\"");
					context.generateHTML(col.getUiType().getType());
					context.generateHTML("\" title=\"");
					context.generateHTML(UIVariableUtil.getI18NProperty(col.getTitle()));
					context.generateHTML("\">");
					context.generateHTML(UIVariableUtil.getI18NProperty(col.getTitle()));
					context.generateHTML("</th>");
				}
			}
		} else if (isShowBigItem==Boolean.TRUE) {
			// default show 4 columns in one row.
			context.generateHTML("<th>1</th><th>2</th><th>3</th><th>4</th>");
		} else {
			for (UITableColumnType col : columns) {
				HTMLUtil.generateTab(context, depth + 3);
				context.generateHTML("<th id=\"");
				context.generateHTML(col.getBeFieldId());
				context.generateHTML("\" htmlType=\"");
				context.generateHTML(col.getUiType().getType());
				context.generateHTML("\" title=\"");
				context.generateHTML(UIVariableUtil.getI18NProperty(col.getTitle()));
				context.generateHTML("\">");
				context.generateHTML(UIVariableUtil.getI18NProperty(col.getTitle()));
				context.generateHTML("</th>");
			}
		}

		HTMLUtil.generateTab(context, depth + 3);
		context.generateHTML("</tr>");
		HTMLUtil.generateTab(context, depth + 2);
		context.generateHTML("</thead>");
	}

	private void generateNormalBody(int depth, UITableSelectModeType selectMode, List listData, List<UITableColumnType> columns)
		throws Exception {
		int count = 0;
		for (Object be : listData) {
			OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
			DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
			evaContext.setVariableValue("rowBE", be);
			evaContext.setVariableValue("index", count);
			ooeeContext.setDefaultEvaluationContext(evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
			
			ExpressionType rowFilter = (ExpressionType)this.getAttribute("rowFilterExpr");
			boolean pass = (boolean)rowFilter.evaluate(ooeeContext);
			if (!pass) {
				continue;
			}
			
			HTMLUtil.generateTab(context, depth + 3);
			context.generateHTML("<tr>");
			HTMLUtil.generateTab(context, depth + 3);
			if (selectMode == UITableSelectModeType.MULTIPLE) {
				context.generateHTML("<td>checkbox,"+count+"</td>");
			} else if (selectMode == UITableSelectModeType.SINGLE) {
				context.generateHTML("<td>radio,"+count+"</td>");
			} else {
				context.generateHTML("<td></td>");
			}
			
			if (UserContext.isMobileRequest()) {
				StringBuffer attrsSB = new StringBuffer();
				StringBuffer htmlAttrsSB = new StringBuffer();
				attrsSB.append("<td title=\"\">");
				for (UITableColumnType col : columns) {
					// find image column at the second column.
					if ("Image".equals(col.getUiType().getType())) {
						context.generateHTML("<td title=\"\">");
						Object value = col.getRowExpression().getExpression().evaluate(
								ooeeContext);
						if (value == null) {
							value = "";
						}
						context.generateHTML(UIVariableUtil.getI18NProperty(col.getTitle()));
						context.generateHTML(":");
						context.generateHTML(value.toString());
						context.generateHTML(", ");
						context.generateHTML("</td>");
					} else if ("HTML".equals(col.getUiType().getType())
							|| "HTMLItem".equals(col.getUiType().getType())) {
						htmlAttrsSB.append("<td title=\"\">");
						Object value = col.getRowExpression().getExpression().evaluate(
								ooeeContext);
						if (value == null) {
							value = "";
						}
						htmlAttrsSB.append(UIVariableUtil.getI18NProperty(col.getTitle()));
						htmlAttrsSB.append(":");
						htmlAttrsSB.append(value.toString());
						htmlAttrsSB.append(", ");
						htmlAttrsSB.append("</td>");
					} else {
						Object value = col.getRowExpression().getExpression().evaluate(
								ooeeContext);
						if (value == null) {
							value = "";
						}
						attrsSB.append(UIVariableUtil.getI18NProperty(col.getTitle()));
						attrsSB.append(":");
						attrsSB.append(value.toString());
						attrsSB.append(", ");
					}
				}
				attrsSB.append("</td>");
				context.generateHTML(attrsSB.toString());
				context.generateHTML(htmlAttrsSB.toString());
			} else {
				for (UITableColumnType col : columns) {
					HTMLUtil.generateTab(context, depth + 3);
					Object value = col.getRowExpression().getExpression().evaluate(
							ooeeContext);
					if (value == null) {
						value = "";
					}
					
					context.generateHTML("<td title=\"");
					if ("HTML".equals(col.getUiType().getType())
							|| "HTMLItem".equals(col.getUiType().getType())) {
					} else {
						context.generateHTML(value.toString());
					}
					context.generateHTML("\">");
					context.generateHTML(value.toString());
					context.generateHTML("</td>");
				}
			}
			HTMLUtil.generateTab(context, depth + 3);
			context.generateHTML("</tr>");
			
			count++;
		}
	}

	private void generateBigItemBody(HTMLSnapshotContext context, int depth,
			List<UITableColumnType> columns, List<Object> listData)
			throws EvaluationException {
		UITableColumnType htmlCol = null;
		for (UITableColumnType col : columns) {
			if ("HTMLItem".equalsIgnoreCase(col.getUiType().getType())) {
				htmlCol = col;
				break;
			}
		}
		if (htmlCol == null) {
			throw new IllegalArgumentException("Please specify a HTMLItem column for this table "+this.getId());
		}
		int count = 4;
		context.generateHTML("<tr>");
		HTMLUtil.generateTab(context, depth + 2);
		context.generateHTML("<td></td>");
		for (int i=0; i<listData.size();i++) {
			OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
			DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
			evaContext.setVariableValue("rowBE", listData.get(i));
			evaContext.setVariableValue("index", count);
			ooeeContext.setDefaultEvaluationContext(evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
			
			ExpressionType rowFilter = (ExpressionType)this.getAttribute("rowFilterExpr");
			boolean pass = (boolean)rowFilter.evaluate(ooeeContext);
			if (!pass) {
				continue;
			}
			
			HTMLUtil.generateTab(context, depth + 3);
			Object value = htmlCol.getRowExpression().getExpression().evaluate(ooeeContext);
			if (value == null) {
				value = "";
			}

			context.generateHTML("<td>");
			context.generateHTML(value.toString());
			context.generateHTML("</td>");
			if (--count == 0 && i<listData.size()) {
				HTMLUtil.generateTab(context, depth + 2);
				context.generateHTML("</tr>");
				HTMLUtil.generateTab(context, depth + 2);
				context.generateHTML("<tr>");
				HTMLUtil.generateTab(context, depth + 2);
				context.generateHTML("<td></td>");
				count = 4;
			}
		}
		if (listData.size() % 4 != 0) {
			int diff = 4 - listData.size();
			if (diff > 0) {
				while (diff-- > 0) {
					context.generateHTML("<td></td>");
				}
			} else {
				diff = 4 - (listData.size() % 4);
				while (diff-- > 0) {
					context.generateHTML("<td></td>");
				}
			}
			HTMLUtil.generateTab(context, depth + 2);
			context.generateHTML("</tr>");
		}
	}
	
	public void generateEndHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth) {
		try {
			super.generateEndHTML(context, ownerEntity, depth);
		} catch (Exception e) {
			logger.error("error. in entity: " + getUIEntityName(), e);
		}
	}

	public void generateAttribute(HTMLSnapshotContext context,
			String attributeName, Object attributeValue) throws IOException {
	}

	@Override
	public boolean isEditPermissionEnabled() {
		return true;
	}

	public Widget createAjaxWidget(VariableEvaluator ee)
    {
        Table t = new Table(getName(), Layout.NULL);

        t.setReadOnly(getReadOnly());
        t.setUIEntityName(getUIEntityName());

		try {
			EvaluationContext expressionContext = ee.getExpressionContext(ODContext.LOCAL_TAG);
			expressionContext.setVariableValue("table", t);
			
			Object stats = this.removeAttribute("statistic");
			if (stats != null) {
				this.hasStatistic=true;
				t.setStatistic((UITableStatsType)stats);
			} else {
				this.hasStatistic=false;
			}
			ExpressionType initQueryExpr = (ExpressionType)this.removeAttribute("initQueryExpr");
			ExpressionType queryExpr = (ExpressionType)this.removeAttribute("queryExpr");
			ExpressionType totalExpr = (ExpressionType)this.removeAttribute("totalExpr");
			Object result;
			if (initQueryExpr != null) {
				result = ee.evaluateExpression(initQueryExpr);
			} else {
				result = ee.evaluateExpression(queryExpr);
			}
			Object totalCount = ee.evaluateExpression(totalExpr);
			this.addAttribute("query", result);
			this.addAttribute("totalCount", totalCount);
			
			t.setListData((List)result);
			t.setConditions((TableConditions)expressionContext.getVariableValue("tableCondition"));
			t.setColumns((List)this.getAttribute("columns"));
			t.setSelectMode((UITableSelectModeType)this.getAttribute("selectMode"));
			t.setQueryExpr(queryExpr);
			t.setTotalExpr(totalExpr);
		} catch (EvaluationException e) {
			throw new IllegalStateException(e);
		}
        
        t.setListened(true);
        t.setFrameInfo(getFrameInfo());

        return t;
    }
	
}
