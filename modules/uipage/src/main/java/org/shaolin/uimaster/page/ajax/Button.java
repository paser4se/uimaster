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
import java.util.Map;

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.javacc.context.EvaluationContext;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.uimaster.page.AjaxContextHelper;
import org.shaolin.uimaster.page.DisposableBfString;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.cache.PageCacheManager;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Button extends TextWidget implements Serializable
{
	private static final Logger logger = LoggerFactory.getLogger(Button.class);
	
    private static final long serialVersionUID = 6492151356109754310L;
    
    public static final String NORMAL_BUTTON = "button";
    
    public static final String SUBMIT_BUTTON = "submit";
    
    public static final String RESET_BUTTON = "reset";
    
    private String buttonType = NORMAL_BUTTON;

    private Map<String, ExpressionType> expMap = null;
    
    public Button(String uiid)
    {
        this(AjaxContextHelper.getAjaxContext().getEntityPrefix() + uiid, new CellLayout());
        this.setListened(true);
    }
    
    public Button(String uiid, String title)
    {
        super(AjaxContextHelper.getAjaxContext().getEntityPrefix() + uiid, title, new CellLayout());
        this.setListened(true);
    }
    
    public Button(String id, Layout layout)
    {
        super(id, layout);
    }

    public void setExpressMap(Map<String, ExpressionType> expMap) {
    	this.expMap = expMap;
    }
    
    public boolean isReadOnly(EvaluationContext context) {
    	if (expMap == null || !this.expMap.containsKey("readOnly")) {
    		return this.isReadOnly();
    	}
    	try {
			return (boolean) this.expMap.get("readOnly").evaluate(context);
		} catch (EvaluationException e) {
			logger.warn(e.getMessage(), e.getCause());
			// anything goes wrong, set as true;
			return true;
		}
    }
    
    public boolean isVisible(EvaluationContext context) {
    	if (expMap == null || !this.expMap.containsKey("visible")) {
    		return this.isVisible();
    	}
    	try {
			return (boolean) this.expMap.get("visible").evaluate(context);
		} catch (EvaluationException e) {
			logger.warn(e.getMessage(), e.getCause());
			// anything goes wrong, set as false;
			return false;
		}
    }
    
    public void setAsEnabled() {
    	if (this.getAttribute("skipSetAsEnabled") == null) {
    		this._updateAttribute("disabled", "false");
    	}
    }
    
    /**
     * Button types
     * <ul>
     * <li>button</li>
     * <li>submit</li>
     * <li>reset</li>
     * </ul>
     * @param buttonType
     */
    public void setButtonType(String buttonType)
    {
        this.buttonType = buttonType;
        addAttribute("buttonType", buttonType);
    }

    public String getButtonType()
    {
        return buttonType;
    }
    
    public String generateJS()
    {
        StringBuffer js = new StringBuffer(200);
        js.append("defaultname.");
        js.append(getId());
        js.append("=new UIMaster.ui.button({");
        js.append("ui:elementList[\"");
        js.append(getId());
        js.append("\"]});");
        return js.toString();
    }    
    
    public String generateHTML()
    {
    	StringBuilder html = DisposableBfString.getBuffer();
        try {
	        generateWidget(html);
	        html.append("<input type=\"");
	        html.append(buttonType);
	        html.append("\" name=\"");
	        html.append(getId());
	        html.append("\"");
	        generateAttributes(html);
	        generateEventListeners(html);
	        html.append(" value=\"");
	        String value = HTMLUtil.formatHtmlValue(getValue());
	        html.append(this.isValueMask() ? WebConfig.getHiddenValueMask() : value);
	        html.append("\"");
	        if ( isReadOnly() )
	        {
	            html.append(" disabled=\"true\"");
	        }
	        html.append(" />");
	        
	        return html.toString();
	    } finally {
			DisposableBfString.release(html);
		}
    }

    public JSONObject toJSON() throws JSONException {
    	return super.toJSON();
    }
	
	@SuppressWarnings("unchecked")
	public void fromJSON(JSONObject json) throws Exception {
		super.fromJSON(json);
		String entityName = json.getString("entity");
		UIFormObject formObject = PageCacheManager.getUIForm(entityName);
		this.expMap = formObject.getComponentExpression(this.getId());
	}
}
