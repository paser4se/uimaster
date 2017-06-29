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
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.VariableUtil;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.AjaxContextHelper;
import org.shaolin.uimaster.page.DisposableBfString;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.IJSHandlerCollections;
import org.shaolin.uimaster.page.PageDispatcher;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.ajax.json.IDataItem;
import org.shaolin.uimaster.page.cache.ODFormObject;
import org.shaolin.uimaster.page.cache.PageCacheManager;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.exception.AjaxException;
import org.shaolin.uimaster.page.exception.ODException;
import org.shaolin.uimaster.page.od.ODContext;
import org.shaolin.uimaster.page.od.ODEntityContext;
import org.shaolin.uimaster.page.od.ODProcessor;
import org.shaolin.uimaster.page.widgets.HTMLReferenceEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Code Example for Data to UI:
 * <br>
 * <br>HashMap input = new HashMap();
 * <br>input.put("beObject", defaultUser);
 * <br>input.put("editable", new Boolean(true));
 * <br>RefForm form = new RefForm("productInfoForm", "org.shaolin.vogerp.productmodel.form.Product", input);
 * <br>@page.addElement(form);
 * <br><br>
 * Code Example for UI to Data: in fact the input data stores in ref-from object after Data2UI, the 'UI2Data' operation 
 * is beautifully performed in the easiest way after filling the data on UI.
 * <br>
 * <br>RefForm form = (RefForm)@page.getElement(@page.getEntityUiid()); 
 * <br>HashMap input = (HashMap)form.ui2Data();
 * <br>
 * @author wushaol
 */
public class RefForm extends Container implements Serializable
{
    public static final String RECONFIG_OVERRIDE = "ReconfigOverride";

    public static final String RECONFIG_ORIGINAL = "ReconfigOriginal";

	private static final long serialVersionUID = -1744731434666233557L;

    private static Logger logger = LoggerFactory.getLogger(RefForm.class);

    private HTMLReferenceEntityType copy;
    
    private Panel form;

    private String uiid;
    
    private Map functionReconfigurationMap;

    private Map<String, Object> inputParams;

    private ModalWindow window;
    private CallBack callBack;
    
    public RefForm(String uiid, String uiEntityName)
    {
        this(AjaxContextHelper.getAjaxContext().getEntityPrefix() + uiid, uiEntityName, new CellLayout());
        this.uiid = uiid;
        this.setListened(true);
    }

    /**
     *
     * @param uiid
     * @param uiEntityName
     * @param odMapperName
     * @param inputParams
     *
     * data to ui.
     *
     * @throws Exception
     */
    public RefForm(String uiid, String uiEntityName, Map<String, Object> inputParams)
    {
        this(AjaxContextHelper.getAjaxContext().getEntityPrefix() + uiid, uiEntityName, new CellLayout());

        this.inputParams = inputParams;
        this.uiid = uiid;

        this.setListened(true);
    }

    public RefForm(String id, String uiEntityName, Layout layout)
    {
        super(id, layout);
        setUIEntityName(uiEntityName);
    }
    
    public RefForm(String id, String uiEntityName, Layout layout, Map<String, Object> inputParams)
    {
        super(id, layout);
        setUIEntityName(uiEntityName);
        this.inputParams = inputParams;
    }

    public void setCopy(HTMLReferenceEntityType copy) {
    	this.copy = copy;
    }
    
    public HTMLReferenceEntityType getCopy() {
    	return this.copy;
    }
    
    public void setForm(Panel form)
    {
        this.form = form;
    }

    public Panel getForm()
    {
        return form;
    }

    /**
     * @return the functionReconfigurationMap
     */
    public Map getFunctionReconfigurationMap()
    {
        return functionReconfigurationMap;
    }

    /**
     * @param functionReconfigurationMap the functionReconfigurationMap to set
     */
    public void setFunctionReconfigurationMap(Map functionReconfigurationMap)
    {
        this.functionReconfigurationMap = functionReconfigurationMap;
    }

