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
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;

public abstract class HTMLTextWidgetType extends HTMLWidgetType
{
    public HTMLTextWidgetType(String id)
    {
        super(id);
    }

    public void generateAttribute(UserRequestContext context, String attributeName,
            Object attributeValue) throws IOException
    {
        if ("text".equals(attributeName) || "value".equals(attributeName))
        {
        }
        else
        {
            super.generateAttribute(context, attributeName, attributeValue);
        }
    }

    public String getValue()
    {
        Object value = getAttribute("value");
        if (value == null)
        {
            value = getAttribute("text");
        }
        return value == null ? "" : value.toString();
    }

    public void setValue(String value)
    {
        addAttribute("value", value);
    }

    public String getCurrencySymbol()
    {
        Object currencySymbol = getAttribute("currencySymbol");
        return currencySymbol == null ? "" : currencySymbol.toString();
    }

    public void setCurrencySymbol(String currencySymbol)
    {
        addAttribute("currencySymbol", currencySymbol);
    }

    public boolean getIsSymbolLeft()
    {
        String isLeft = (String)getAttribute("isLeft");

        return (isLeft == null || isLeft.equals("true")) ? true : false;
    }

    public void setIsSymbolLeft(Boolean isLeft)
    {
        if (isLeft == null)
        {
            isLeft = Boolean.TRUE;
        }
        addAttribute("isLeft", isLeft.toString());
    }
    
    public void setIsCurrency(boolean isCurrency)
    {
		addAttribute("isCurrency", (Boolean.valueOf(isCurrency)).toString());
    }
    
    public boolean getIsCurrency()
    {
    	String isCurrency = (String)getAttribute("isCurrency");
    	return (isCurrency != null && isCurrency.equalsIgnoreCase("true")) ? true : false;    	
    }

    public String getLocale()
    {
        String locale = (String)getAttribute("locale");

        return locale == null ? "" : locale;
    }

    public void setLocale(String locale)
    {
        addAttribute("locale", locale == null ? "" : locale);
    }

    public String getCurrencyFormat()
    {
        String locale = (String)getAttribute("currencyFormat");

        return locale == null ? "" : locale;
    }

    public void setCurrencyFormat(String format)
    {
        addAttribute("currencyFormat", format == null ? "" : format);
    }

    public JSONObject createJsonModel(VariableEvaluator ee) throws JSONException
    {
    		if (this.needAjaxSupport()) {
			return super.createJsonModel(ee);
		}
    		return null;
    }
    private static final long serialVersionUID = 1705497534002626203L;
}
