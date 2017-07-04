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

import java.awt.ComponentOrientation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shaolin.bmdp.i18n.ResourceUtil;
import org.shaolin.bmdp.json.JSONArray;
import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.EvaluationContext;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.uimaster.page.ajax.AFile;
import org.shaolin.uimaster.page.ajax.AList;
import org.shaolin.uimaster.page.ajax.Button;
import org.shaolin.uimaster.page.ajax.CheckBox;
import org.shaolin.uimaster.page.ajax.CheckBoxGroup;
import org.shaolin.uimaster.page.ajax.ComboBox;
import org.shaolin.uimaster.page.ajax.Hidden;
import org.shaolin.uimaster.page.ajax.Image;
import org.shaolin.uimaster.page.ajax.Label;
import org.shaolin.uimaster.page.ajax.Link;
import org.shaolin.uimaster.page.ajax.Matrix;
import org.shaolin.uimaster.page.ajax.Panel;
import org.shaolin.uimaster.page.ajax.PasswordField;
import org.shaolin.uimaster.page.ajax.RadioButton;
import org.shaolin.uimaster.page.ajax.RadioButtonGroup;
import org.shaolin.uimaster.page.ajax.RefForm;
import org.shaolin.uimaster.page.ajax.Table;
import org.shaolin.uimaster.page.ajax.TextArea;
import org.shaolin.uimaster.page.ajax.TextField;
import org.shaolin.uimaster.page.ajax.Tree;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.ajax.handlers.EventHandler;
import org.shaolin.uimaster.page.ajax.json.DataItem;
import org.shaolin.uimaster.page.ajax.json.IDataItem;
import org.shaolin.uimaster.page.ajax.json.IRequestData;
import org.shaolin.uimaster.page.ajax.json.RequestData;
import org.shaolin.uimaster.page.exception.AjaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ajax Context for the whole page operations. It has initialized in HTMLUIEntity.getAjaxContext
 * 
 * @author swu
 */
public class AjaxContext extends TransOpsExecuteContext implements Serializable
{
    private static final long serialVersionUID = -1744731434567233557L;

    private static final Logger logger = LoggerFactory.getLogger(AjaxContext.class);

    /**
     * what is object of all comps of the current page.
     */
    public static final String AJAX_COMP_MAP = "_ajax_page_component_map";

    public static final String GLOBAL_PAGE = "#GLOBAL#";
    
    public static final String ALL_CACHED_PAGES = "_allpages";
    
    /**
     * request action data set.
     */
    public static final String AJAX_USER_EVENT = "_ajaxUserEvent";

    public static final String AJAX_UIID = "_uiid";

    public static final String AJAX_DATA = "_data";

    public static final String AJAX_ACTION_PAGE = "_actionPage";

    public static final String AJAX_FRAME_PREFIX = "_framePrefix";

    public static final String AJAX_BA = "_baName";

    /**
     * for response action name.
     */
    public static final String AJAX_ACTION_TYPE = "_actionType";

    /**
     * for the current uientity/uipage variables of 'Variable' node.
     */
    public static final String CURRENT_ACCESS_VARIABLE = "__CURRENT_ACCESS_VARIABLE_";

    private transient HttpServletRequest request;
    
    private transient HttpServletResponse response;
    /**
     * what is this operation corresponds to the entity.
     */
    private String entityUiid = "";

    private String entityPrefix = "";

    private boolean isLTR = true;

    //TODO: Whether the current page can be edited. support for security.
    private boolean pageEditableFlag = true;

    /**
     * UI JSON Model which is a user session scope.
     */
    private final Map<String, JSONObject> ajaxJsonMap;
    
    /**
     * UI Widget Model which lazy created for every ajax operation!
     */
    private final Map<String, Widget> ajaxWidgetMap = new HashMap<String, Widget>();
    
    
    /**
     * request parameters.
     */
    private final IRequestData requestData;
    
    private Button eventSource;

    private boolean illegalEventSource = false;
    
    /**
     * Linear array list.
     */
    private final ArrayList<JSONObject> dataItems;
    
    /**
     * only for serialization support.
     */
    public AjaxContext() {
    	this.ajaxJsonMap = null;
        this.dataItems = null;
        this.requestData = null;
    }
    