    public void setInputParameter(String key, Object value) {
		if(inputParams != null) 
			inputParams.put(key, value);
	}
    
	public Object getInputParameter(String key) {
		return inputParams != null ? inputParams.get(key) : null;
	}

	public void refresh() {
//		this.closeIfinWindows(true);
//		this.openInWindows(this.window.getTitle(), this.callBack, this.window.getWidth(), this.window.getHeight());
		
		IDataItem dataItem = AjaxContextHelper.createDataItem();
        dataItem.setUiid(this.getId());
        dataItem.setJsHandler(IJSHandlerCollections.FROM_REFRESH);
        dataItem.setJs(this.generateJS());
        dataItem.setData(this.buildUpRefEntity());
        dataItem.setFrameInfo(this.getFrameInfo());
        
        AjaxContext ajaxContext = AjaxContextHelper.getAjaxContext();
        ajaxContext.addDataItem(dataItem);
	}
	
    public Map ui2Data()
    {
    	return ui2Data(this.inputParams);
    }
    
    public Map ui2Data(Map inputParams)
    {
    	UserRequestContext orginalUserContext = UserRequestContext.UserContext.get();
        try {
            if(logger.isDebugEnabled())
            {
                logger.debug("[ui2Data] uientity: "+this.getUIEntityName());
            }
            
            AjaxContext ajaxContext = AjaxContextHelper.getAjaxContext();
            UserRequestContext htmlContext = new UserRequestContext(ajaxContext.getRequest());
            htmlContext.setCurrentFormInfo(this.getUIEntityName(), "", "");
            htmlContext.setIsDataToUI(false);//Don't set prefix in here.
            htmlContext.setAjaxWidgetMap(AjaxContextHelper.getFrameMap(ajaxContext.getRequest()));
            
            ODFormObject odEntityObject = PageCacheManager.getODFormObject(this.getUIEntityName());
            HTMLReferenceEntityType newReferObject = new HTMLReferenceEntityType(this.getId(), this.getUIEntityName());
            inputParams = (inputParams == null)?new HashMap():inputParams;
            inputParams.put(odEntityObject.getUiParamName(), newReferObject);
            htmlContext.setODMapperData(inputParams);
            UserRequestContext.UserContext.set(htmlContext);
            
            ODProcessor processor = new ODProcessor(htmlContext, this.getUIEntityName(), -1);
            ODEntityContext odContext = processor.process();
    		if (ajaxContext.getEventSource() != null) {
    			// this check is supposed to be in UI to Data stage.
    			// check event source whether is valid or not.
    			if (ajaxContext.getEventSource().isReadOnly(odContext)) {
    				ajaxContext.markAsInvalidEventSource();
    				Dialog.showMessageDialog("\u64CD\u4F5C\u65E0\u6548\uFF0C\u8BF7\u5237\u65B0\u9875\u9762\uFF01", "", Dialog.WARNING_MESSAGE, null);
    				throw new ODException("Ajax event source is readonly for secure check.");
    			}
    			if (!ajaxContext.getEventSource().isVisible(odContext)) {
    				ajaxContext.markAsInvalidEventSource();
    				Dialog.showMessageDialog("\u64CD\u4F5C\u65E0\u6548\uFF0C\u8BF7\u5237\u65B0\u9875\u9762\uFF01", "", Dialog.WARNING_MESSAGE, null);
    				throw new ODException("Ajax event source is invisible for secure check.");
    			}
    		}
            
            Map referenceEntityMap = new HashMap();
            htmlContext.setRefEntityMap(referenceEntityMap);
            Map result = htmlContext.getODMapperData();
			if (logger.isDebugEnabled()) {
				if (result != null) {
					logger.debug("OD Mapping Result: " + result.toString());
				} else {
					logger.debug("OD Mapping Result is null!");
				}
			}
            return result;
        }
        catch(Exception ex)
        {
			throw new IllegalStateException("Call UI[" + this.getUIEntityName()
					+ "] to Data error: " + ex.getMessage(), ex);
        } 
        finally 
        {
        	UserRequestContext.UserContext.set(orginalUserContext);
        }
    }

