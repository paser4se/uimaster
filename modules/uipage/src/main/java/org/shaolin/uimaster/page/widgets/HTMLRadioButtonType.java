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

public class HTMLRadioButtonType extends HTMLSelectComponentType
{
    private static Logger logger = LoggerFactory.getLogger(HTMLRadioButtonType.class);

    public HTMLRadioButtonType(String id)
    {
        super(id);
    }

    private String _getName2()
    {
    	
        String prefix = UserRequestContext.UserContext.get().getHTMLPrefix();
        if (prefix == null) 
        	prefix = "";
        return prefix + getId();
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
            context.generateHTML("<input type=radio name=\"");
            context.generateHTML(_getName2());
            context.generateHTML("\"");
            context.generateHTML(" id=\"");
            context.generateHTML(_getName2());
            context.generateHTML("\"");
            generateAttributes(context);
            generateEventListeners(context);
            if (isReadOnly() != null && isReadOnly().booleanValue())
            {
                context.generateHTML(" disabled=\"true\"");
            }
            context.generateHTML(" />");
            if (!this.isVisible())
                context.generateHTML("<span style=\"display:none\">");
            context.generateHTML("<label for=\"");
            context.generateHTML(_getName2());
            context.generateHTML("\">");
            if (context.isValueMask())
            {
                context.generateHTML(WebConfig.getHiddenValueMask());
            }
            else
            {
                context.generateHTML(HTMLUtil.htmlEncode(getLabel()));
            }
            context.generateHTML("</label>");
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

    public JSONObject createJsonModel(VariableEvaluator ee) throws JSONException 
    {
//        return super.createJsonModel(ee);
    		return null;
    }
    
    private static final long serialVersionUID = -4405215152580918889L;
}