    public AjaxContext(final Map<String, JSONObject> uiMap, final IRequestData requestData)
    {
        this.ajaxJsonMap = uiMap;
        this.dataItems = new ArrayList<JSONObject>();
        this.requestData = requestData;
        
        if (requestData != null) {
	        this.entityUiid = requestData.getEntityUiid();
	        if (entityUiid != null && entityUiid.length() > 0)
	            this.entityPrefix = entityUiid + ".";
        }
    }

    public void addAJAXComponent(String compID, Widget component) throws JSONException
    {
		ajaxWidgetMap.put(compID, component);
		ajaxJsonMap.put(compID, component.toJSON());
    }

    public Widget getAJAXComponent(String compID)
    {
        return (Widget)ajaxWidgetMap.get(compID);
    }

    public static void registerPageAjaxContext(String pageName, HttpServletRequest request)
    {
        try
        {
            Map<String, JSONObject> uiMap = AjaxContextHelper.getFrameMap(request);
            IRequestData requestData = new RequestData();
            requestData.setEntityName(pageName);
            requestData.setFrameId(request.getParameter("_framePrefix"));
            requestData.setEntityUiid("");
            requestData.setUiid("");
            AjaxContext context = new AjaxContext(uiMap, requestData);
            context.initData();
            context.setRequest(request, null);
            AjaxContextHelper.createAjaxContext(context);
        }
        catch (Exception e)
        {
            logger.warn("Fail to register Page Ajax Context: " + e.getMessage(), e);
        }
    }
    
    public static void registerPageAjaxContext(String pageName, Map<String, JSONObject> uiMap, HttpServletRequest request)
    {
        try
        {
            IRequestData requestData = new RequestData();
            requestData.setEntityName(pageName);
            requestData.setFrameId(request.getParameter("_framePrefix"));
            requestData.setEntityUiid("");
            requestData.setUiid("");
            AjaxContext context = new AjaxContext(uiMap, requestData);
            context.initData();
            context.setRequest(request, null);
            AjaxContextHelper.createAjaxContext(context);
        }
        catch (EvaluationException e) {
            logger.warn("Fail to register Page Ajax Context: " + e.getMessage(), e);
        }
    }

    public void removePageAjaxContext()
    {
        AjaxContextHelper.removeAjaxContext();
    }

    public HttpServletRequest getRequest()
    {
        return request;
    }

    public HttpServletResponse getResponse()
    {
        return response;
    }
    
    public void initData() throws EvaluationException
    {
        DefaultEvaluationContext globalEContext = new DefaultEvaluationContext();
        Map accessibleVars = (Map)ajaxJsonMap
                .get(AjaxContext.CURRENT_ACCESS_VARIABLE + this.entityPrefix);
        if (accessibleVars != null && !accessibleVars.isEmpty())
        {
            Iterator iterator = accessibleVars.entrySet().iterator();
            while (iterator.hasNext())
            {
                Map.Entry entry = (Map.Entry)iterator.next();
                globalEContext.setVariableValue((String)entry.getKey(), entry.getValue());
            }
        }
        this.setEvaluationContextObject("@", globalEContext);
        globalEContext.setVariableValue("request", request);
        globalEContext.setVariableValue("page", this);
    }

    public void setRequest(HttpServletRequest request, HttpServletResponse response) throws EvaluationException
    {
        setHttpRequest(request);
        this.response = response;

        DefaultEvaluationContext globalEContext = (DefaultEvaluationContext)this
                .getEvaluationContextObject("@");
        globalEContext.setVariableValue("request", request);
    }

    public void setHttpRequest(HttpServletRequest request)
    {
        this.request = request;
        this.setLTR(request);
    }

    private void setLTR(HttpServletRequest request)
    {
        String userLocale = WebConfig.getUserLocale(request);
        ComponentOrientation ce = ComponentOrientation.getOrientation(
        		ResourceUtil.getLocaleObject(userLocale));
        isLTR = ce.isLeftToRight();
    }

    public IRequestData getRequestData()
    {
        return this.requestData;
    }

    public String getParameter(String key)
    {
        return (String)this.requestData.getData().get(key);
    }

    public String getFrameId() {
    	return request.getParameter("_framePrefix");
    }
    
    public int itemSize()
    {
        return dataItems.size();
    }
    
