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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.shaolin.bmdp.datamodel.page.StringPropertyType;
import org.shaolin.bmdp.datamodel.page.ValidatorPropertyType;
import org.shaolin.bmdp.i18n.LocaleContext;
import org.shaolin.bmdp.i18n.ResourceUtil;
import org.shaolin.uimaster.html.layout.IUISkin;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.exception.AttributeSetAlreadyException;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The top of all HTML widgets. 
 * 
 * Be aware of these HTML* classes are the static structure which shared for all user requests. 
 * Each user requested data stores in {@link UserRequestContext} only.
 * 
 * @author wushaol
 *
 */
public abstract class HTMLWidgetType implements Serializable
{
    private static final Logger logger = LoggerFactory.getLogger(HTMLWidgetType.class);
    private static final long serialVersionUID = -6119707922874957783L;
    
    private String entityName;
    private final String id;
    // defined static maps.
    Map<String, Object> attributeMap;
    Map<String, Object> eventListenerMap;
    
    // defined static UISkin.
    private IUISkin uiskinObj;

	public HTMLWidgetType(final String id) {
    	this.id = id;
    }

    public void reset()
    {
        this.attributeMap = null;
        this.eventListenerMap = null;
    }

    public void setUIEntityName(String entityName) {
    	this.entityName = entityName;
    }
    
