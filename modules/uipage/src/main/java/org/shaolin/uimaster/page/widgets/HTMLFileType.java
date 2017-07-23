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

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.ajax.AFile;
import org.shaolin.uimaster.page.ajax.Layout;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLFileType extends HTMLTextWidgetType 
{
    private static final Logger logger = LoggerFactory.getLogger(HTMLFileType.class);

    public HTMLFileType(String id)
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
            String root = WebConfig.getWebRoot();
            context.generateHTML("<script type=\"text/javascript\" src=\""+root+"/js/jquery-form.js\"></script>");
            HTMLUtil.generateTab(context, depth + 2);
            context.generateHTML("<input type=\"file\" name=\"");
            context.generateHTML(getName());
            context.generateHTML("\"");
            generateAttributes(context);
            generateEventListeners(context);
            context.generateHTML(" value=\"\" ");
            if (this.getAttribute("isMultiple") != null && 
            		("true".equals(this.getAttribute("isMultiple")) || Boolean.TRUE == this.getAttribute("isMultiple"))) {
            	context.generateHTML("multiple=\"multiple\" ");
            }
        	context.generateHTML("suffix=\"");
        	context.generateHTML(this.getAttribute("suffix").toString());
        	context.generateHTML("\" ");
            context.generateHTML("/ class=\"uimaster_button\">");
            HTMLUtil.generateTab(context, depth + 2);
            context.generateHTML("<div class=\"uimaster_file_hints\">");
            context.generateHTML((String)this.getAttribute("text"));
            context.generateHTML("</div>");
            HTMLUtil.generateTab(context, depth + 2);
            context.generateHTML("<div class=\"uimaster_action_bar\">");
            context.generateHTML("<input type=\"button\" value=\"\u4E0A\u4F20\" id=\"upload\" class=\"uimaster_button\"/>");
            context.generateHTML("<input type=\"button\" value=\"\u6E05\u7A7A\u6240\u6709\u6587\u4EF6\" id=\"cleanupload\" class=\"uimaster_button\"/>");
            context.generateHTML("<input type=\"button\" value=\"\u5728\u7EBF\u641C\u7D22\u56FE\u7247\" id=\"onlinesearch\" class=\"uimaster_button\"/></div>");
            HTMLUtil.generateTab(context, depth + 2);
            context.generateHTML("<div name=\"progressbox\" style=\"display:none;\">");
            context.generateHTML("<div name=\"progressbar\"></div><div name=\"percent\">0%</div></div>");
            context.generateHTML("<div name=\"message\"></div>");
            HTMLUtil.generateTab(context, depth);
            generateEndWidget(context);
        }
        catch (Exception e)
        {
            logger.error("error. in entity: " + getUIEntityName(), e);
        }
    }
    
    /**
     *  Whether this component can have editPermission.
     */
    @Override
    public boolean isEditPermissionEnabled()
    {
        return false;
    }
    
    public JSONObject createJsonModel(VariableEvaluator ee) throws JSONException 
    {
        AFile file = new AFile(getName(), Layout.NULL);

        file.setReadOnly(isReadOnly());
        file.setUIEntityName(getUIEntityName());
        if (this.getAttribute("storedPath") == null) {
        	throw new IllegalArgumentException("The file stored path can't be empty!");
        }
        file.setStoredPath(this.getAttribute("storedPath").toString());
        file.setSuffix(this.getAttribute("suffix").toString());
        if (this.getAttribute("allowedNumbers") != null) {
        	file.setAllowedNumbers(Integer.parseInt(this.removeAttribute("allowedNumbers").toString()));
        }
        if (this.getAttribute("contentSize") != null) {
        	file.setContentSize((Integer)this.removeAttribute("contentSize"));
        }
        if (this.getAttribute("photoWidth") != null) {
        	Object a = this.removeAttribute("photoWidth");
        	if (a instanceof String) {
        		file.setWidth(Integer.valueOf((String)a));
        	} else if (a instanceof Integer) {
        		file.setWidth((Integer)a);
        	} 
        }
        if (this.getAttribute("photoHeight") != null) {
        	Object a = this.removeAttribute("photoHeight");
        	if (a instanceof String) {
        		file.setHeight(Integer.valueOf((String)a));
        	} else if (a instanceof Integer) {
        		file.setHeight((Integer)a);
        	} 
        }
        if (this.getAttribute("refreshExpr") != null) {
        	file.setRefreshExpr((ExpressionType)this.removeAttribute("refreshExpr"));
        }
        
        file.setListened(true);

    	return file.toJSON();
    }

}
