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
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLHiddenType extends HTMLTextWidgetType
{
    private static final Logger logger = LoggerFactory.getLogger(HTMLHiddenType.class);

    public HTMLHiddenType(String id)
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
            context.generateHTML("<input type=hidden name=\"");
            context.generateHTML(getName());
            context.generateHTML("\"");
            generateAttributes(context);
            generateEventListeners(context);
            context.generateHTML(" value=\"");
            if (this.getAttribute("secure") == null || !Boolean.valueOf(this.getAttribute("secure").toString())) {
            	context.generateHTML(HTMLUtil.formatHtmlValue(getValue()));
            }
            context.generateHTML("\" />");
        }
        catch (Exception e)
        {
            logger.error("error. in entity: " + getUIEntityName(), e);
        }
    }

    public String getValue()
    {
        String value = (String)getAttribute("value");
        return value == null ? "" : value;
    }
    
    /**
     * Whether this component can have editPermission.
     */
    @Override
    public boolean isEditPermissionEnabled()
    {
        return false;
    }

    public JSONObject createJsonModel(VariableEvaluator ee) throws JSONException 
    {
    		if (getAttribute("secure") != null) {
    			this.addAttribute("needAjaxSupport", true);
    			return super.createJsonModel(ee);
    		}
    		return null;
    }
    
    private static final long serialVersionUID = 1875046878985040938L;
}
