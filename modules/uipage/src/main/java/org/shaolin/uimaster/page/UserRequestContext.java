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
package org.shaolin.uimaster.page;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shaolin.bmdp.runtime.Registry;
import org.shaolin.bmdp.utils.CloseUtil;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.ajax.json.JSONObject;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.exception.UIComponentNotFoundException;
import org.shaolin.uimaster.page.od.ODContext;
import org.shaolin.uimaster.page.security.ComponentPermission;
import org.shaolin.uimaster.page.widgets.HTMLReferenceEntityType;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created a user context to deal with a request received from the client. 
 * 
 * @author wushaol
 *
 */
public class UserRequestContext implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(UserRequestContext.class);
	
    public static final String REQUEST_PARENT_TAG_KEY = "REQUEST_PARENT_TAG_KEY";
    private static final String RECONFIGURATION_FUNCTION_KEY = "RECONFIGURATION_FUNCTION_KEY";

    private transient HttpServletRequest request;
    private transient HttpServletResponse response;
    private transient Writer out;
    private transient UIFormObject currentUIForm;
    private java.util.Stack<RefEntityInfo> refEntityStack = new java.util.Stack<RefEntityInfo>();
    
    private static class RefEntityInfo {
    	String htmlPrefix = "";
    	String divPrefix = "";
    	String formName = null;
    	String odName = null;
    	ODContext evalContext;
    }
    
    // for good performance
    private static final ThreadLocal<StringBuilder> htmlBuffer = new ThreadLocal<StringBuilder>();
    // for easy usability
    public static final ThreadLocal<UserRequestContext> UserContext = new ThreadLocal<UserRequestContext>();

    // uiid, <name, value>. this variable stores all dynamic attributes including expr and any other attributes for current page.
    private final Map<String, Map<String, Object>> uicompAttributes = new HashMap<String, Map<String, Object>>();
    private final Map<String, Map<String, String>> uicompStyles = new HashMap<String, Map<String, String>>();
    private final Map<String, Map<String, String>> reconfigurationMap = new HashMap<String, Map<String, String>>();
    private final Map<String, Widget<?>> ajaxWidgetMap = new HashMap<String, Widget<?>>();
    private transient Map<String, ComponentPermission> componentPermissions;
    private transient Map<String, UIFormObject> refEntityMap;
    private List<String> resourceBundles;
    
    private transient ArrayList<String> pageJs;

    private Set<String> jsNameSet = new HashSet<String>();
    
    private boolean jsonStyle;

    private boolean noResponse = false;

    private boolean isLeftToRight = true;

    private boolean isDataToUI = true;
    
    private int valueMaskCounter;
    
    public UserRequestContext(HttpServletRequest request, HttpServletResponse response) 
    {
        this.request = request;
        this.response = response;
        try {
        	String charset = Registry.getInstance().getEncoding();
        	response.setCharacterEncoding(charset);
        	response.setContentType("text/html;charset=" + charset);
			this.out = response.getWriter();
		} catch (IOException e) {
			logger.warn(e.getMessage(), e);
		}
    }

    public UserRequestContext(HttpServletRequest request)
    {
        this.request = request;
        noResponse = true;
    }
    
	public UserRequestContext(HttpServletRequest request, Writer writer) {
		this.request = request;
		this.out = writer;
	}

    public Map<String, Widget<?>> getPageAjaxWidgets()
    {
        return ajaxWidgetMap;
    }
    
    // this is designed for Ajax operation.
    public void setAjaxWidgetMap(final Map<String, Widget<?>> map) {
    	this.ajaxWidgetMap.putAll(map);
    }
    
    public void addAjaxWidget(final String compID, final Widget<?> component)
    {
        ajaxWidgetMap.put(compID, component);
    }
    
    public Widget<?> getAjaxWidget(String compID)
    {
        return ajaxWidgetMap.get(compID);
    }
    
    public void addAttribute(String uiid, Map<String, Object> items) {
    	if (uicompAttributes.containsKey(uiid)) {
    		uicompAttributes.get(uiid).putAll(items);
    	} else {
    		uicompAttributes.put(uiid, items);
    	}
    }
    
    public void addAttribute(String uiid, String name, Object value) {
    	if (uicompAttributes.containsKey(uiid)) {
    		uicompAttributes.get(uiid).put(name, value);
    	} else {
    		Map<String, Object> items = new HashMap<String, Object>();
    		items.put(name, value);
    		uicompAttributes.put(uiid, items);
    	}
    }
    
    public Map<String, Object> getAttribute(String uiid) {
    	return uicompAttributes.get(uiid);
    }
    
    public Object getAttribute(String uiid, String name) {
    	if (uicompAttributes.containsKey(uiid)) {
    		return uicompAttributes.get(uiid).get(name);
    	}
    	return null;
    }
    
    public boolean hasAttribute(String uiid) {
    	return uicompAttributes.containsKey(uiid);
    }
    
    public void addStyle(String uiid, String name, String value)
    {
    	if (uicompStyles.containsKey(uiid)) {
    		uicompStyles.get(uiid).put(name, value);
    	} else {
    		Map<String, String> items = new HashMap<String, String>();
    		items.put(name, value);
    		uicompStyles.put(uiid, items);
    	}
    }
    
    public boolean hasStyle(String uiid) {
    	return uicompStyles.containsKey(uiid);
    }
    
    public Map<String, String> getStyle(String uiid) {
    	return uicompStyles.get(uiid);
    }
    
    public String getStyle(String uiid, String name) {
    	if (uicompStyles.containsKey(uiid)) {
    		return uicompStyles.get(uiid).get(name);
    	}
    	return null;
    }
    
    public void addResourceBundle(String bundle) {
    	if (this.resourceBundles == null) {
    		this.resourceBundles = new ArrayList<String>();
    	}
    	this.resourceBundles.add(bundle);
    }
    
    public List<String> getResourceBundle() {
    	return this.resourceBundles;
    }
    
    public void resetRepository()
    {
    	refEntityStack.clear();
    }

    public String getFrameInfo()
    {
        String frameInfo = (String)request.getAttribute("_framePagePrefix");
        if (frameInfo == null)
        {
            String framePrefix = (String)request.getParameter("_framePrefix");
            if (framePrefix != null && !framePrefix.equals("null"))
            {
                frameInfo = framePrefix;
            }
        }
        return frameInfo == null ? "" : frameInfo;
    }

    public void setRequest(HttpServletRequest request)
    {
        this.request = request;
    }

    public HttpServletRequest getRequest()
    {
        return request;
    }
    
    public HttpServletResponse getResponse()
    {
        return response;
    }

    public Writer getOut()
    {
        return out;
    }

    public void setCurrentFormInfo(String formName, String htmlPrefix, String divPrefix)
    {
    	RefEntityInfo info = new RefEntityInfo();
    	info.formName = formName;
    	info.odName = formName;
    	info.htmlPrefix = htmlPrefix;
    	info.divPrefix = divPrefix;
    	
    	this.refEntityStack.push(info);
    }
    
    public void resetCurrentFormInfo()
    {
    	this.refEntityStack.pop();
    }

    public void setIsDataToUI(boolean isDataToUI)
    {
    	this.isDataToUI = isDataToUI;
    }

    public void setReconfigFunction(Map<String, String> reconfigurationMap)
    {
        String key = getHTMLPrefix() + RECONFIGURATION_FUNCTION_KEY;
        this.reconfigurationMap.put(key, reconfigurationMap);
    }

    public String getFormName()
    {
        return this.refEntityStack.peek().formName;
    }

    public String getODMapperName()
    {
        return this.refEntityStack.peek().odName;
    }

    public String getHTMLPrefix()
    {
        return this.refEntityStack.peek().htmlPrefix;
    }

    public String getDIVPrefix()
    {
        return this.refEntityStack.peek().divPrefix;
    }
    
    private Map<String, Object> odMappingData = null;
    
    public void setODMapperData(Map<String, Object> data)
    {
    	odMappingData = data;
    }
    
    public Map<String, Object> getODMapperData()
    {
        return odMappingData;
    }
    
    /**
     * the context value is only available in once.
     * 
     * @param formId
     * @return
     */
    public ODContext getODMapperContext(String formId)
    {
    	for (RefEntityInfo info : this.refEntityStack) {
    		if (info.htmlPrefix.equals(formId)) {
    			return info.evalContext;
    		}
    	}
    	return null;
    }
    
    public void setODMapperContext(String formId, ODContext odContext)
    {
    	for (RefEntityInfo info : this.refEntityStack) {
			if (info.htmlPrefix.equals(formId)) {
				info.evalContext = odContext;
				break;
			}
		}
	}

    public boolean getIsDataToUI()
    {
        return isDataToUI;
    }

    public String getReconfigFunction(String name)
    {
        return getReconfigFunction(getHTMLPrefix(), name);
    }

    public String getReconfigFunction(String prefix, String name)
    {
        String key = prefix + RECONFIGURATION_FUNCTION_KEY;
        Map<String, String> reconfigurationMap = this.reconfigurationMap.get(key);
        return reconfigurationMap == null ? null : reconfigurationMap.get(name);
    }

    public void printHTMLAttributeValues()
    {
		if (logger.isDebugEnabled()) {
			logger.debug("All dynamic attributes: " + this.uicompAttributes.toString());
		}
    }

    public void addJsName(String jsName)
    {
        jsNameSet.add(jsName);
    }

    public boolean containsJsName(String jsName)
    {
        return jsNameSet.contains(jsName);
    }

    public String getImageUrl(String entityName, final String src)
    {
    	String url = src;
        if(url == null) {
        	return "";
        }
        if (url.startsWith("http") || url.startsWith("https")) {
        	return url;
        }
    	if (!url.startsWith(WebConfig.getResourceContextRoot())) {
    		if (url.startsWith("/images")) {
    			return WebConfig.getResourceContextRoot() + url;
    		}
    		return url = WebConfig.getResourceContextRoot() +"/images"+ url;
    	} 
        return url;
    }

    public void generateJS(String value)
    {
		if (jsonStyle) {
			if (pageJs == null) {
				pageJs = new ArrayList<String>();
			}
			pageJs.add(value);
		} else {
			generateHTML(value);
		}
    }

    private void appendHtmlBuffer(String value)
    {
        if (htmlBuffer.get() == null)
        {
            htmlBuffer.set(new StringBuilder(2048));
        }
        htmlBuffer.get().append(value);
    }

    public void generateHTML(String value)
    {
        if (jsonStyle)
        {
            appendHtmlBuffer(value);
        }
        else if (!noResponse)
        {
        	// write out to client directly
        	try
        	{
        		if (value == null) {
        			value = "";
        		}
        		out.write(value);
        		out.flush();
        	}
        	catch (IOException e)
        	{
        		CloseUtil.close(out);
        	}
        }
    }

    public String getJSString()
    {
    	if (pageJs != null) {
        	JSONObject obj = new JSONObject(pageJs);
        	return obj.toString();
        }
        return "";
    }

    public void setJSONStyle(boolean flag)
    {
        jsonStyle = flag;
    }

    public boolean isJSONStyle()
    {
        return jsonStyle;
    }

    /**
     * Only get once from htmlBuffer.
     * 
     * @return
     */
    public String getHtmlString()
    {
    	String s = htmlBuffer.get().toString();
    	// good performance then setLength(0);
    	htmlBuffer.get().delete(0, htmlBuffer.get().length());
        return s;
    }

    public static boolean isInstance(String type, Object o)
    {
		if (type == null || o == null) {
			return false;
		}

		if (o instanceof HTMLReferenceEntityType) {
			return type.equals(((HTMLReferenceEntityType) o).getType());
		} else {
			return true;
		}
    }

    public void setRefEntityMap(Map<String, UIFormObject> refEntityMap)
    {
        this.refEntityMap = refEntityMap;
    }

    public Map<String, UIFormObject> getRefEntityMap()
    {
        return refEntityMap;
    }

    public void setLeftToRight(boolean isLeftToRight)
    {
        this.isLeftToRight = isLeftToRight;
    }

    public boolean isLeftToRight()
    {
        return isLeftToRight;
    }

    public void enterValueMask()
    {
        valueMaskCounter++;
    }

    public void leaveValueMask()
    {
        valueMaskCounter--;
    }

    public boolean isValueMask()
    {
        return valueMaskCounter > 0;
    }

    /**
     * The security controls configured on outside layer entity that covers the one configured on inside layer
     * If the process sequence is from outside to inside, putIfAbsent will be true
     * If the process sequence is from inside to outside(for ajax component), putIfAbsent will be false
     * 
     * @param compId         the widget id the security controls is configured on, stands for the full widget id such as uientity2.uientity1.button
     * @param viewPermission 
     * @param editPermission
     * @param putIfAbsent    
     * 
     */
    public void putCompPermission(String compId, String[] viewPermission, String[] editPermission,
            boolean putIfAbsent)
    {
		if (componentPermissions == null) {
			componentPermissions = new HashMap<String, ComponentPermission>();
		}

		ComponentPermission cp = (ComponentPermission) componentPermissions
				.get(compId);

		if (cp == null) {
			cp = new ComponentPermission(viewPermission, editPermission);
			componentPermissions.put(compId, cp);
		} else {
			if (putIfAbsent) {
				if (cp.getViewPermission().length == 0) {
					cp.setViewPermission(viewPermission);
				}
				if (cp.getEditPermission().length == 0) {
					cp.setEditPermission(editPermission);
				}
			} else {
				cp.setViewPermission(viewPermission);
				cp.setEditPermission(editPermission);
			}
		}
    }

    public ComponentPermission getCompPermission(String compId)
    {
        if (componentPermissions != null)
        {
            return (ComponentPermission)componentPermissions.get(compId);
        }
        else
        {
            return null;
        }
    }
    
    public HTMLWidgetType getHtmlWidget(String uiid) 
    		throws UIComponentNotFoundException {
    	if (currentUIForm == null) {
    		throw new UIComponentNotFoundException("Please set the correspodent UIFormObject for current request context!");
    	}
    	if (currentUIForm.getComponents().containsKey(uiid)) {
    		// page access
			return currentUIForm.getComponents().get(uiid);
		} else if (uiid.startsWith(this.getHTMLPrefix())) {
			// single form access such as new refform.
			return currentUIForm.getComponents().get(uiid.substring(this.getHTMLPrefix().length()));
		} else {
			throw new UIComponentNotFoundException(
					"the component does not exist in the cache. uiid: " + uiid );
		}
	}

	public void setFormObject(UIFormObject uiform) {
		this.currentUIForm = uiform;
	}

	public UIFormObject getFormObject() {
		return this.currentUIForm ;
	}
}
