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

import java.lang.reflect.Constructor;

import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The customized ui widget.
 * 
 * @author wushaol
 *
 */
public class HTMLCustWidgetType extends HTMLWidgetType 
{
    private static final Logger logger = LoggerFactory.getLogger(HTMLCustWidgetType.class);

    private HTMLWidgetType custWidget;
    
    private String type;
    
	public HTMLCustWidgetType(String id) {
		super(id);
	}

    @Override
	public void generateBeginHTML(UserRequestContext context, UIFormObject ownerEntity, int depth) {
    	try {
			if (this.getAttribute("init") != null) {
				custWidget.addAttribute("expr", this.getAttribute("init"));
			}
			custWidget.attributeMap = this.attributeMap;
			custWidget.eventListenerMap = this.eventListenerMap;
			
			custWidget.generateBeginHTML(context, ownerEntity, depth);
		} catch (Exception e) {
			logger.error("error generating ui the customized widget. in entity: " + getUIEntityName() +"."+ type, e);
		}
	}
    
    @Override
    public void generateEndHTML(UserRequestContext context, UIFormObject ownerEntity, int depth) {
        try {
        	custWidget.generateEndHTML(context, ownerEntity, depth);
        } catch (Exception e) {
            logger.error("error generating ui the customized widget. in entity: " + getUIEntityName() +"."+ type, e);
        }
    }

    public JSONObject createJsonModel(VariableEvaluator ee) throws JSONException {
		try {
			type = (String)this.getAttribute("custType");
			Constructor<HTMLWidgetType> constructor = (Constructor<HTMLWidgetType>)
					Class.forName(type).getConstructor(String.class);
			custWidget = constructor.newInstance(this.getUIID());
			custWidget.setReadOnly(isReadOnly());
			return custWidget.createJsonModel(ee);
		} catch (Exception e) {
			logger.error("error generating ui the customized widget. in entity: " + getUIEntityName() +"."+ type, e);
			return null;
		}
	}

}