    public String getUIEntityName() {
    	return this.entityName;
    }
    
    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return getName(true);
    }
    
    public String getName(boolean withSuffix)
    {
        String name = id;
        UserRequestContext context = UserRequestContext.UserContext.get();
        if (context != null && context.getHTMLPrefix() != null && context.getHTMLPrefix().length() > 0)
        {
        	name = UserRequestContext.UserContext.get().getHTMLPrefix() + id;
        }
        return name;
    }

    public IUISkin getUISkin() {
		return uiskinObj;
	}

	public void setUISkin(IUISkin uiskinObj) {
		this.uiskinObj = uiskinObj;
	}
    
    /**
     * this is static attributes sharing for all users.
     * 
     * @param attributeMap
     * @throws AttributeSetAlreadyException
     */
    public void setAttribute(Map<String, Object> attributeMap) throws AttributeSetAlreadyException
    {
        if (attributeMap == null){
            return;
        }
        if (this.attributeMap != null)
        {
        	throw new AttributeSetAlreadyException("");
        }
        attributeMap.remove("readOnly");//useless in here.
        this.attributeMap = Collections.unmodifiableMap(attributeMap);
    }

    /**
     * Add dynamic attribute for current user.
     * 
     * @param name
     * @param value
     */
    public void addAttribute(String name, Object value)
    {
        int endIndex = name.indexOf("[");
        if (endIndex > -1)
        {
            name = name.substring(0, endIndex);
            Object valueList = UserRequestContext.UserContext.get().getAttribute(this.getName()).get(name);
			if (valueList != null) {
				if (valueList instanceof List) {
					((List) valueList).add(value);
				} else {
					logger.error(getName() + "'s " + name + " is not list attribute");
				}
			} else {
                valueList = new ArrayList();
                ((List) valueList).add(value);
                UserRequestContext.UserContext.get().addAttribute(this.getName(), name, valueList);
            }
        }
        else
        {
        	UserRequestContext.UserContext.get().addAttribute(this.getName(), name, value);
        }
    }

    public void setEventListener(Map<String, Object> eventMap) throws AttributeSetAlreadyException
	{
		if (eventMap == null) {
			return;
		}
		if (eventListenerMap != null) {
			throw new AttributeSetAlreadyException("");
		}
	    eventListenerMap = Collections.unmodifiableMap(eventMap);
	}

	public String getEventListener(String name)
	{
	    return eventListenerMap == null ? null : (String) eventListenerMap.get(name);
	}

	public boolean needAjaxSupport() {
    	return (attributeMap == null) ? false : attributeMap.containsKey("needAjaxSupport");
    }
    
    public Object getAttribute(String name)
    {
    	Object v = null;
    	String uiid = this.getName();
		if (UserRequestContext.UserContext.get().hasAttribute(uiid)) {
    		v = UserRequestContext.UserContext.get().getAttribute(uiid).get(name);
    	}
    	if (v != null) {
    		return v;
    	}
        return attributeMap == null ? null : attributeMap.get(name);
    }
    
    public Object removeAttribute(String name)
    {
    	String uiid = this.getName();
    	if (UserRequestContext.UserContext.get().hasAttribute(uiid)) {
    		Object v = UserRequestContext.UserContext.get().getAttribute(uiid).remove(name);
    		if (v != null) {
    			return v;
    		}
    	}
    	// we must not remove the static attribute instead getting only.
    	return attributeMap == null ? null : attributeMap.get(name);
    }

    public boolean containsAttribute(String name)
    {
    	String uiid = this.getName();
		if (UserRequestContext.UserContext.get().hasAttribute(uiid)) {
			return true;
		}
    	return attributeMap == null ? false : attributeMap.containsKey(name);
    }

    public void addStyle(String name, String value)
    {
    	UserRequestContext.UserContext.get().addStyle(getName(), name, value);
    }

    public String getStyle(String name)
    {
    	String v = null;
    	String uiid = this.getName();
		if (UserRequestContext.UserContext.get().hasAttribute(uiid)) {
    		v = UserRequestContext.UserContext.get().getStyle(uiid, name);
    	}
		return v;
    }

    public void setAJAXAttributes(UserRequestContext context, Widget widget)
    {
        Map<String, Object> odMappingAttrs = context.getAttribute(getName());
        if (odMappingAttrs != null && !odMappingAttrs.isEmpty())
        {
            for (Iterator it = odMappingAttrs.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry)it.next();
                String attributeName = (String)entry.getKey();;
                Object attributeValue = entry.getValue();
                setAJAXAttribute(attributeName, attributeValue, widget);
            }
        }

        if (attributeMap != null && !attributeMap.isEmpty())
        {
            if (odMappingAttrs == null || !odMappingAttrs.containsKey("viewPermission"))
            {
            	Object attributeValue = getAttribute("viewPermission");
                setAJAXAttribute("viewPermission", attributeValue, widget);
            }
            if (odMappingAttrs == null || !odMappingAttrs.containsKey("editPermission"))
            {
            	Object attributeValue = getAttribute("editPermission");
                setAJAXAttribute("editPermission", attributeValue, widget);
            }
            if (odMappingAttrs == null || !odMappingAttrs.containsKey("visible"))
            {
            	Object attributeValue = getAttribute("visible");
                setAJAXAttribute("visible", attributeValue, widget);
            }
            if (odMappingAttrs == null || !odMappingAttrs.containsKey("editable"))
            {
            	Object attributeValue = getAttribute("editable");
                setAJAXAttribute("editable", attributeValue, widget);
            }
        }
    }

    public void setAJAXAttribute(String attributeName, Object attributeValue, Widget widget)
    {
        if ("viewPermission".equals(attributeName))
        {
            String[] viewPermissions = convertToStringArray(attributeValue);
            if ((viewPermissions != null) && (viewPermissions.length > 0))
            {
                widget.setViewPermissions(viewPermissions);
            }
            return;
        }
        else if ("editPermission".equals(attributeName))
        {
            String[] editPermissions = convertToStringArray(attributeValue);
            if ((editPermissions != null) && (editPermissions.length > 0))
            {
                widget.setEditPermissions(editPermissions);
            }
            return;
        }
        else if ("visible".equals(attributeName))
        {
        	String attrValue = String.valueOf(attributeValue);
            if ("false".equals(attrValue))
            {
                widget.setVisible(false);
            }
            return;
        }
        else if ("editable".equals(attributeName))
        {
        	String attrValue = String.valueOf(attributeValue);
            if ("false".equals(attrValue))
            {
                widget.addAttribute("disabled", "true");
            }
            return;
        }
    }

    protected void setAJAXConstraints(Widget widget)
    {
        Object init = getAttribute("initValidation");
        if (init != null)
        {
            widget.addConstraint("initValidation", init, null);
        }
        Object value = getAttribute("validator");
        if (value != null)
        {
            ValidatorPropertyType validator = (ValidatorPropertyType)value;
            String message = validator.getErrMsg();
            if (message == null)
            {
                if (validator.getI18NMsg() != null)
                {
                    message = ResourceUtil.getResource(LocaleContext.getUserLocale(),
                            validator.getI18NMsg().getBundle(), 
                            validator.getI18NMsg().getKey());
                }
            }
            List<StringPropertyType> params = validator.getParams();
            String[] parameters = new String[params.size()];
            for (int i = 0; i < params.size(); i++)
            {
                parameters[i] = params.get(i).getValue();
            }
            widget.addCustomValidator(validator.getFuncCode(), parameters, message);
        }
        
        if (containsAttribute("allowBlank")) {
	        widget.addConstraint("allowBlank", 
	        		getAttribute("allowBlank"), 
	        		(String)getAttribute("allowBlankText"));
        }
		if (this instanceof HTMLTextWidgetType) {
			widget.addConstraint("minLength", 
					getAttribute("minLength"), 
					(String)getAttribute("lengthText"));
			widget.addConstraint("regex", 
					getAttribute("regex"), 
					(String)getAttribute("regexText"));
			if (this instanceof HTMLTextAreaType) {
				widget.addConstraint("maxLength", 
						getAttribute("maxLength"), 
						(String)getAttribute("lengthText"));
			}
		} else if (this instanceof HTMLChoiceType) {
			if (this instanceof HTMLSingleChoiceType) {
				widget.addConstraint("selectedValueConstraint", 
						getAttribute("selectedValueConstraint"), 
						(String)getAttribute("selectedValueConstraintText"));
			} else if (this instanceof HTMLMultiChoiceType) {
				widget.addConstraint("selectedValuesConstraint", 
						(Object[])getAttribute("selectedValuesConstraint"), 
						(String)getAttribute("selectedValuesConstraintText"));
			}
		} else if (this instanceof HTMLSelectComponentType) {
			if (this instanceof HTMLCheckBoxType) {
				widget.addConstraint("mustCheck", 
						getAttribute("mustCheck"), 
						(String)getAttribute("mustCheckText"));
			}
		}
    }
    
    public String getReconfigurateFunction(String handler)
    {
        return getReconfigurateFunction(handler, true);
    }
    
    public String getReconfigurateFunction(final String handler, boolean needParameter)
    {
    	UserRequestContext context = UserRequestContext.UserContext.get();
    	String aHandler = handler;
        String functionPrefix = context.getHTMLPrefix();
        String reconfiguration = context.getReconfigFunction(functionPrefix, aHandler);
        while (reconfiguration != null)
        {
            if (functionPrefix.endsWith("."))
            {
                functionPrefix = functionPrefix.substring(0, functionPrefix.length() - 1);
            }
            int endIndex = functionPrefix.lastIndexOf(".");
            if (endIndex == -1)
            {
                functionPrefix = "";
            }
            else
            {
                functionPrefix = functionPrefix.substring(0 ,endIndex + 1);
            }
            aHandler = reconfiguration;
            reconfiguration = context.getReconfigFunction(functionPrefix, aHandler);
        }

        String functionName = "defaultname." + functionPrefix + aHandler;
        if (needParameter && aHandler.indexOf('(') == -1) {  
	        String parameter = "defaultname." + getName() + ",event";
	        return "javascript:" + functionName + "(" + parameter + ")";
        } else {
        	return "javascript:" + functionName;
        }
    }

    public HTMLLayoutType getHTMLLayout()
    {
        return (HTMLLayoutType)UserRequestContext.UserContext.get().getAttribute(this.getName(), "layout");
    }

    public void setHTMLLayout(final HTMLLayoutType htmlLayout)
    {
    	UserRequestContext.UserContext.get().addAttribute(this.getName(), "layout", htmlLayout);
    }

    public abstract void generateBeginHTML(UserRequestContext context, UIFormObject ownerEntity, int depth);

    public abstract void generateEndHTML(UserRequestContext context, UIFormObject ownerEntity, int depth);

    public void generateAttributes(UserRequestContext context) throws IOException
    {
    	if (attributeMap != null && !attributeMap.isEmpty())
        {
	    	String type = (String)attributeMap.get("type");
	        if (type != null)
	        {
	            String defaultCssClass = "uimaster_" + type.substring(0, 1).toLowerCase() + type.substring(1, type.length()-4);
	            if (isReadOnly() != null && isReadOnly().booleanValue())
	            {
	                defaultCssClass += "_readonly";
	            }
	            String UIStyle = (String)getAttribute("UIStyle");
	            if (UIStyle != null && !UIStyle.trim().equals("null"))
	            {
	            	if (defaultCssClass.contains(UIStyle)) {
	            		UIStyle = defaultCssClass;
	            	} else {
	            		UIStyle = defaultCssClass + " " + UIStyle;
	            	}
	                addAttribute("UIStyle", UIStyle);
	            }
	            else
	            {
	            	addAttribute("UIStyle", defaultCssClass);
	            }
	        }
	    }
    	
        Map<String, Object> odMappingAttrs = context.getAttribute(getName());
        if (odMappingAttrs != null && !odMappingAttrs.isEmpty())
        {
            for (Iterator it = odMappingAttrs.keySet().iterator(); it.hasNext(); )
            {
                String attributeName = (String) it.next();
                Object attributeValue = odMappingAttrs.get(attributeName);
                generateAttribute(context, attributeName, attributeValue);
            }
        }

        if (attributeMap != null && !attributeMap.isEmpty())
        {
            for (Iterator it = attributeMap.keySet().iterator(); it.hasNext(); )
            {
                String attributeName = (String) it.next();
                if ("type".equals(attributeName)) {
                	continue;
                }
                if (odMappingAttrs == null || !odMappingAttrs.containsKey(attributeName))
                {
                    Object attributeValue = getAttribute(attributeName);
                    generateAttribute(context, attributeName, attributeValue);
                }
            }
        }

        if (context.hasStyle(this.getName()))
        {
            context.generateHTML(" style=\"");
            Iterator it = context.getStyle(this.getName()).entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry entry = (Map.Entry)it.next();
                String name = (String) entry.getKey();
                String value = (String) entry.getValue();
                context.generateHTML(name);
                context.generateHTML(":");
                context.generateHTML(value);
                context.generateHTML(";");
            }
            context.generateHTML("\"");
            context.getStyle(this.getName()).clear();//only used once.
        }
    }

    private String[] convertToStringArray(Object attributeValue)
    {
        if (attributeValue instanceof String)
        {
            return new String[]{(String)attributeValue};
        }
        else if (attributeValue instanceof List)
        {
            List<String> temp = (List<String>)attributeValue;
            String[] tempArray = new String[temp.size()];
            return temp.toArray(tempArray);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * This method will replace \\ to \ to avoid html escape issue
     * @param origin
     */
    private String handleEscape(String origin)
    {    
        StringBuffer sb = new StringBuffer(origin.length());
        int index1 = 0;
        int index2 = 0;
        while(true)
        {
            index1 = origin.indexOf("\\\\",index2);
            if(index1 < 0)//to the end
            {
                sb.append(origin.substring(index2));
                break;
            }
            else
            {
                sb.append(origin.substring(index2,index1));
                sb.append("\\");
                index2 = index1+2;
            }
        }
        return sb.toString();
    }

    public void generateEventListeners(UserRequestContext context) throws IOException
    {
		if (eventListenerMap != null) {
            for (Iterator<String> it = eventListenerMap.keySet().iterator();it.hasNext();) {
                String event = it.next();
                context.generateHTML(" ");
                context.generateHTML(event);
                context.generateHTML("=\"");
                context.generateHTML(getReconfigurateFunction(getEventListener(event)));
                context.generateHTML("\"");
            }
        }
    }

    private static final List<String> SKIP_ATTRS = new ArrayList<String>();
    static{
	    SKIP_ATTRS.add("referenceEntity");
	    SKIP_ATTRS.add("layout");
	    SKIP_ATTRS.add("initValidation");
	    SKIP_ATTRS.add("validator");
	    SKIP_ATTRS.add("viewPermission");
	    SKIP_ATTRS.add("editPermission");
    }
    public void generateAttribute(UserRequestContext context, String attributeName, Object attributeValue) throws IOException
    {
        if(SKIP_ATTRS.contains(attributeName))
        {
            return;
        }
        
        String attrValue = attributeValue == null ? "": attributeValue.toString();
        if ("visible".equals(attributeName))
        {
            if ("false".equals(attrValue))
            {
                addStyle("display", "none");
            }
        }
        else if ("editable".equals(attributeName))
        {
            if ("false".equals(attrValue))
            {
                context.generateHTML(" disabled=\"true\"");
            }
        }
        else if ("UIStyle".equals(attributeName))
        {
            if (!"null".equals(attrValue))
            {
                context.generateHTML(" class=\"");
                context.generateHTML(attrValue);
                context.generateHTML("\"");
            }
        }
        else if("reconfiguration".equals(attributeName))
        {
            context.generateHTML(" reconfiguration='");
            context.generateHTML(attrValue);
            context.generateHTML("'");
        }
        else if("regex".equals(attributeName))
        {
            context.generateHTML(" regex=\"");
            context.generateHTML(HTMLUtil.formatHtmlValue(this.handleEscape(attrValue)));
            context.generateHTML("\"");
        }
        else if (attributeName.endsWith("-msg--"))
        {
            return;
        }
        else if ("value".equals(attributeName))
        {
            if(context.isValueMask())
            {
                context.generateHTML(" ");
                context.generateHTML(attributeName);
                context.generateHTML("=\"");
                context.generateHTML(HTMLUtil.formatHtmlValue(WebConfig.getHiddenValueMask()));
                context.generateHTML("\"");
            }
        }
        else if ("readOnly".equals(attributeName) && "false".equals(attrValue))
        {
        	return;
        }
        else
        {
            context.generateHTML(" ");
            context.generateHTML(attributeName);
            context.generateHTML("=\"");
            context.generateHTML(HTMLUtil.formatHtmlValue(attrValue));
            context.generateHTML("\"");
        }
    }

    public void addHints(UserRequestContext context) {
    	if (this.getAttribute("hints") != null || this.getAttribute("hintsDesc") != null) {
    		String desc = (String)this.getAttribute("hintsDesc");
    		if (desc == null) {
    			desc = "";
    		}
        	context.generateHTML("<i class=\"ui-icon ui-icon-lightbulb uimaster-hints-icon\" alt=\""+desc+"\" onclick=\"javascript:UIMaster.getHints(this, '"+this.getAttribute("hints")+"');\"></i>");
        }
    }
    
    public void generateWidget(UserRequestContext context)
    {
        Object visibleValue = getAttribute("visible");
        boolean unVisible = false;
        if ( visibleValue != null )
        {
            unVisible = visibleValue.toString().equals("false");
        }
        if ( unVisible )
        {
            context.generateHTML("<span style=\"display:none;\">");
        }
        String widgetLabel = (String)removeAttribute("widgetLabel");
        if ( widgetLabel !=  null && !widgetLabel.equals("") )
        {
            String widgetLabelColor = (String)removeAttribute("widgetLabelColor");
            String widgetLabelFont = (String)removeAttribute("widgetLabelFont");

            context.generateHTML("<label");
            context.generateHTML(" id=\"" + getName() + "_widgetLabel\"");
            context.generateHTML(" style=\"");
            if ( widgetLabelColor != null )
            {
                context.generateHTML("color:");
                if (widgetLabelColor.startsWith("#"))
                {
                    context.generateHTML(widgetLabelColor);
                }
                else
                {
                    context.generateHTML("rgb(" + widgetLabelColor + ")");
                }
                context.generateHTML(";");
            }
            if ( widgetLabelFont != null )
            {
                String[] fontInfo  = widgetLabelFont.split(",");
                if (fontInfo.length != 3)
                {
                    logger.warn("widgetLabelFont error:" + widgetLabelFont);
                }
                else
                {
                    String fontStyle = "Plain".equals(fontInfo[2]) ? "normal" : fontInfo[2];
                    fontStyle += " " + fontInfo[1] + "pt";
                    fontStyle += " " + fontInfo[0];
                    context.generateHTML("font:");
                    context.generateHTML(fontStyle);
                    context.generateHTML(";");
                }
            }
            context.generateHTML("\" class=\"uimaster_widgetLabel\">");
            if ( widgetLabel.equals(" ") )
            {
                context.generateHTML("&nbsp;");
            }
            else
            {
                context.generateHTML(widgetLabel);
            }
            context.generateHTML("</label>");
        }

        if ( unVisible )
        {
            context.generateHTML("</span>");
        }
    }

    public void generateEndWidget(UserRequestContext context)
    {
    	addHints(context);
    	String dlinkInfo = (String)getAttribute("dtargetInfo");
        if (dlinkInfo != null)
        {
        	int index = dlinkInfo.lastIndexOf(";");
        	String link = dlinkInfo.substring(0, index);
        	String uipanel = dlinkInfo.substring(index + 1);
        	String comment = (String)getAttribute("dlinkInfo");
        	context.generateHTML("<span><a href=\"javascript:dPageLink('");
        	context.generateHTML(link);
        	context.generateHTML("','");
        	context.generateHTML(uipanel);
        	context.generateHTML("');\" title=\"");
        	context.generateHTML(comment);
        	context.generateHTML("\");\">");
        	context.generateHTML(comment);
        	context.generateHTML("</a></span>");
        }
    }
    
    public Boolean isReadOnly()
    {
    	Object v = getAttribute("readOnly");
        return v == null ? Boolean.FALSE : "true".equalsIgnoreCase(v.toString());
    }
    
    public void setReadOnly(boolean v) 
    {
    	UserRequestContext.UserContext.get().addAttribute(this.getName(), "readOnly", v);
    }
    
    public boolean isVisible()
    {
        String visible = (String) getAttribute("visible");
        return visible == null ? true : "true".equals(visible);
    }

    public void setVisible(boolean visible)
    {
        addAttribute("visible", String.valueOf(visible));
    }

    public void disableValidation()
    {
        addAttribute("validationFlag", "disabled");
    }
    
    public boolean isEditable()
    {
        Object editable = getAttribute("editable");
        return (editable == null || editable.toString().trim().isEmpty()) ? true : "true".equalsIgnoreCase(editable.toString());
    }

    public void setEditable(boolean editable)
    {
        addAttribute("editable", String.valueOf(editable));
    }

    public String getUIID()
    {
        return this.id;
    }
    
    /**
     * Whether this component can have editPermission.
     */
    public boolean isEditPermissionEnabled()
    {
        return true;
    }
    
    /**
     * this method uses for UI to Data operation.
     * @return
     */
	protected String getValueFromRequest(UserRequestContext context) 
	{
		return context.getRequest().getParameter(this.getUIID());
	}
	
	/**
     * this method uses for UI to Data operation.
     * @return
     */
	protected String[] getValuesFromRequest(UserRequestContext context) 
	{
		return context.getRequest().getParameterValues(this.getUIID());
	}
	
	public Widget createAjaxWidget(VariableEvaluator ee) {
		return null;
	}
	
	public String toString() {
		return this.getName() + "[" + this.getClass().getSimpleName() + "]";
	}
}
