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

import java.util.List;

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.datamodel.page.ExpressionPropertyType;
import org.shaolin.bmdp.datamodel.page.UITableColumnType;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.EvaluationContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.uimaster.page.DisposableBfString;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.ajax.Chart;
import org.shaolin.uimaster.page.ajax.Layout;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.UIVariableUtil;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.shaolin.uimaster.page.od.ODContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HTMLChartSuper extends HTMLWidgetType
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public HTMLChartSuper(String id)
    {
        super(id);
    }

    @Override
	public void generateBeginHTML(UserRequestContext context, UIFormObject ownerEntity, int depth) {
    	if (context.getRequest().getAttribute("_haschats") == null) {
			context.getRequest().setAttribute("_haschats", Boolean.TRUE);
            String root = (UserContext.isMobileRequest() && UserContext.isAppClient()) 
        			? WebConfig.getAppContextRoot(context.getRequest()) : WebConfig.getResourceContextRoot();
        	context.generateHTML("<script type=\"text/javascript\" src=\""+root+"/js/uimaster-chart.js\"></script>");
        }
    	generateWidget(context);
    	Object widthProp = this.removeAttribute("width");
    	Object heightProp = this.removeAttribute("height");
    	String width = widthProp == null? "100%": widthProp.toString();
    	String height = heightProp == null? "100%": heightProp.toString();
    	context.generateHTML("<canvas id=\"");
		context.generateHTML(getName());
		context.generateHTML("\" width=\""+width+"\" height=\""+height+"\" title=\""+this.removeAttribute("title")+"\">");
		context.generateHTML("{type: '" + this.getClass().getSimpleName() + "',");
		try {
			context.generateHTML("data: {labels:");
			List<UITableColumnType> columns = (List<UITableColumnType>)this.removeAttribute("columns");
			if (this.getAttribute("labels") != null) {
				context.generateHTML(this.getAttribute("labels").toString());
				context.generateHTML(",");
			} else {
				StringBuilder sb = DisposableBfString.getBuffer();
				try {
					sb.append("[");
					Object data = this.getAttribute("query");
					OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
					DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
					if (data != null && data instanceof List && ((List)data).size() > 0) {
						List<Object> listData = (List<Object>)data;
						evaContext.setVariableValue("rowBE", listData.get(0));
					}
					ooeeContext.setDefaultEvaluationContext(evaContext);
					ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
					for (UITableColumnType col : columns) {
						ExpressionPropertyType isVisibleExpr = col.getIsVisible();
						if (isVisibleExpr != null) {
							// it does require the data from the first row.
							Boolean isVisible = (Boolean)isVisibleExpr.getExpression().evaluate(ooeeContext);
							if (!isVisible.booleanValue()) {
								continue;
							}
						}
						sb.append("'");
						sb.append(UIVariableUtil.getI18NProperty(col.getTitle()));
						sb.append("',");
					}
					if (sb.length() > 0) {
						sb.deleteCharAt(sb.length()-1);
					}
					sb.append("],");
					context.generateHTML(sb.toString());
				} finally {
					DisposableBfString.release(sb);
				}
			}
			
			generateData(columns, context, depth + 2);
		} catch (Exception e) {
			logger.error("Failed to generate chart: " + e.getMessage(), e);
		}
		context.generateHTML("}");
		if (this.getClass() == HTMLChartRadarType.class) {
			context.generateHTML(",options: {scale: {beginAtZero: true,reverse: false}}");
		} else if (this.getClass() == HTMLChartPieType.class) {
			context.generateHTML(",options: {responsive: true, maintainAspectRatio: true}");
		} else if (this.getClass() == HTMLChartBarType.class) {
			context.generateHTML(",options: {responsive: true}");
		} else if (this.getClass() == HTMLChartDoughnutType.class
				|| this.getClass() == HTMLChartPolarPieType.class) {
			context.generateHTML(",options: {responsive: true}");
		} else if (this.getClass() == HTMLChartLinearType.class) {
			if (this.getAttribute("dataType") != null) {
				if("date".equalsIgnoreCase((String)this.getAttribute("dataType"))) {
					context.generateHTML(",options: {responsive: true,scales: {xAxes:[{type:\"time\",display:true,time:{format:'YYYY-MM-DD'},scaleLabel:{show:true,labelString:'Date'}}],");
					context.generateHTML("yAxes:[{display:true,scaleLabel: {show: true,labelString:'Values'}}]},elements:{line:{tension: 0.3}}}");
				} else {
					context.generateHTML(",options: {responsive: true,scales: {xAxes:[{display:true,scaleLabel:{show:true,labelString:''}}],");
					context.generateHTML("yAxes:[{display:true,scaleLabel: {show: true,labelString:''}}]},elements:{line:{tension: 0.3}}}");
				}
			} else {
				context.generateHTML(",options: {responsive: true,scales: {xAxes:[{display:true,scaleLabel:{show:true,labelString:''}}],");
				context.generateHTML("yAxes:[{display:true,scaleLabel: {show: true,labelString:''}}]},elements:{line:{tension: 0.3}}}");
			}
		}
		context.generateHTML("}");
		context.generateHTML("</canvas>");
	}
    
    public abstract void generateData(List<UITableColumnType> columns, UserRequestContext context, int depth) throws Exception;
    
    @Override
    public void generateEndHTML(UserRequestContext context, UIFormObject ownerEntity, int depth)
    {
    }

	public Widget createAjaxWidget(VariableEvaluator ee) {
		try {
			EvaluationContext expressionContext = ee.getExpressionContext(ODContext.LOCAL_TAG);
			expressionContext.setVariableValue("condition", null);
			ExpressionType queryExpr = (ExpressionType) this.removeAttribute("queryExpr");
			Object result = ee.evaluateExpression(queryExpr);
			ExpressionType labelExpr = (ExpressionType) this.removeAttribute("labelExpr");
			if (labelExpr != null) {
				Object labels = ee.evaluateExpression(labelExpr);
				this.addAttribute("labels", labels);
			}
			this.addAttribute("query", result);
			Chart chart = new Chart(getName(), Layout.NULL, this.getClass());
			chart.setColumns((List)this.getAttribute("columns"), queryExpr, expressionContext.getVariableValue("condition"));
			chart.setReadOnly(isReadOnly());
			chart.setUIEntityName(getUIEntityName());

			chart.setListened(true);

			return chart;
		} catch (EvaluationException e) {
			throw new IllegalStateException(e);
		}
	}

    
    private static final long serialVersionUID = -5232602952223828765L;
}
