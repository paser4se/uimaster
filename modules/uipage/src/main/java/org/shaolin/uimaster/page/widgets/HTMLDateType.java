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

import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLDateType extends HTMLTextWidgetType
{
    private static Logger logger = LoggerFactory.getLogger(HTMLDateType.class);

    private static final long serialVersionUID = -5232602952223828765L;
    
    private boolean isRange = false;

    public HTMLDateType(String id)
    {
        super(id);
    }

    public boolean isRange() {
		return isRange;
	}

	public void setRange(boolean isRange) {
		this.isRange = isRange;
	}
    
    @Override
	public void generateBeginHTML(UserRequestContext context, UIFormObject ownerEntity, int depth) {
		
	}
    
    @Override
    public void generateEndHTML(UserRequestContext context, UIFormObject ownerEntity, int depth)
    {
        generateWidget(context);
        generateContent(context);
        generateEndWidget(context);
    }

    public String getValue()
    {
        String value = (String)getAttribute("value");
        if (value == null)
        {
            value = (String)getAttribute("text");
        }
        return value == null ? "" : value;
    }

    private void generateContent(UserRequestContext context)
    {
        try
        {
        	boolean isReadOnly = isReadOnly() != null && isReadOnly().booleanValue();
			if (isReadOnly) {
				addAttribute("allowBlank", "true");
				addAttribute("readOnly", "true");
			}
			if (isReadOnly || !isEditable()) {
				context.generateHTML("<input type=\"input\" name=\"");
			} else if (this.getAttribute("isDataOnly") != null && "true".equals(this.getAttribute("isDataOnly").toString())) {
				context.generateHTML("<input type=\"date\" name=\"");
			} else {
				context.generateHTML("<input type=\"datetime-local\" name=\"");
			}
            context.generateHTML(getName());
            context.generateHTML("\"");
            generateAttributes(context);
            generateEventListeners(context);
            context.generateHTML(" value=\"");
            context.generateHTML(HTMLUtil.formatHtmlValue(getValue()));
            context.generateHTML("\" />");
            
            /** use html5 instead.
            if (!isReadOnly && isEditable()) {
	            String root = (UserContext.isMobileRequest() && UserContext.isAppClient()) 
	        			? WebConfig.getAppResourceContextRoot() : WebConfig.getResourceContextRoot();
	            if (isRange) {
	            	context.generateHTML("<img src='");
					context.generateHTML(root + "/images/controls/calendar/selectdate.gif");
					context.generateHTML("' />");
	            } else {
		            context.generateHTML("&nbsp;&nbsp;<img src='");
					context.generateHTML(root + "/images/controls/calendar/selectdate.gif");
					context.generateHTML("' onclick='javascript:defaultname.");
					context.generateHTML(this.getPrefix() + this.getUIID());
					context.generateHTML(".open();'/>");
	            }
            }
            */
        }
        catch (Exception e)
        {
            logger.error("error. in entity: " + getUIEntityName(), e);
        }
    }

    public void generateAttribute(UserRequestContext context, String attributeName, Object attributeValue) throws IOException
    {
        if ("initValidation".equals(attributeName) || "validator".equals(attributeName))
        {
        	return;
        }
        if ("editable".equals(attributeName))
        {
            if ("false".equals(String.valueOf(attributeValue)))
            {
                context.generateHTML(" readOnly=\"true\"");
            }
        }
        else if ("maxLength".equals(attributeName))
        {
            context.generateHTML(" maxlength=\"");
            context.generateHTML((String)attributeValue);
            context.generateHTML("\"");
        }
        else if ("txtFieldLength".equals(attributeName))
        {
            context.generateHTML(" size=\"");
            context.generateHTML((String)attributeValue);
            context.generateHTML("\"");
        }
        else if ("prompt".equals(attributeName))
        {
            if ( attributeValue != null && !((String)attributeValue).trim().equals("") )
            {
                context.generateHTML(" title=\"");
                context.generateHTML((String)attributeValue);
                context.generateHTML("\"");
            }
        }
        else
        {
            super.generateAttribute(context, attributeName, attributeValue);
        }
    }

    public JSONObject createJsonModel(VariableEvaluator ee) throws JSONException {
        return null; //super.createJsonModel(ee);
    }

}