    /**
     * customized data item.
     * 
     * @param dataItem
     */
    public void addDataItem(IDataItem dataItem)
    {
        dataItems.add( new JSONObject(dataItem) );
    }

    public void importAnother(AjaxContext another)
    {
    	if (another.dataItems != null) {
    		dataItems.addAll(another.dataItems);
    	}
    }
    
    public void invokeEventHander(String actionName) throws Exception {
    	EventHandler handler = new EventHandler();
    	handler.trigger0(this, actionName);
    }
    
    public void executeJavaScript(String script)
    {
    	if (script != null && script.length() > 0) 
    	{
    		executeJavaScript(script, requestData.getFrameId());
    	}
    }

    public void executeJavaScript(String script, String frameInfo)
    {
        IDataItem item = new DataItem();
        item.setJsHandler(IJSHandlerCollections.JAVASCRIPT);
        item.setJs(script);
        item.setFrameInfo(frameInfo);
        dataItems.add(new JSONObject(item));
    }

    /**
     * This method not only generate JSON, also clear all the items.
     * @throws AjaxException 
     */
    public String getDataAsJSON() throws Exception
    {
    	Map<String, JSONObject> ajaxMap = AjaxContextHelper.getFrameMap(this.getRequest());
    	// sync json model.
    	for (Map.Entry<String, Widget> entries: ajaxWidgetMap.entrySet()) {
    		ajaxMap.put(entries.getKey(), entries.getValue().toJSON());
    	}
    	ajaxWidgetMap.clear();
    	
    	AjaxContextHelper.updateFrameMap(this.getRequest(), ajaxMap);
    	
        JSONArray json = new JSONArray();
        for (int i = 0; i < dataItems.size(); i++)
        {
            json.put(dataItems.get(i));
        }
        
        dataItems.clear();
        return json.toString();
    }

    /**
     * @throws EvaluationException
     */
    public void synchVariables() throws EvaluationException
    {
        Map accessibleVars = (Map)ajaxJsonMap
                .get(AjaxContext.CURRENT_ACCESS_VARIABLE + this.entityPrefix);
        if (accessibleVars != null && !accessibleVars.isEmpty())
        {
            EvaluationContext eContext = this.getEvaluationContextObject("@");
            Iterator iterator = accessibleVars.keySet().iterator();
            while (iterator.hasNext())
            {
                String key = (String)iterator.next();
                accessibleVars.put(key, eContext.getVariableValue(key));
            }
        }
    }

    public String getEntityPrefix()
    {
        return entityPrefix;
    }

    public String getEntityUiid()
    {
        return entityUiid;
    }

    public String getEntityName()
    {
        return requestData.getEntityName();
    }
    
    public void setEventSource(Button button) {
    	this.eventSource = button;
    }
    
    public Button getEventSource() {
    	return this.eventSource;
    }

    public void markAsInvalidEventSource() {
    	this.illegalEventSource = true;
    }
    
    public boolean isInvalidEventSource() {
    	return this.illegalEventSource;
    }
    
    /**
     * by relative id adjust whether is existed this component in the component tree.
     * 
     * @param uiid
     * @return
     */
    public boolean existElement(String uiid)
    {
    	if (uiid == null) {
    		return false;
    	}
    	if (uiid.equals(this.getEntityUiid())) {
    		return ajaxJsonMap.containsKey(uiid);
    	}
    	String realUiid = uiid;
    	if (this.entityPrefix != null && !this.entityPrefix.isEmpty()) {
    		if (!uiid.startsWith(this.entityPrefix)) {
    			realUiid = this.entityPrefix + uiid;
    		}
    	}
        return ajaxJsonMap.containsKey(realUiid);
    }

    /**
     * by absolute id adjust whether is existed this component in the component tree, also input
     * instance id equals that instance id of the uiMap.
     * 
     * @param comp
     * @return uiMap.containsKey(comp.getId()) && comp == uiMap.get(comp.getId())
     * @throws JSONException 
     */
    public boolean existElement(Widget<?> comp)
    {
    	if (comp == null) {
    		return false;
    	}
//    	if (eventSourceUIMap.containsKey(comp.getId())) {
//	        try {
//				return comp.getClass().getSimpleName().equals(eventSourceUIMap.get(comp.getId()).getString("type"));
//			} catch (JSONException e) {
//				return false;
//			}
//    	}
    	return ajaxJsonMap.containsKey(comp.getId());
    }

