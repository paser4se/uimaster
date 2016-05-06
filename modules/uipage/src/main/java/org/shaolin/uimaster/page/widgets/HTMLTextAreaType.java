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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.bmdp.utils.FileUtil;
import org.shaolin.uimaster.page.HTMLSnapshotContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.ajax.Layout;
import org.shaolin.uimaster.page.ajax.TextArea;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLTextAreaType extends HTMLTextWidgetType
{
	private static final long serialVersionUID = -2216731075469117671L;
	
    private static Logger logger = LoggerFactory.getLogger(HTMLTextAreaType.class);

    public HTMLTextAreaType()
    {
    }

    public HTMLTextAreaType(HTMLSnapshotContext context)
    {
        super(context);
    }

    public HTMLTextAreaType(HTMLSnapshotContext context, String id)
    {
        super(context, id);
    }

    @Override
	public void generateBeginHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth) {
		
	}
    
    public void generateEndHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth)
    {
        try
        {
            generateWidget(context);
            if ( isReadOnly() != null && isReadOnly().booleanValue() )
            {
                addAttribute("allowBlank", "true");
                addAttribute("readOnly", "true");
            }
            boolean isHTMLSupported = this.getAttribute("htmlSupport") != null && 
            		"true".equals(this.getAttribute("htmlSupport").toString());
            if (context.getRequest().getAttribute("_hasCKeditor") == null) {
				context.getRequest().setAttribute("_hasCKeditor", Boolean.TRUE);
	            String root = (UserContext.isMobileRequest() && UserContext.isAppClient()) 
	        			? WebConfig.getAppResourceContextRoot() : WebConfig.getResourceContextRoot();
	        	context.generateHTML("<script type=\"text/javascript\" src=\""+root+"/js/controls/ckeditor/ckeditor.js\"></script>");
            }
            
            context.generateHTML("<textarea name=\"");
            context.generateHTML(getName());
            context.generateHTML("\"");
            generateAttributes(context);
            if (getAllAttribute("rows") == null)
            {
                context.generateHTML(" rows=\"4\"");
            }
            if (getAllAttribute("cols") == null)
            {
                context.generateHTML(" cols=\"30\"");
            }
            generateEventListeners(context);
			if (isHTMLSupported) {
            	context.generateHTML(" style=\"display:none;\"");
            }
            context.generateHTML(">");
            if (context.isValueMask())
            {
                context.generateHTML(WebConfig.getHiddenValueMask());
            }
            else
            {
        		context.generateHTML(HTMLUtil.formatHtmlValue(getValue()));
            }
            context.generateHTML("</textarea>");
            if (isHTMLSupported) {
            	if (this.getAttribute("viewMode") != null && 
                		"true".equals(this.getAttribute("viewMode").toString())) {
            		context.generateHTML("<iframe id=\"");
        			context.generateHTML(getName());
        			context.generateHTML("\" name=\"");
        			context.generateHTML(getName());
        			context.generateHTML("\" src=\"");
        			context.generateHTML(WebConfig.getResourceContextRoot());
        			context.generateHTML("/");
        			context.generateHTML(getValue());
        			context.generateHTML("\" frameborder=\"0\" width=\"100%\" height=\"100%\" scrolling='yes'>");
        			context.generateHTML("</iframe>");
            	} else {
	            	context.generateHTML("<div>");
	            	HTMLUtil.generateTab(context, depth);
	            	context.generateHTML("<textarea name=\"");
	            	context.generateHTML(getName());
	                context.generateHTML("_ckeditor\" style=\"display:none\">");
	            	File file = new File(WebConfig.getResourcePath() + getValue());
	        		if (file.exists() && file.isFile()) {
	        			String content = FileUtil.readFile(new FileInputStream(file));
	        			context.generateHTML(content);
	        		}
	        		context.generateHTML("</textarea>");
	        		HTMLUtil.generateTab(context, depth);
		        	HTMLUtil.generateTab(context, depth);
		        	context.generateHTML("</div>");
            	}
            }
            
            generateEndWidget(context);
        }
        catch (Exception e)
        {
            logger.error("error. in entity: " + getUIEntityName(), e);
        }
    }

    public void generateAttribute(HTMLSnapshotContext context, String attributeName, Object attributeValue) throws IOException
    {
        if ("initValidation".equals(attributeName) || "validator".equals(attributeName))
        {
        	return;
        }
        if ("editable".equals(attributeName))
        {
            if ("false".equals(String.valueOf(attributeValue)))
            {
                context.generateHTML(" readOnly=\"true\"");
            }
        }
        else if ("prompt".equals(attributeName))
        {
            if ( attributeValue != null && !((String)attributeValue).trim().equals("") )
            {
                context.generateHTML(" title=\"");
                context.generateHTML((String)attributeValue);
                context.generateHTML("\"");
            }
        }
        else
        {
            super.generateAttribute(context, attributeName, attributeValue);
        }
    }

    public Widget createAjaxWidget(VariableEvaluator ee)
    {
        TextArea textArea = new TextArea(getName(), Layout.NULL);

        textArea.setReadOnly(isReadOnly());
        textArea.setUIEntityName(getUIEntityName());

        // we don't expect to anything except the pure value 
        // what we really need in the backend.
        textArea.setValue(getValue());
        if (this.getAttribute("htmlSupport") != null && 
        		"true".equals(this.getAttribute("htmlSupport").toString())) {
        	textArea.setHtmlSupport(true);
        	if (this.getAttribute("viewMode") != null && 
            		"true".equals(this.getAttribute("viewMode").toString())) {
        		textArea.setHtmlSupport(false);
        	}
        }
        
        setAJAXConstraints(textArea);
        setAJAXAttributes(textArea);
        
        textArea.setListened(true);
        textArea.setFrameInfo(getFrameInfo());

        return textArea;
    }
    
}
