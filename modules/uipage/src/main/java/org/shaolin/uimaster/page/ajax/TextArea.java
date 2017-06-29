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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import org.shaolin.bmdp.utils.FileUtil;
import org.shaolin.bmdp.utils.StringUtil;
import org.shaolin.uimaster.page.AjaxContextHelper;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.DisposableBfString;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.ajax.json.IDataItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextArea extends TextWidget implements Serializable
{
    private static Logger logger = LoggerFactory.getLogger(TextArea.class);
	
    private static final long serialVersionUID = 7184128621017147296L;
    
    private String cssClass;
    
    private boolean htmlSupport;

	public TextArea(String uiid)
    {
        this(AjaxContextHelper.getAjaxContext().getEntityPrefix() + uiid, new CellLayout());
        this._setWidgetLabel(uiid);
        this.setListened(true);
    }
    
    public TextArea(String uiid, String value)
    {
        super(AjaxContextHelper.getAjaxContext().getEntityPrefix() + uiid, value, new CellLayout());
        this._setWidgetLabel(uiid);
        this.setListened(true);
    }

    public TextArea(String id, Layout layout)
    {
        super(id, layout);
        this._setWidgetLabel(id);
    }

    public TextArea addAttribute(String name, Object value, boolean update)
    {
        if ( name.equals("editable") )
        {
            if ( value == null ? false : !"true".equals(value) )
            {
                setReadOnly(Boolean.TRUE);
            }
        }
        else if ( name.equals("prompt") )
        {
            super.addAttribute("title", value, true);
        }
        else if ( name.equals("class") )
        {
            cssClass = (String)value;
            super.addAttribute("class", cssClass, true);
        }
        else
        {
            super.addAttribute(name, value, update);
        }
        return this;
    }
    
    public void setRows(int row)
    {
        super.addAttribute("rows", row);
    }
    
    public void setCols(int col)
    {
        super.addAttribute("cols", col);
    }
    
    public void append(String str)
    {
        String body = this.getValue()+str;
        this.setValue(body);
    }
    
    public void clear()
    {
        this.setValue("");
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
        
        AjaxContext ajaxContext = AjaxContextHelper.getAjaxContext();
        if (ajaxContext == null || !ajaxContext.existElement(this))
        {
            return;
        }
        
        StringBuilder sb = new  StringBuilder();
        sb.append("{'name':'value','value':'");
        sb.append(StringUtil.escapeHtmlToBytes(String.valueOf(value)));
        sb.append("'}");
        IDataItem dataItem = AjaxContextHelper.updateAttrItem(this.getId(), sb.toString());
        dataItem.setFrameInfo(getFrameInfo());
        ajaxContext.addDataItem(dataItem);
        
        if (!this.isHtmlSupport()) {
    		return;
    	}
        File file = new File(WebConfig.getResourcePath() + getValue());
        if (file.isDirectory()) {
        	logger.warn("The html content cannot be found in a directory: " + file.getAbsolutePath());
        	return;
        }
		
        sb = new  StringBuilder();
        sb.append("{'name':'value','ishtmlcontent':true,'value':'");
        try {
        	sb.append(StringUtil.escapeHtmlToBytes(FileUtil.readFile(new FileInputStream(file))));
        	sb.append("'}");
        	IDataItem dataItem1 = AjaxContextHelper.updateAttrItem(this.getId(), sb.toString());
        	dataItem1.setFrameInfo(getFrameInfo());
        	ajaxContext.addDataItem(dataItem1);
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
    
    public void setHTMLContent(String htmlContent) {
    	if (!this.isHtmlSupport()) {
    		return;
    	}
		try {
			File file = new File(WebConfig.getResourcePath() + getValue());
			if (file.isDirectory()) {
				logger.warn("The html content cannot be stored in a directory: " + file.getAbsolutePath());
				return;
			}
			FileUtil.write(file.getAbsolutePath(), htmlContent);
			logger.info("Saved the html content: " + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public boolean isHtmlSupport() {
		return htmlSupport;
	}

	public void setHtmlSupport(boolean htmlSupport) {
		this.htmlSupport = htmlSupport;
	}
    
    public String generateJS()
    {
        StringBuffer js = new StringBuffer(200);
        js.append("defaultname.");
        js.append(getId());
        js.append("=new UIMaster.ui.textarea({");
        js.append("ui:elementList[\"");
        js.append(getId());
        js.append("\"]");
        js.append(super.generateJS());
        js.append("});");
        return js.toString();
    }
    
    protected TextArea generateAttribute(String name, Object value, StringBuilder sb)
    {
        String attrValue = (String)value;
        if ("editable".equals(name))
        {
            if ("false".equals(String.valueOf(attrValue)))
            {
                sb.append(" readOnly=\"true\"");
            }
        }
        else if ("prompt".equals(name))
        {
            if ( attrValue != null && !attrValue.trim().equals("") )
            {
                sb.append(" title=\"");
                sb.append(attrValue);
                sb.append("\"");
            }
        }
        else
        {
            super.generateAttribute(name, value, sb);
        }
        return this;
    }
    
    public String generateHTML()
    {
    	StringBuilder html = DisposableBfString.getBuffer();
    	try {
        generateWidget(html);
        html.append("<textarea name=\"");
        html.append(getId());
        html.append("\" class=\"");
        if ( isReadOnly() )
        {
            html.append("uimaster_textArea_readOnly ");
            if ( cssClass != null && !cssClass.trim().equals("null") )
            {
                html.append(cssClass);
            }
            html.append("\" readOnly=\"true\"");
            addAttribute("allowBlank", "true", false);
        }
        else
        {
            html.append("uimaster_textArea ");
            if ( cssClass != null && !cssClass.trim().equals("null") )
            {
                html.append(cssClass);
            }
            html.append("\"");
        }
        html.append("\"");
        generateAttributes(html);
        if ( getAttribute("rows") == null )
        {
            html.append(" rows=\"4\"");
        }
        if ( getAttribute("cols") == null )
        {
            html.append(" cols=\"30\"");
        }
        generateEventListeners(html);
        html.append(">");
        String value = HTMLUtil.formatHtmlValue(getValue());
        html.append(this.isValueMask() ? WebConfig.getHiddenValueMask() : value);
        html.append("</textarea>");

        return html.toString();
    	} finally {
			DisposableBfString.release(html);
		}
    }

}