    public boolean existElmByAbstId(String absoluteUiid)
    {
        return existElmByAbsoluteId(absoluteUiid, this.getFrameId());
    }
    
    public boolean existElmByAbstId(String absoluteUiid, String frameName)
    {
        return existElmByAbsoluteId(absoluteUiid, frameName);
    }
    
    /**
     * by absolute id adjust whether is existed this component in the component tree.
     * 
     * @param absoluteUiid
     * @param frameName
     * @return
     */
    public boolean existElmByAbsoluteId(final String absoluteUiid, final String frameName)
    {
		return ajaxJsonMap.containsKey(absoluteUiid);
    }

    /**
     * get element by id.
     * 
     * @param uiid
     * @return Component
     * @throws JSONException 
     */
    public Widget getElement(String uiid)
    {
    	if (uiid == null) {
    		throw new IllegalArgumentException("The ui widget id can be null.");
    	}
    	String realUiid = uiid;
    	if (!uiid.equals(this.getEntityUiid())) {
    		if (this.entityPrefix != null && !this.entityPrefix.isEmpty()) {
    			if (!uiid.startsWith(this.entityPrefix)) {
    				realUiid = this.entityPrefix + uiid;
    			}
    		}
    	}
        JSONObject comp = ajaxJsonMap.get(realUiid);
        if (comp == null) {
            logger.debug("The ui widget can not be found by this " + realUiid + " id. try to use the original id: "+uiid);
            comp = ajaxJsonMap.get(uiid);
            if (comp == null) {
            	logger.warn("The ui widget can not be found by either " + realUiid + " or "+ uiid);
            }
        }
        if (comp != null) {
        	try {
	        	String id = comp.getString("uiid");
				if (!ajaxWidgetMap.containsKey(id)) {
        			ajaxWidgetMap.put(id, Widget.covertFromJSON(comp));
	        	}
	        	return ajaxWidgetMap.get(id);
        	} catch (Exception e) {
        		throw new IllegalArgumentException("The ui widget can not be created by " + comp, e);
        	}
        }
        throw new IllegalArgumentException("The ui widget can not be created by " + realUiid);
    }

    public Widget getElementByAbsoluteId(String absoluteUiid)
    {
    	if (!ajaxJsonMap.containsKey(absoluteUiid)) {
    		logger.warn("The ui widget can not be found by absolute id: " + absoluteUiid);
    		return null;
    	}
		if (!ajaxWidgetMap.containsKey(absoluteUiid)) {
    		try {
    			ajaxWidgetMap.put(absoluteUiid, Widget.covertFromJSON(ajaxJsonMap.get(absoluteUiid)));
			} catch (Exception e) {
				logger.warn("The ui widget can not be created by " + ajaxJsonMap.get(absoluteUiid));
			}
    	}
    	return ajaxWidgetMap.get(absoluteUiid);
    }
    
    public boolean addElement(Widget comp)
    {
        try {
			addAJAXComponent(comp.getId(), comp);
		} catch (JSONException e) {
			//must not have any exception.
			logger.warn(e.getMessage(), e);
		}
        return true;
    }

    public Widget removeElement(String uiid, String frameId)
    {
    	return removeElement(uiid);
    }
    public Widget removeElement(String uiid)
    {
    	if (uiid.equals(this.getEntityUiid())) {
    		ajaxJsonMap.remove(uiid);
    		return (Widget)ajaxWidgetMap.remove(uiid);
    	}
    	String realUiid = uiid;
    	if (this.entityPrefix != null && !this.entityPrefix.isEmpty()) {
    		if (!uiid.startsWith(this.entityPrefix)) {
    			realUiid = this.entityPrefix + uiid;
    		}
    	}
    	if (ajaxJsonMap.containsKey(realUiid)) {
	    	ajaxJsonMap.remove(realUiid);
	        return (Widget)ajaxWidgetMap.remove(realUiid);
    	}
    	// final check
        if (ajaxJsonMap.containsKey(uiid)) { 
    		ajaxJsonMap.remove(uiid);
            return (Widget)ajaxWidgetMap.remove(uiid);
    	}
        return null;
    }

