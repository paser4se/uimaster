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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.PageDispatcher;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.ajax.Layout;
import org.shaolin.uimaster.page.ajax.RefForm;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.exception.UIPageException;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.shaolin.uimaster.page.od.ODContext;

public class HTMLReferenceEntityType extends HTMLWidgetType implements Serializable
{
    public static final String EVENT = "event";
    public static final String PROPERTY = "property";
    public static final String VARIABLE = "variable";

    private static Logger logger = Logger.getLogger(HTMLReferenceEntityType.class);

    private String refEntityName;
    
    private Map functionReconfigurationMap;
    
    public HTMLReferenceEntityType(String id)
    {
    	super(id);
    }

    public HTMLReferenceEntityType(String id, String type)
    {
        super(id);
        String refE = getReferenceEntity();
        if (refE != null)
        {
            this.refEntityName = refE;
        }
        else
        {
            this.refEntityName = type;
        }
        setReferenceEntity(this.refEntityName);
    }

    public HTMLReferenceEntityType(String id, HTMLReferenceEntityType referenceEntity)
    {
        super(id);
        if (referenceEntity != null)
        {
            this.refEntityName = referenceEntity.getType();
            setReferenceEntity(this.refEntityName);
        }
    }

    public void setEntityName(String type)
    {
        this.refEntityName = type;
    }

    public String getType()
    {
        return refEntityName;
    }

    
    public void generateBeginHTML(UserRequestContext context, UIFormObject ownerEntity, int depth)
    {
        String name = getName();
        context.setCurrentFormInfo(refEntityName, (name + "."), (getDIVPrefix(context.getDIVPrefix()) + "-"));
        
		if (functionReconfigurationMap != null) {
			context.setReconfigFunction(functionReconfigurationMap);
		}
	}
	
	private String getDIVPrefix(String divPrefix)
    {
        if (divPrefix != null && divPrefix.length() > 0) {
            return divPrefix + getId();
        }
        return getId();
    }

    public void generateEndHTML(UserRequestContext context, UIFormObject ownerEntity, int depth)
    {
		try {
            HTMLWidgetType parentComponent = (HTMLWidgetType) context.getRequest().
                    getAttribute(UserRequestContext.REQUEST_PARENT_TAG_KEY);
            context.getRequest().setAttribute(UserRequestContext.REQUEST_PARENT_TAG_KEY, this);
            generateWidget(context);
            generateHTML(context, depth);
            
            context.getRequest().setAttribute(UserRequestContext.REQUEST_PARENT_TAG_KEY, parentComponent);
            generateEndWidget(context);
            
            context.resetCurrentFormInfo();
		} catch (Exception e) {
			throw new IllegalStateException("Error occur in form: " + refEntityName, e);
		}
    }

    private void setReferenceEntity(String referenceEntity)
    {
        String htmlName = getName();
        if (htmlName.endsWith("."))
        {
            htmlName = htmlName.substring(0, htmlName.length() - 1);
        }
        if (UserRequestContext.UserContext.get() != null) {
        	UserRequestContext.UserContext.get().addAttribute(htmlName, "referenceEntity", referenceEntity);
        }
    }

    public String getReferenceEntity()
    {
        return this.refEntityName;
    }

    public boolean isHidden()
    {
        return "false".equals((String) getAttribute("visible"));
    }

    public void addFunctionReconfiguration(String name, String value)
    {
        if (functionReconfigurationMap == null)
        {
            functionReconfigurationMap = new HashMap();
        }
        functionReconfigurationMap.put(name, value);
    }

	public void setReconfiguration(Map reconfigurationMap, Map propMap, Map appendMap) {
		if (reconfigurationMap != null) {
			functionReconfigurationMap = (Map) reconfigurationMap.get(EVENT);
		}
	}

    private void generateHTML(UserRequestContext context, int depth) throws UIPageException
    {
    	UIFormObject entity = HTMLUtil.parseUIForm(getReferenceEntity());
        PageDispatcher dispatcher = new PageDispatcher(entity, new DefaultEvaluationContext());
        dispatcher.forwardForm(context, depth, isReadOnly(), this);
    }
    
    public Widget createAjaxWidget(VariableEvaluator ee)
    {
    	HTMLReferenceEntityType copy = new HTMLReferenceEntityType(this.getId());
    	copy.refEntityName = this.refEntityName;
    	
    	DefaultEvaluationContext evalContext = (DefaultEvaluationContext)ee.getExpressionContext(ODContext.LOCAL_TAG);
        String refEntityName = this.refEntityName;
        RefForm referenceEntity = new RefForm(getName(), refEntityName, Layout.NULL, new HashMap(evalContext.getVariableObjects()));
        referenceEntity.setCopy(copy);
        referenceEntity.setReadOnly(isReadOnly());
        referenceEntity.setListened(true);
        referenceEntity.setFrameInfo(UserRequestContext.UserContext.get().getFrameInfo());
        return referenceEntity;
    }

    private static final long serialVersionUID = -6715298246482475095L;
}
