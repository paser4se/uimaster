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

import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.ajax.Layout;
import org.shaolin.uimaster.page.ajax.Panel;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.shaolin.uimaster.page.od.mappings.DynamicUIComponentMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLPanelType extends HTMLContainerType
{
	private static final long serialVersionUID = 389498529972253646L;
	
    private static final Logger logger = LoggerFactory.getLogger(HTMLPanelType.class);

    public HTMLPanelType(String id)
    {
        super(id);
    }

    public void generateBeginHTML(UserRequestContext context, UIFormObject ownerEntity, int depth)
    {
        try
        {
            generateWidget(context);
            String title = (String)getAttribute("title");
            boolean hasDiv = "true".equals(getAttribute("hasDiv"));
            boolean hasErrorMessage = "true".equals(getAttribute("hasErrorMessage"));

            if (title != null && title.trim().length() > 0)
            {
                context.generateHTML("<fieldset><legend>");
                context.generateHTML(title);
                context.generateHTML("</legend>");
            }
            if (hasDiv)
            {
                context.generateHTML("<div id=\"");
                context.generateHTML(getName());
                context.generateHTML("\" class=\"panelSystemDiv\">");
            }
            else
            {
                addAttribute("id", getName());
            }

            super.generateBeginHTML(context, ownerEntity, depth);

            // add behind panel-div, and no need to wrap panel-div
            if (hasErrorMessage)
            {
                context.generateHTML("<div id=\"");
                context.generateHTML(getName());
                context.generateHTML("-warn-placeholder");
                context.generateHTML("\" class=\"err-hidden\">");
                context.generateHTML("</div>");
            }
            generateEndWidget(context);
        }
        catch (Exception e)
        {
            logger.error("error. in entity: " + getUIEntityName(), e);
        }
    }

    public boolean hasDynamicUI() {
    	return (this.getAttribute("dynamicUI") != null && "true".equals(this.getAttribute("dynamicUI")));
    }
    
    public void setDynamicItems(List<HTMLDynamicUIItem> items) {
    	this.addAttribute("dynamicItems", items);
    }
    
    public void generateEndHTML(UserRequestContext context, UIFormObject ownerEntity, int depth)
    {
        try
        {
        	List<HTMLDynamicUIItem> items = (List<HTMLDynamicUIItem>)this.getAttribute("dynamicItems");
        	if (this.hasDynamicUI() && items != null) {
        		String jsonValue = (String)context.getODMapperData().get(DynamicUIComponentMapping.JSON_VALUE);
        		
        		int i = 0;
				for (HTMLDynamicUIItem item : items) {
					
					String uiid = this.getId() + "-dynamicUI" + i++;
					HTMLUtil.generateTab(context, depth);
					context.generateHTML("<div id=\"div-"+uiid+"-left-cell\" class=\"uimaster_widget_cell w1 h1 uimaster_dynamicitem_leftform_cell\" >");
					HTMLUtil.generateTab(context, depth + 1);
					HTMLLabelType lable = new HTMLLabelType(uiid + "Lable");
					lable.addAttribute("UIStyle", "uimaster_label uimaster_leftform_widget");
					lable.setValue(item.getLabelName());
					lable.setHTMLLayout(this.getHTMLLayout());
					
					lable.generateBeginHTML(context, ownerEntity, depth);
					lable.generateEndHTML(context, ownerEntity, depth);
					
					HTMLUtil.generateTab(context, depth + 1);
					context.generateHTML("</div>");
					HTMLUtil.generateTab(context, depth);
					context.generateHTML("<div id=\"div-"+uiid+"-right-cell\" class=\"uimaster_widget_cell w1 h1 uimaster_dynamicitem_rightform_cell\" >");
					HTMLUtil.generateTab(context, depth + 1);
					
					item.generate(jsonValue, uiid, this.getHTMLLayout(), context, ownerEntity, depth);
					
					HTMLUtil.generateTab(context, depth + 1);
					context.generateHTML("</div>");
					HTMLUtil.generateTab(context, depth);
					context.generateHTML("<div class=\"hardreturn\"></div>");
				}
        		
        	}
            super.generateEndHTML(context, ownerEntity, depth);
            
            String title = (String)getAttribute("title");
            boolean hasDiv = "true".equals(getAttribute("hasDiv"));
            if (hasDiv)
            {
                context.generateHTML("</div>");
            }
            if (title != null)
            {
                context.generateHTML("</fieldset>");
            }
        }
        catch (Exception e)
        {
            logger.error("error. in entity: " + getUIEntityName(), e);
        }
    }

    public void generateAttribute(UserRequestContext context, String attributeName,
            Object attributeValue) throws IOException
    {
        if (!(attributeValue instanceof String))
            return;
        String attrValue = (String)attributeValue;
        if ("spacing".equals(attributeName) || "padding".equals(attributeName)
                || "hasDiv".equals(attributeName) || "title".equals(attributeName)
                || "hasErrorMessage".equals(attributeName) || "editable".equals(attributeName))
        {
        }
        else if ("background".equals(attributeName))
        {
            if (!"".equals(attrValue))
            {
                context.generateHTML(" background=\"");
                context.generateHTML(context.getImageUrl(getUIEntityName(), attrValue));
                context.generateHTML("\"");
            }
        }
        else if ("borderColor".equals(attributeName))
        {
            context.generateHTML(" borderColor=\"");
            if (!attrValue.startsWith("#"))
            {
                context.generateHTML("rgb(");
            }
            context.generateHTML(attrValue);
            if (!attrValue.startsWith("#"))
            {
                context.generateHTML(")");
            }
            context.generateHTML("\"");
        }
        else
        {
            super.generateAttribute(context, attributeName, attributeValue);
        }
    }

    public Widget<Panel> createAjaxWidget(VariableEvaluator ee)
    {
    	if (!needAjaxSupport()) {
    		return null;
    	}
    	
        Panel panel = new Panel(getName(), Layout.NULL);
        
        panel.setDivPrefix(UserRequestContext.UserContext.get().getDIVPrefix());
        
        panel.setReadOnly(isReadOnly());
        panel.setUIEntityName(getUIEntityName());
        panel.setListened(true);
        if (this.hasDynamicUI()) {
        	List<HTMLDynamicUIItem> items = (List<HTMLDynamicUIItem>)this.getAttribute("dynamicItems");
        	panel.setDynamicUI(items);
        }
        
        return panel;
    }
    
}
