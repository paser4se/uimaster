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
import java.util.List;

import org.shaolin.bmdp.datamodel.page.UITableColumnType;
import org.shaolin.uimaster.page.HTMLSnapshotContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.UIVariableUtil;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLChartSuper extends HTMLWidgetType
{
    private static Logger logger = LoggerFactory.getLogger(HTMLChartSuper.class);

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
    	
    	List<UITableColumnType> columns = (List<UITableColumnType>)this.removeAttribute("columns");
    	context.generateHTML("<div id=\"");
		context.generateHTML(getName());
		context.generateHTML("\" style=\"display:none\">");
		for (UITableColumnType col : columns) {
			HTMLUtil.generateTab(context, depth + 3);
			context.generateHTML("<div id=\"");
			context.generateHTML(col.getBeFieldId());
			context.generateHTML("\" htmlType=\"");
			context.generateHTML(col.getUiType().getType());
			context.generateHTML("\" title=\"");
			context.generateHTML(UIVariableUtil.getI18NProperty(col.getTitle()));
			context.generateHTML("\">");
			context.generateHTML(UIVariableUtil.getI18NProperty(col.getTitle()));
			context.generateHTML("</div>");
		}
		context.generateHTML("</div>");
	}
    
    @Override
    public void generateEndHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth)
    {
    }

    private void generateContent(HTMLSnapshotContext context)
    {
    }

    public void generateAttribute(HTMLSnapshotContext context, String attributeName, Object attributeValue) throws IOException
    {
    }

    public Widget createAjaxWidget(VariableEvaluator ee)
    {
        return null;
    }

    
    private static final long serialVersionUID = -5232602952223828765L;
}
