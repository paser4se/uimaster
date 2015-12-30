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

import org.shaolin.uimaster.page.HTMLSnapshotContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLMapType extends HTMLWidgetType 
{
    private static final Logger logger = LoggerFactory.getLogger(HTMLMapType.class);

    public HTMLMapType()
    {
    }

    public HTMLMapType(HTMLSnapshotContext context)
    {
        super(context);
    }

    public HTMLMapType(HTMLSnapshotContext context, String id)
    {
        super(context, id);
    }

    @Override
	public void generateBeginHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth) {
		
	}
    
    @Override
    public void generateEndHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth)
    {
        try
        {
            generateWidget(context);
            
            HTMLUtil.generateTab(context, depth);
            
            // the entrance of baidu map is shit! we have to define this link in html head.
//            <node name="commonjs">
//	            <node name="org.shaolin.vogerp.commonmodel.page.CustomerManagement">
//	                <item name="baidumap" value="http://api.map.baidu.com/api?v=2.0&amp;ak=kT8czcw0fydHdXiGPGBOckX1" />
//	            </node>
//	        </node>
            
            context.generateHTML("<div class=\"uimaster_map\">");
            context.generateHTML("<div id=\"");
            context.generateHTML(getName());
            context.generateHTML("\" style=\"border: 1px solid gray; overflow:hidden;\"");
            generateAttributes(context);
            context.generateHTML("></div>");
            context.generateHTML("<div id=\"");
            context.generateHTML(getName());
            context.generateHTML("Result\"></div></div>");
            
            generateEndWidget(context);
        }
        catch (Exception e)
        {
            logger.error("error. in entity: " + getUIEntityName(), e);
        }
    }

    @Override
    public Widget createAjaxWidget(VariableEvaluator ee)
    {
    	return null;
    }

}
