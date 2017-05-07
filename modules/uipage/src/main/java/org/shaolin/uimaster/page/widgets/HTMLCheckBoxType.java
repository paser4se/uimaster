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

import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.ajax.CheckBox;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HTMLCheckBoxType extends HTMLSelectComponentType
{
    private static final Logger logger = LoggerFactory.getLogger(HTMLCheckBoxType.class);

    public HTMLCheckBoxType(String id)
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
            context.generateHTML("<input type=\"checkbox\" name=\"");
            context.generateHTML(getName());
            context.generateHTML("\"");
            context.generateHTML(" id=\"");
            context.generateHTML(getName());
            context.generateHTML("\"");
            generateAttributes(context);
            generateEventListeners(context);
            if (isReadOnly() != null && isReadOnly().booleanValue())
            {
                context.generateHTML(" disabled=\"true\"");
            }
            context.generateHTML(" />");
            if (!this.isVisible()) {
                context.generateHTML("<span style=\"display:none\">");
            }
            String linkevent = getEventListener("linkevent");
        	if (linkevent != null && linkevent.length() > 0) {
        		context.generateHTML("<a href=\"" + getReconfigurateFunction(linkevent));
        		context.generateHTML("\">");
        	} 
            if (context.isValueMask()) {
                context.generateHTML(WebConfig.getHiddenValueMask());
            } else {
				if (getLabel() != null && !"null".equals(getName())) {
					context.generateHTML(HTMLUtil.htmlEncode(getLabel()));
				}
            }
        	if (linkevent != null && linkevent.length() > 0) {
        		context.generateHTML("</a>");
        	} 
            if (!this.isVisible())
            {
                context.generateHTML("</span>");
            }
            generateEndWidget(context);
        }
        catch (Exception e)
        {
            logger.error("error. in entity: " + getUIEntityName(), e);
        }
    }

    public Widget createAjaxWidget(VariableEvaluator ee)
    {
        CheckBox checkBox = new CheckBox(getName(), null);

        checkBox.setReadOnly(isReadOnly());
        checkBox.setUIEntityName(getUIEntityName());

        checkBox.setLabel(getLabel());
        checkBox.setSelected(getValue());
        setAJAXConstraints(checkBox);
        setAJAXAttributes(UserRequestContext.UserContext.get(), checkBox);
        
        checkBox.setListened(true);

        return checkBox;
    }
    
    private static final long serialVersionUID = -6662073300811877694L;
}
