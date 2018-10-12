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
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLTextFieldType extends HTMLTextWidgetType
{
    private static Logger logger = LoggerFactory.getLogger(HTMLTextFieldType.class);

    public HTMLTextFieldType(String id)
    {
        super(id);
    }

    @Override
	public void generateBeginHTML(UserRequestContext context, UIFormObject ownerEntity, int depth) {
		
	}
    
    @Override
    public void generateEndHTML(UserRequestContext context, UIFormObject ownerEntity, int depth)
    {
        generateWidget(context);
        String currencySymbol = getCurrencySymbol();
        if ( currencySymbol == null || currencySymbol.equals("") )
        {
            generateContent(context);
        }
        else if ( getIsSymbolLeft() )
        {
            generateCurrencySymbol(context, currencySymbol);
            generateContent(context);
        }
        else
        {
            generateContent(context);
            generateCurrencySymbol(context, currencySymbol);
        }
        generateEndWidget(context);
    }

    public void generateCurrencySymbol(UserRequestContext context, String currencySymbol)
    {
        context.generateHTML("<span class=\"currencySymbolText\"");
        context.generateHTML(" id=\"" + getName() + "_currencySymbol\" >");
        context.generateHTML(currencySymbol);
        context.generateHTML("</span>");
    }

    private void generateContent(UserRequestContext context)
    {
        try
        {
            if ( isReadOnly() != null && isReadOnly().booleanValue() )
            {
                addAttribute("allowBlank", "true");
                addAttribute("readOnly", "true");
            }
            
            if (this.getAttribute("isNumber") != null) {
            	context.generateHTML("<input type=\"number\" name=\"");
            } else if (this.getAttribute("isEmail") != null) {
            	context.generateHTML("<input type=\"email\" name=\"");
            } else {
            	context.generateHTML("<input type=\"text\" name=\"");
            }
            context.generateHTML(getName());
            context.generateHTML("\"");
            generateAttributes(context);
            generateEventListeners(context);
            context.generateHTML(" value=\"");
            if (context.isValueMask())
            {
                context.generateHTML(WebConfig.getHiddenValueMask());
            }
            else
            {
                context.generateHTML(HTMLUtil.formatHtmlValue(getValue()));
            }
            context.generateHTML("\" />");
            if (this.getAttribute("needAmount") != null && 
            		"true".equals(this.getAttribute("needAmount").toString())) {
            	context.generateHTML("<span class=\"uimaster-amount-btn\">");
            	context.generateHTML("<span class=\"uimaster-amount-increase\">+</span>");
            	context.generateHTML("<span class=\"uimaster-amount-decrease\">-</span>");
            	context.generateHTML("</span>");
            }
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

    public JSONObject createJsonModel(VariableEvaluator ee) throws JSONException
    {
//        return super.createJsonModel(ee);
    		return null;
    }

    
    private static final long serialVersionUID = -5232602952223828765L;
}
