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
package org.shaolin.uimaster.page.ajax;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.shaolin.uimaster.page.AjaxActionHelper;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.ajax.json.IDataItem;

abstract public class TextWidget extends Widget<TextWidget> implements Serializable
{
    private static final long serialVersionUID = 7730729344287217301L;
    
    private String currencySymbol;
    
    private boolean isSysmbolLeft;
    
    public TextWidget(String id, Layout layout)
    {
        super(id, layout);
    }
    
    public TextWidget(String id, String value, Layout layout)
    {
        super(id, layout);
        this.setValue(value);
    }

    protected TextWidget generateAttribute(String name, Object value, StringBuilder sb)
    {
		if (!name.equals("value")) {
			return super.generateAttribute(name, value, sb);
		}
		return this;
    }

    public void setValue(String value)
    {
        if(this._isReadOnly())
        {
            return;
        }
        // disable it by default due to the server side update.
        addAttribute("value", value, false);
        
        if(!this.isListened())
        {
            return;
        }
        
        AjaxContext ajaxContext = AjaxActionHelper.getAjaxContext();
        if (ajaxContext == null || !ajaxContext.existElement(this))
        {
            return;
        }
        
        StringBuilder sb = new  StringBuilder();
        sb.append("{'name':'value','value':'");
        sb.append(HTMLUtil.handleEscape(String.valueOf(value)));
        sb.append("'}");
        IDataItem dataItem = AjaxActionHelper.updateAttrItem(this.getId(), sb.toString());
        dataItem.setFrameInfo(getFrameInfo());
        ajaxContext.addDataItem(dataItem);
    }
    
    public TextWidget checkConstraint() {
    	String value = (String)getAttribute("value");
    	if(this.hasConstraint("allowBlank")) {
    		boolean allowBlank = Boolean.valueOf(this.getConstraint("allowBlank").toString());
    		if (!allowBlank && value == null) {
    			throw new IllegalStateException("UI Constraint fails in: " 
    							+ this.getConstraint("allowBlankText") 
    							+ ",UIID: " + this.getId());
    		}
    	}
    	if(this.hasConstraint("minLength")) {
    		int minLength = Integer.valueOf(this.getConstraint("minLength").toString());
    		if (value != null && minLength > value.length()) {
    			throw new IllegalStateException("UI Constraint fails in: " 
    							+ this.getConstraint("minLengthText") 
    							+ ",UIID: " + this.getId());
    		}
    	}
    	if(this.hasConstraint("maxLength")) {
    		int maxLength = Integer.valueOf(this.getConstraint("maxLength").toString());
    		if (value != null && maxLength < value.length()) {
    			throw new IllegalStateException("UI Constraint fails in: " 
    							+ this.getConstraint("maxLengthText") 
    							+ ",UIID: " + this.getId());
    		}
    	}
    	if(this.hasConstraint("regex")) {
    		String regex = (String)this.getConstraint("regex");
    		Pattern pattern = Pattern.compile(regex);
    		Matcher matcher = pattern.matcher(value);
    		if (!matcher.matches()) {
    			throw new IllegalStateException("UI Constraint fails in: " 
    							+ this.getConstraint("regexText") 
    							+ ",UIID: " + this.getId());
    		}
    	}
    	return this;
    }

    public String getValue()
    {
    	checkConstraint();
        return (String)getAttribute("value");
    }

    public void setCurrencySymbol(String currencySymbol)
    {
        this.currencySymbol = currencySymbol;
        addAttribute("currencySymbol", currencySymbol);
    }

    public String getCurrencySymbol()
    {
        return currencySymbol;
    }

    public void setSymbolLeft(boolean isSysmbolLeft)
    {
        this.isSysmbolLeft = isSysmbolLeft;
        addAttribute("isSysmbolLeft", isSysmbolLeft);
    }

    public boolean isSymbolLeft()
    {
        return isSysmbolLeft;
    }
    
    protected void generateCurrencySymbol(StringBuilder sb)
    {
        sb.append("<span class=\"currencySymbol\"");
        sb.append(" id=\"" + getId() + "_currencySymbol\" >");
        sb.append(currencySymbol);
        sb.append("</span>");
    }
}
