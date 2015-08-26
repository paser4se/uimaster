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
import org.shaolin.bmdp.datamodel.page.UITableColumnType;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.uimaster.page.HTMLSnapshotContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.ajax.Chart;
import org.shaolin.uimaster.page.ajax.Layout;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.UIVariableUtil;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HTMLChartSuper extends HTMLWidgetType
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public HTMLChartSuper()
    {
    }

    public HTMLChartSuper(HTMLSnapshotContext context)
    {
        super(context);
    }

    public HTMLChartSuper(HTMLSnapshotContext context, String id)
    {
        super(context, id);
    }

    @Override
	public void generateBeginHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth) {
    	generateWidget(context);
    	String root = WebConfig.getResourceContextRoot();
    	context.generateHTML("<script type=\"text/javascript\" src=\""+root+"/js/uimaster-chart.js\"></script>\n");
    	
    	Object widthProp = this.removeAttribute("width");
    	Object heightProp = this.removeAttribute("height");
    	int width = widthProp == null? 100: Integer.valueOf(widthProp.toString());
    	int height = heightProp == null? 100: Integer.valueOf(heightProp.toString());
    	context.generateHTML("<canvas id=\"");
		context.generateHTML(getName());
		context.generateHTML("\" width=\""+width+"px;\" width=\""+height+"px;\">");
		context.generateHTML("{type: '" + this.getClass().getSimpleName() + "',");
		context.generateHTML("labels:[");
		StringBuffer sb = new StringBuffer();
		List<UITableColumnType> columns = (List<UITableColumnType>)this.removeAttribute("columns");
		for (UITableColumnType col : columns) {
			sb.append("'");
			sb.append(UIVariableUtil.getI18NProperty(col.getTitle()));
			sb.append("',");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length()-1);
			context.generateHTML(sb.toString());
		}
		context.generateHTML("],");
		try {
			generateData(columns, context, depth + 2);
		} catch (Exception e) {
			logger.error("Failed to generate chart: " + e.getMessage(), e);
		}
		context.generateHTML("}");
		context.generateHTML("</canvas>");
	}
    
    public abstract void generateData(List<UITableColumnType> columns, HTMLSnapshotContext context, int depth) throws Exception;
    
    @Override
    public void generateEndHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth)
    {
    }

	public Widget createAjaxWidget(VariableEvaluator ee) {
		try {
			ExpressionType queryExpr = (ExpressionType) this.removeAttribute("queryExpr");
			Object result = ee.evaluateExpression(queryExpr);
			this.addAttribute("query", result);
			Chart chart = new Chart(getName(), Layout.NULL, this.getClass());

			chart.setReadOnly(getReadOnly());
			chart.setUIEntityName(getUIEntityName());

			chart.setListened(true);
			chart.setFrameInfo(getFrameInfo());

			return chart;
		} catch (EvaluationException e) {
			throw new IllegalStateException(e);
		}
	}

    
    private static final long serialVersionUID = -5232602952223828765L;
}