    public String getUiid()
    {
        return this.uiid;
    }

    public String generateJS()
    {
    	StringBuilder sb = DisposableBfString.getBuffer();
    	try {
	        sb.append("defaultname.").append(this.getId());
	        sb.append(" = new ").append(getJsName()).append("('");
	        sb.append(this.getId()).append(".');\n");
	        return sb.toString();
    	} finally {
			DisposableBfString.release(sb);
		}
    }

    private String getJsName()
    {
        return this.getUIEntityName().replace('.', '_');
    }

    public String generateHTML()
    {
        return buildUpRefEntity();
    }

    String buildUpRefEntity()
    {
    	if (this.getId() == null) {
    		throw new IllegalStateException("Please make sure you are using the right refform object!");
    	}
    	UserRequestContext orginalUserContext = UserRequestContext.UserContext.get();
        try
        {
        	AjaxContext ajaxContext = AjaxContextHelper.getAjaxContext();
        	String frameId = AjaxContextHelper.getFrameId(ajaxContext.getRequest());
        	this.setFrameInfo(frameId);
        	
            StringWriter writer = new StringWriter();
            UserRequestContext htmlContext = new UserRequestContext(ajaxContext.getRequest(), writer);
            htmlContext.setIsDataToUI(true);
            htmlContext.setCurrentFormInfo(this.getUIEntityName(), "", "");
            
            UIFormObject entity = HTMLUtil.parseUIForm(this.getUIEntityName());
            ODFormObject odEntityObject = PageCacheManager.getODFormObject(this.getUIEntityName());
            HTMLReferenceEntityType newReferObject = new HTMLReferenceEntityType(this.getId(), this.getUIEntityName());
            if (inputParams == null)
                inputParams = new HashMap();
            inputParams.put(odEntityObject.getUiParamName(), newReferObject);
            Map internalInputParams = odEntityObject.getLocalEContext().getVariableObjects();
            Iterator iter = internalInputParams.keySet().iterator();
            while (iter.hasNext()) {
            	String varName = (String)iter.next();
            	if (!inputParams.containsKey(varName)) {
            		inputParams.put(varName, internalInputParams.get(varName));
            	}
            }
            
            htmlContext.setFormObject(entity);
            htmlContext.setODMapperData(inputParams);
			Map<String, JSONObject> ajaxMap = AjaxContextHelper.getFrameMap(ajaxContext.getRequest());
			htmlContext.setAjaxWidgetMap(ajaxMap);
			UserRequestContext.UserContext.set(htmlContext);
            
            callODMapper(htmlContext, this.getUIEntityName());
            
            inputParams.remove(odEntityObject.getUiParamName());// cannot be serializable!
            htmlContext.setReconfigFunction(functionReconfigurationMap);
            htmlContext.setRefEntityMap(new HashMap());
            htmlContext.printHTMLAttributeValues();

            String oldFrameInfo = (String)htmlContext.getRequest().getAttribute("_framePagePrefix");
            htmlContext.getRequest().setAttribute("_framePagePrefix", this.getFrameInfo());
            
            //for a RefEntity uientity2.uientity1, firstly, 
            //call AjaxComponentSecurityUtil.loadSecurityMap to load all the security controls configured on higher level(in this case, uientity2, uipage)
            //in this method, the process sequence is from inside to outside
            //then call HTMLUIEntity.parse to process the security controls configured on lower level(in this case, security controls configured within uientity1)
            //in this method, the process sequence is from outside to inside
            
            OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
            DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
            Iterator<Map.Entry<String, Object>> i = inputParams.entrySet().iterator();
            while (i.hasNext()) {
            	Map.Entry<String, Object> entry = i.next();
            	evaContext.setVariableValue(entry.getKey(), entry.getValue());
            }
            evaContext.setVariableValue("page", AjaxContextHelper.getAjaxContext());
            ooeeContext.setDefaultEvaluationContext(evaContext);
            ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
            
            HTMLReferenceEntityType refEntity = new HTMLReferenceEntityType(this.getId());
            if (inputParams.get(RECONFIG_ORIGINAL) != null && inputParams.get(RECONFIG_OVERRIDE) != null) {
            	refEntity.addFunctionReconfiguration(inputParams.get(RECONFIG_ORIGINAL).toString(), 
            			inputParams.get(RECONFIG_OVERRIDE).toString());
            }
            refEntity.generateBeginHTML(htmlContext, entity, 0);
            
            PageDispatcher dispatcher = new PageDispatcher(entity, ooeeContext);
            dispatcher.forwardForm(htmlContext, 0, isReadOnly(), refEntity);
            htmlContext.getRequest().setAttribute("_framePagePrefix", oldFrameInfo);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Created UI ajax widgets.");
                for(Map.Entry<String, JSONObject> entry : htmlContext.getPageAjaxWidgets().entrySet())
                {
                    logger.debug("Ajax widget: "+ entry.getValue().toString());
                }
            }
            AjaxContextHelper.updateFrameMap(ajaxContext.getRequest(), htmlContext.getPageAjaxWidgets());
            
            // append the dynamic js files.
            StringWriter jswriter = new StringWriter();
            UserRequestContext jsContext = new UserRequestContext(ajaxContext.getRequest(), jswriter);
        	UIFormObject formObject = PageCacheManager.getUIFormObject(this.getUIEntityName());
        	formObject.getJSPathSet(jsContext, Collections.emptyMap(), true);
        	String data = jswriter.getBuffer().toString();
        	IDataItem dataItem = AjaxContextHelper.createLoadJS(getId(), data);
            dataItem.setFrameInfo(getFrameInfo());
            ajaxContext.addDataItem(dataItem);
            htmlContext.resetCurrentFormInfo();
            
            return writer.getBuffer().toString();
        }
        catch (Exception ex)
        {
            logger.error("Error occurred while building the UI form structure of entity: " + uiid, ex);
            return "<div>Error occurred while building the UI form structure of entity: " + uiid + "</div>";
        }
        finally 
        {
        	UserRequestContext.UserContext.set(orginalUserContext);
        }
    }
    
    public void callODMapper(UserRequestContext htmlContext, String odmapperName) throws ODException
    {
        if (logger.isDebugEnabled())
            logger.debug("callODMapper odmapper name: " + odmapperName);
        
        ODProcessor processor = new ODProcessor(htmlContext, odmapperName, -1);
        processor.process();
    }
    
    /**
     * This is a very interesting API design for flying a form over the parent page.
     * 
     * @param title required
     * @param callBack optional when the window is closed.
     */
    public void openInWindows(String title, CallBack callBack) {
    	openInWindows(title, callBack, -1, -1, false);
    }
    
    public void openInWindows(String title, CallBack callBack, boolean autoResize) {
    	openInWindows(title, callBack, 0, 0, false, autoResize);
    }
    
    public void openInWindows(String title, CallBack callBack, int width, int height) {
    	openInWindows(title, callBack, width, height, false);
    }
    
    public void openInWindows(String title, CallBack callBack, int width, int height, boolean showCloseBtn) {
    	openInWindows(title, callBack, width, height, showCloseBtn, false);
    }
    
    public void openInWindows(String title, CallBack callBack, int width, int height, boolean showCloseBtn, boolean autoResize) {
    	window = new ModalWindow(this.getUiid() + "-Dialog", title, this);
        window.setFixable(true);
        window.setShowCloseBtn(showCloseBtn);
        window.setAutoResize(autoResize);
        if (width > 0) {
        	window.setBounds(-1, -1, width, height);
        }
        window.open();
        
        this.callBack = callBack;
    }
    
    public void addWindowsClosedCallBack(CallBack caller) {
    	//TODO:
    }
    
    public void closeIfinWindows(Object... obj) {
    	if (window != null) {
			window.close();
            this.remove();
			if (callBack != null) {
				callBack.execute(obj);
			}
			callBack = null;
		}
		
    }
    
	public void closeIfinWindows() {
		if (window != null) {
			window.close();
            this.remove();
			if (callBack != null) {
				callBack.execute();
			}
			callBack = null;
		}
	}
	
	public void closeIfinWindows(boolean skipCallBack) {
		if (window != null) {
			window.close();
            this.remove();
			if (!skipCallBack) {
				if (callBack != null) {
					callBack.execute();
				}
			}
			callBack = null;
		}
		
	}
	
	public boolean isInWindows() {
		return window != null;
	}
    
    public RefForm remove() 
    {
        AjaxContext ajaxContext = AjaxContextHelper.getAjaxContext();
        if(ajaxContext == null)
            return this;
        if(ajaxContext.existElmByAbsoluteId(getId(), getFrameInfo()))
        {
			try {
				Map<String, JSONObject> map = AjaxContextHelper.getFrameMap(ajaxContext.getRequest());
				Iterator<Entry<String, JSONObject>> iterator = map.entrySet().iterator();
				while(iterator.hasNext())
				{
					Map.Entry<String, JSONObject> entry = iterator.next();
					String uiid = (String) entry.getKey();
					if(uiid.startsWith(this.getId()+"."))
					{
						if(logger.isDebugEnabled())
							logger.debug("Remove component["+uiid+"] in cache of ui map.");
						iterator.remove();
					}
				}
				ajaxContext.removeElement(getId(), getFrameInfo());
				AjaxContextHelper.updateFrameMap(ajaxContext.getRequest(), map);
				
				IDataItem dataItem = AjaxContextHelper.createRemoveItem("", getId());
				dataItem.setFrameInfo(getFrameInfo());
				ajaxContext.addDataItem(dataItem);
			} catch (AjaxException e) {
				logger.warn("Session maybe timeout: " + e.getMessage(), e);
				return this;
			}
        }
        return this;
    }
    
    public JSONObject toJSON() throws JSONException {
    	JSONObject json = super.toJSON();
    	if (this.copy != null) {
    		json.put("refid", this.copy.getUIID());
    		json.put("refcopy", this.copy.getType());
    	} else {
    		json.put("refid", this.getId());
    		json.put("refcopy", this.getUIEntityName());
    	}
    	if (this.inputParams != null) {
    		JSONObject inputValues = VariableUtil.convertVarToJson((HashMap)this.inputParams);
    		inputValues.remove("UIEntity");
    		json.put("inputParams", inputValues);
    	}
    	if (this.window != null) {
    		json.put("hasWindow", 1);
    	}
    	if (this.callBack != null) {
    		json.put("cbclass", callBack.getClass().getName());
    		json.put("callBack", callBack.toJSON());
		}
    	return json;
    }
    
    public void fromJSON(JSONObject json) throws Exception {
    	super.fromJSON(json);
    	this.setUIEntityName(json.getString("entity"));
    	this.copy = new HTMLReferenceEntityType(json.getString("refid"), json.getString("refcopy"));
    	if (json.has("inputParams")) {
    		this.inputParams = VariableUtil.convertJsonToVar(json.getJSONObject("inputParams"));
    	}
    	if (json.has("hasWindow")) {
    		this.window = new ModalWindow(this.getUiid() + "-Dialog", "", this);
    		this.window.setId(this.getUiid() + "-Dialog");//bug fix.
			if (json.has("callBack")) {
				callBack = (CallBack)Class.forName(json.getString("cbclass")).newInstance();
				callBack.fromJSON(json.getJSONObject("callBack"));
			}
		}
    }
    
    public static HTMLReferenceEntityType getCopyType(final JSONObject json) throws JSONException {
    	HTMLReferenceEntityType copy = new HTMLReferenceEntityType(json.getString("refid"), json.getString("refcopy"));
    	return copy;
    }
    
}
