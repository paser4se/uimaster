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

import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLMapType extends HTMLWidgetType 
{
    private static final Logger logger = LoggerFactory.getLogger(HTMLMapType.class);

    public HTMLMapType(String id)
    {
        super(id);
    }

    @Override
	public void generateBeginHTML(UserRequestContext context, UIFormObject ownerEntity, int depth) {
		
	}
    
    @Override
    public void generateEndHTML(UserRequestContext context, UIFormObject ownerEntity, int depth)
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
            if (context.getRequest().getAttribute("_hasWorldMap") == null) {
				context.getRequest().setAttribute("_hasWorldMap", Boolean.TRUE);
	            String root = (UserContext.isMobileRequest() && UserContext.isAppClient()) 
	        			? WebConfig.getAppContextRoot(context.getRequest()) : WebConfig.getResourceContextRoot();
	        	//context.generateHTML("<script type=\"text/javascript\" src=\""+root+"/js/controls/map/raphael-min.js\"></script>");
	        	//context.generateHTML("<script type=\"text/javascript\" src=\""+root+"/js/controls/map/chinaMapConfig.js\"></script>");
	        	//context.generateHTML("<script type=\"text/javascript\" src=\""+root+"/js/controls/map/map.js\"></script>");
            }
            
            String data = (String)this.removeAttribute("value");
            String event = this.getEventListener("onclick");
            if (event != null && event.trim().length() > 0) {
            	event = getReconfigurateFunction(event);
            }
            context.generateHTML("<div id=\"");
            context.generateHTML(getName());
            context.generateHTML("\" class=\"uimaster_map\" event=\""+event+"\"");
            generateAttributes(context);
            context.generateHTML("><div style=\"display:none;\">"+data+"</div><div id=\"");
            context.generateHTML(getName());
            context.generateHTML("List\" class=\"uimster_mapInfo\"><ul class=\"list1\"></ul><ul class=\"list2\"></ul><ul class=\"list3\"></ul></div>");
            context.generateHTML("<div id=\"");
            context.generateHTML(getName());
            context.generateHTML("Map\" class=\"uimster_map_map\"></div>");
            context.generateHTML("<div id=\"");
            context.generateHTML(getName());
            context.generateHTML("Color\" class=\"uimster_map_color\"></div>");
            
            context.generateHTML("</div>");
            generateEndWidget(context);
        }
        catch (Exception e)
        {
            logger.error("error. in entity: " + getUIEntityName(), e);
        }
    }
    
    @Override
    public JSONObject createJsonModel(VariableEvaluator ee) throws JSONException 
    {
//    	Map matrix = new Map(getName(), Layout.NULL);
//    	matrix.setUIEntityName(getUIEntityName());
//    	matrix.setListened(true);
		return super.createJsonModel(ee);
    }

}