    public void removeFramePage(String frameId) {
    	logger.info("Close the frame page: " + frameId);
    	Map ajaxComponentMap = AjaxContextHelper.getAjaxWidgetMap(request.getSession());
    	ajaxComponentMap.remove(frameId);
    	AjaxContextHelper.removeCachedPage(request.getSession(), frameId);
    }
    
    public RefForm removeForm(String uiid) {
    	return (RefForm)removeElement(uiid);
    }
    
    public RefForm getRefForm(String uiid)
    {
        return (RefForm)this.getElement(uiid);
    }
    
    public TextField getTextField(String uiid)
    {
    	return (TextField)this.getElement(uiid);
    }
	
    public TextArea getTextArea(String uiid)
    {
        return (TextArea)this.getElement(uiid);
    }
    
    public Table getTable(String uiid) 
    {
    	return (Table)this.getElement(uiid);
    }
    
    public Button getButton(String uiid)
    {
        return (Button)this.getElement(uiid);
    }
    
    public PasswordField getPasswordField(String uiid)
    {
        return (PasswordField)this.getElement(uiid);
    }
    
    public Hidden getHidden(String uiid)
    {
        return (Hidden)this.getElement(uiid);
    }
    
    public Label getLabel(String uiid)
    {
        return (Label)this.getElement(uiid);
    }
    
    public Link getLink(String uiid)
    {
        return (Link)this.getElement(uiid);
    }
    
    public AFile getFile(String uiid)
    {
        return (AFile)this.getElement(uiid);
    }
    
    public Image getImage(String uiid)
    {
        return (Image)this.getElement(uiid);
    }
    
    public CheckBox getCheckBox(String uiid)
    {
        return (CheckBox)this.getElement(uiid);
    }
    
    public RadioButton getRadioButton(String uiid)
    {
        return (RadioButton)this.getElement(uiid);
    }
    
    public ComboBox getComboBox(String uiid)
    {
        return (ComboBox)this.getElement(uiid);
    }
    
    public RadioButtonGroup getRadioBtnGroup(String uiid)
    {
        return (RadioButtonGroup)this.getElement(uiid);
    }
    
    public AList getList(String uiid)
    {
        return (AList)this.getElement(uiid);
    }
    
    public Matrix getMatrix(String uiid)
    {
        return (Matrix)this.getElement(uiid);
    }
    
    public CheckBoxGroup getCheckBoxGroup(String uiid)
    {
        return (CheckBoxGroup)this.getElement(uiid);
    }
    
    public Panel getPanel(String uiid)
    {
        return (Panel)this.getElement(uiid);
    }
    
    public Tree getTree(String uiid)
    {
        return (Tree)this.getElement(uiid);
    }
    
    public void log(String message) {
    	logger.info(message);
    }
    
    public boolean isLTR()
    {
        return isLTR;
    }

    public void printUiMap()
    {
    }

    public Map getVariables(String entityPrefix)
    {
        if (entityPrefix == null)
        {
            entityPrefix = "";
        }
        return (Map)ajaxJsonMap.get(AjaxContext.CURRENT_ACCESS_VARIABLE + entityPrefix);
    }

    /**
     * @return if true means that the current page can be edited by the current user
     */
    public boolean isPageEditable()
    {
        return pageEditableFlag;
    }
    
    public void addTempUserData(String key, Object value) {
		Object v = request.getSession().getAttribute("_TempUserData");
		if (v != null) {
			 ((HashMap)v).put(key, value);
		} else {
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put(key, value);
			request.getSession().setAttribute("_TempUserData", data);
		}
	}

	public Object getTempUserData(String key) {
		Object v = request.getSession().getAttribute("_TempUserData");
		if (v != null) {
			return ((HashMap)v).get(key);
		}
		return null;
	}
	
	public Object removeTempUserData(String key) {
		Object v = request.getSession().getAttribute("_TempUserData");
		if (v != null) {
			return ((HashMap)v).get(key);
		}
		return null;
	}
	
	public void loadJs(final String js) {
		String resource = UserContext.isAppClient()? WebConfig.getAppContextRoot(this.getRequest()) + js : WebConfig.getWebContextRoot() + js;
		IDataItem dataItem = AjaxContextHelper.createLoadJS("", resource);
        dataItem.setFrameInfo(this.getFrameId());
        this.addDataItem(dataItem);
	}
}
