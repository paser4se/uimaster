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
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.shaolin.uimaster.page.AjaxContextHelper;
import org.shaolin.uimaster.page.DisposableBfString;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.cache.PageCacheManager;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.od.ODContext;

public class AFile extends TextWidget implements Serializable
{
    private static final long serialVersionUID = -8002328531496168701L;

    private String storedPath = null;
    
    private String suffix = "";
    
    private int allowedNumbers = -1;
    
    private int contentSize = -1;
    
    private int width = -1;
    
	private int height = -1;
    
	private ExpressionType refreshExpr = null;
	
	public AFile(String uiid)
    {
        this(AjaxContextHelper.getAjaxContext().getEntityPrefix() + uiid, new CellLayout());
        this._setWidgetLabel(uiid);
        this.setListened(true);
    }
    
    public AFile(String uiid, String path)
    {
        super(AjaxContextHelper.getAjaxContext().getEntityPrefix() + uiid, path, new CellLayout());
        this._setWidgetLabel(uiid);
        this.setListened(true);
    }
    
    public AFile(String id, Layout layout)
    {
        super(id, layout);
        this._setWidgetLabel(id);
    }
    
    public void setStoredPath(String storedPath) {
    	this.storedPath = storedPath;
    	if (this.allowedNumbers <= 1) {
    		return;
    	}
    	// check whether the given path is a file or directory.
		if (this.storedPath.lastIndexOf('/') != -1) {
			String tempPath = this.storedPath.substring(this.storedPath.lastIndexOf('/') + 1);
			if (tempPath.lastIndexOf('.') != -1) {
				this.storedPath = this.storedPath.substring(0, this.storedPath.lastIndexOf('/'));
			}
		} else if (this.storedPath.lastIndexOf('\\') != -1) {
			String tempPath = this.storedPath.substring(this.storedPath.lastIndexOf('\\') + 1);
			if (tempPath.lastIndexOf('.') != -1) {
				this.storedPath = this.storedPath.substring(0, this.storedPath.lastIndexOf('\\'));
			}
		} 
    }
    
    public int getAllowedNumbers() {
		return allowedNumbers;
	}

	public void setAllowedNumbers(int allowedNumbers) {
		this.allowedNumbers = allowedNumbers;
	}
	
	public void setContentSize(int size) {
		this.contentSize = size;
	}
    
	public int getContentSize() {
		return this.contentSize;
	}
	
    public String getStoredPath() {
    	return this.storedPath;
    }
    
    public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
    
    public String generateJS()
    {
        StringBuilder js = DisposableBfString.getBuffer();
        try {
	        js.append("defaultname.");
	        js.append(getId());
	        js.append("=new UIMaster.ui.file({");
	        js.append("ui:elementList[\"");
	        js.append(getId());
	        js.append("\"]});");
	        return js.toString();
        } finally {
        	DisposableBfString.release(js);
        }
    }    
    
    public String generateHTML()
    {
    	StringBuilder html = DisposableBfString.getBuffer();
    	try {
	        generateWidget(html);
	        html.append("<script language=\"javascript\">document.forms[0].encoding=\"multipart/form-data\";</script>");
	        html.append("<input type=\"file\" name=\"");
	        html.append(getId());
	        html.append("\"");
	        generateAttributes(html);
	        generateEventListeners(html);
	        html.append(" value=\"");
	        String value = HTMLUtil.formatHtmlValue(getValue());
	        html.append(this.isValueMask() ? WebConfig.getHiddenValueMask() : value);
	        html.append("\" />");  
	        
	        return html.toString();
    	} finally {
        	DisposableBfString.release(html);
        }
    }
    
    public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void refresh() {
		if (this.refreshExpr == null) {
			return;
		}
		try {
			OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
			DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
			evaContext.setVariableValue("page", AjaxContextHelper.getAjaxContext());
			ooeeContext.setDefaultEvaluationContext(evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.GLOBAL_TAG, evaContext);
			this.refreshExpr.evaluate(ooeeContext);
		} catch (Exception e) {
			logger.error("error occurrs after selecting image: " + this.getId(), e);
		}
	}
	
	public ExpressionType getRefreshExpr() {
		return refreshExpr;
	}

	public void setRefreshExpr(ExpressionType refreshExpr) {
		this.refreshExpr = refreshExpr;
	}

	public JSONObject toJSON() throws JSONException {
    	JSONObject json = super.toJSON();
    	json.put("storedPath", this.storedPath);
    	json.put("allowNbs", this.allowedNumbers);
    	json.put("contentSize", this.contentSize);
    	json.put("width", this.width);
    	json.put("height", this.height);
    	return json;
    }
    
    public void fromJSON(JSONObject json) throws Exception {
    	super.fromJSON(json);
    	this.storedPath = json.getString("storedPath");
    	this.allowedNumbers = json.getInt("allowNbs");
    	this.contentSize = json.getInt("contentSize");
    	this.width = json.getInt("width");
    	this.height = json.getInt("height");
    	
    	String entityName = json.getString("entity");
    	UIFormObject formObject = PageCacheManager.getUIForm(entityName);
		Map<String, Object> attributes = formObject.getComponentProperty(this.getId(), true);
		this.refreshExpr = (ExpressionType)attributes.get("refreshExpr");
    }
}
