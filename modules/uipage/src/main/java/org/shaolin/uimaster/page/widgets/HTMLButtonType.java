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
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLButtonType extends HTMLTextWidgetType 
{
    private static final Logger logger = LoggerFactory.getLogger(HTMLButtonType.class);

    private String buttonType;

    public HTMLButtonType(String id)
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
        	if (this.getAttribute("jsscript") != null) {
        		HTMLUtil.generateTab(context, depth);
                context.generateHTML(this.removeAttribute("jsscript").toString());
        	}
            generateWidget(context);
            context.generateHTML("<input type=\"");
            context.generateHTML(getButtonType());
            context.generateHTML("\" name=\"");
            context.generateHTML(getName());
            context.generateHTML("\"");
            generateAttributes(context);
            context.generateHTML(" value=\"");
            if (context.isValueMask())
            {
                context.generateHTML(WebConfig.getHiddenValueMask());
            }
            else
            {
                context.generateHTML(HTMLUtil.formatHtmlValue(getValue()));
            }
            context.generateHTML("\"");
            if (isReadOnly() != null && isReadOnly().booleanValue())
            {
                context.generateHTML(" disabled=\"true\"");
            }
            else 
            {
            	generateEventListeners(context);
            }
            context.generateHTML(" data-inline=\"true\"/>");
            generateEndWidget(context);
        }
        catch (Exception e)
        {
            logger.error("error. in entity: " + getUIEntityName(), e);
        }
    }

    private String getButtonType()
    {
        if ( buttonType == null )
        {
            buttonType = (String)removeAttribute("buttonType");
            if ( buttonType == null || ( !buttonType.trim().equals("reset") && !buttonType.trim().equals("submit") ) )
            {
                buttonType = "button";
            }
        }

        return buttonType;
    }
    
    public JSONObject createJsonModel(VariableEvaluator ee) throws JSONException
    {
	    	if (needAjaxSupport()) {
	        	return super.createJsonModel(ee);
	    	}
	    return null;
    }

    public JSONObject createJsonEventSource() throws JSONException {
    		return super.createJsonModel(null);
    }
    
}
