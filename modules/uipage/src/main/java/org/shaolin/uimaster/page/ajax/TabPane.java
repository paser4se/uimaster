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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.datamodel.page.SimpleComponentMappingType;
import org.shaolin.bmdp.datamodel.page.TableLayoutConstraintType;
import org.shaolin.bmdp.datamodel.page.UIFrameType;
import org.shaolin.bmdp.datamodel.page.UIReferenceEntityType;
import org.shaolin.bmdp.datamodel.page.UITabPaneItemType;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.EvaluationContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.javacc.exception.ParsingException;
import org.shaolin.uimaster.html.layout.HTMLPanelLayout;
import org.shaolin.uimaster.page.AjaxActionHelper;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.DisposableBfString;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.PageDispatcher;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.ajax.json.IDataItem;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.exception.AjaxException;
import org.shaolin.uimaster.page.exception.UIPageException;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.shaolin.uimaster.page.od.ODContext;
import org.shaolin.uimaster.page.od.ODEntityContext;
import org.shaolin.uimaster.page.od.ODTabPaneContext;
import org.shaolin.uimaster.page.od.mappings.SimpleComponentMapping;
import org.shaolin.uimaster.page.spi.IJsGenerator;
import org.shaolin.uimaster.page.widgets.HTMLCellLayoutType;
import org.shaolin.uimaster.page.widgets.HTMLDynamicUIItem;
import org.shaolin.uimaster.page.widgets.HTMLFrameType;
import org.shaolin.uimaster.page.widgets.HTMLPanelType;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example:
 *  <br>
 *  <br>TabPane taPane = (TabPane)@page.getElement("taPane");
 *  <br>if(taPane == null)
 *  <br>{
 *  <br>    taPane = new TabPane("taPane");
 *  <br>    Panel rootPanel = (Panel)@page.getElement("Form");
 *  <br>    rootPanel.append(taPane);
 *  <br>}
 *
 */
public class TabPane extends Container implements Serializable 
{
	private static final Logger logger = LoggerFactory.getLogger(TabPane.class);
    private static final long serialVersionUID = -1744731434666233557L;
    private static final String CMD_ADDTAB = "addTab";
    private static final String CMD_REMOVETAB = "removeTab";
    private static final String CMD_SETBODY = "setBody";
    private static final String CMD_SETTITLE = "setTitle";
    private static final String CMD_SETSELECTEDINDEX = "setSelectedIndex";
    private static final String HANDLERNAME = "tabPaneHandler";
    //List of titles(String)
    private List titles=new ArrayList();
    //List of entities(RefEntity)
    private List entities=new ArrayList();
    //
    private Map<Integer, Boolean> loadedTabs = new HashMap<Integer, Boolean>();
    {
    	loadedTabs.put(0, true);
    }
    /**
     * ajax loading objects.
     */
    private List<UITabPaneItemType> tabs;
    private UIFormObject ownerEntity;
    private OOEEContext evalContext;
    // clean the ajax loading objects if all tabs loaded.
    private AtomicInteger accessedIndex = new AtomicInteger();
    
    private ExpressionType selectedAction;
    private String originalUIID;
    private String uiid;
    private int selectedIndex;
    
    private boolean ajaxLoad;
    
    /**
     * 
     * @param uiid 
     * @param titles List of titles(String)
     * @param entities List of bodies(String)
     */
    public TabPane(String uiid)
    {
        this(uiid, new CellLayout());
    }
    
    public TabPane(String id, Layout layout)
    {
        super(AjaxActionHelper.getAjaxContext().getEntityPrefix() +id, layout);
        this.setListened(true);
        this.uiid = this.getId();
    }
    
    public void setOriginalUIID(String uiid){
    	this.originalUIID = uiid;
    }
    
    public TabPane addAttribute(String name, Object value, boolean update)
    {
        if(name == null || name.length() == 0)
        {
            return this;
        }
        if(name.equals("selectedIndex"))
        {
            selectedIndex = Integer.valueOf(value.toString()).intValue();
        }
        return this;
    }
    
    /**
     * for ui html type!
     * 
     * @param id
     * @param selectedIndex
     * @param layout
     */
    public TabPane(String id, List<UITabPaneItemType> tabs, int selectedIndex, Layout layout)
    {
        super(id, layout);
        this.setListened(true);
        this.tabs = tabs;
        this.selectedIndex = selectedIndex;
        this.uiid = this.getId();
    }
    
    public void setAjaxLoad(boolean ajaxLoad) {
    	this.ajaxLoad = ajaxLoad;
    }
    
    public void setSelectedAction(ExpressionType selectedAction) {
    	this.selectedAction = selectedAction;
    }

    public void setOwnerEntity(UIFormObject ownerEntity) {
    	this.ownerEntity = ownerEntity;
    }
    
    public void setODMapperContext(VariableEvaluator ee) {
    	EvaluationContext cloneContext = null;
    	EvaluationContext eContext = ee.getExpressionContext();
    	if (eContext instanceof ODContext) {
    		DefaultEvaluationContext localContext = (DefaultEvaluationContext)
    					((ODContext)eContext).getEvaluationContextObject("$");
    		if (localContext.getVariableObjects() != null) {
    			cloneContext = new DefaultEvaluationContext(new HashMap(localContext.getVariableObjects()));
    		}
		} else if (eContext instanceof DefaultEvaluationContext) {
			DefaultEvaluationContext localContext = (DefaultEvaluationContext)eContext;
			if (localContext.getVariableObjects() != null) {
				cloneContext = new DefaultEvaluationContext(new HashMap(localContext.getVariableObjects()));
			}
		} else {
			cloneContext = eContext;
		}
    	if (cloneContext == null) {
    		cloneContext = new DefaultEvaluationContext();
    	}
    	OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
    	ooeeContext.setDefaultEvaluationContext(cloneContext);
    	ooeeContext.setEvaluationContextObject("$", cloneContext);
    	this.evalContext = ooeeContext;
    }
    
    public String generateHTML()
    {
    	StringBuilder html = DisposableBfString.getBuffer();
    	try {
	        generateWidget(html);
	
	        //Generate the tab
	        html.append("<div id=\"");
	        html.append(uiid);
	        html.append("\" class=\"tab\">");
	        html.append("<div class=\"uimaster_tabPane\">");
	        
	        //Generate the titles' container
	        html.append("<div class =\"tab-titles\" id=\"titles-container-");
	        html.append(uiid);
	        html.append("\" selectedIndex=\"");
	        html.append(getSelectedIndex());
	        html.append("\">");
	        html.append("</div>");
	        
	        //Generate the bodies of the tabpane
	        html.append("<div class=\"tab-bodies\" id=\"bodies-container-");
	        html.append(uiid);
	        html.append("\">");
	        html.append("</div>");
	        
	        html.append("</div>");
	        html.append("</div>");
	        return html.toString();
    	} finally {
			DisposableBfString.release(html);
		}
    }
    public String generateJS()
    {
        StringBuffer js = new StringBuffer(300);
        js.append("defaultname.");
        js.append(uiid);
        js.append("=new UIMaster.ui.tab({");
        js.append("uiid:\"").append(uiid).append("\",");
        js.append("ui:elementList[\"");
        js.append(uiid);
        js.append("\"]");
        js.append(super.generateJS());
        js.append("});");
        for ( int i = 0; i < entities.size(); i++ )
        {
            RefForm entity = ((RefForm)entities.get(i));
            if (entity != null )
            {
                js.append(entity.generateJS());
            }
        }
        return js.toString();
    }
    
    public boolean loadContent(int index) throws UIPageException, EvaluationException, ParsingException {
    	if (!this.ajaxLoad) {
    		return false;
    	}
    	if (this.tabs == null) {
    		throw new IllegalStateException("Please enable the ajax loading for this tab panel.");
    	}
    	this.selectedIndex = index;
    	if (this.tabs.size() <= index || loadedTabs.containsKey(index)) {
    		//prevent the dynamic frame tab or loaded tab.
    		return false;
    	}
    	loadedTabs.put(index, true);
    	this.accessedIndex.incrementAndGet();
    	
    	
    	AjaxContext ajaxContext = AjaxActionHelper.getAjaxContext();
    	Map ajaxMap = null;
		try {
			ajaxMap = AjaxActionHelper.getFrameMap(ajaxContext.getRequest());
		} catch (AjaxException e1) {
			logger.warn("Session maybe timeout: " + e1.getMessage(), e1);
			return false;
		}
		UserRequestContext orginalUserContext = UserRequestContext.UserContext.get();
    	try {
    		StringWriter htmlWriter = new StringWriter();
	    	UserRequestContext htmlContext = new UserRequestContext(ajaxContext.getRequest(), htmlWriter);
	        htmlContext.setCurrentFormInfo(this.getUIEntityName(), ajaxContext.getEntityPrefix(), ajaxContext.getEntityPrefix().replace(".", "-"));
	        htmlContext.setIsDataToUI(true);//Don't set prefix in here.
	        htmlContext.setAjaxWidgetMap(ajaxMap);
	        htmlContext.setFormObject(ownerEntity);
	        htmlContext.setODMapperData(new HashMap());
	        UserRequestContext.UserContext.set(htmlContext);
	        
	    	UITabPaneItemType tab = tabs.get(index);
	        String entityPrefix = ajaxContext.getEntityPrefix();
	        if (this.evalContext == null) {
	        	throw new UIPageException("Evaluation Context is missing for TabPane lazyloading form! " + tab.getUiid());
	        }
	        
		if (tab.getPanel() != null) {
			VariableEvaluator ee = new VariableEvaluator(this.evalContext);
			List<String> componentIds = ownerEntity.getAllComponentID(this.getId() + "."+ tab.getUiid());
    		for(String compId : componentIds) {
    			Map propMap = ownerEntity.getComponentProperty(compId);
    			Map i18nMap = ownerEntity.getComponentI18N(compId);
    			Map expMap = ownerEntity.getComponentExpression(compId);
    			Map<String, Object> tempMap = new HashMap<String, Object>();
    			if (expMap != null && expMap.size() > 0) {
    				HTMLUtil.evaluateExpression(propMap, expMap, tempMap, ee);
    			}
    			if (i18nMap != null && i18nMap.size() > 0) {
    				HTMLUtil.internationalization(propMap, i18nMap, tempMap, htmlContext);
    			}
    			String uiid = htmlContext.getHTMLPrefix() + compId;
    			htmlContext.addAttribute(uiid, tempMap);
				HTMLWidgetType htmlWidget = ownerEntity.getComponents().get(compId);
				if (htmlWidget.getClass() == HTMLPanelType.class && ((HTMLPanelType)htmlWidget).hasDynamicUI()) {
		        	String filter = (String)htmlContext.getAttribute(htmlWidget.getName(), "dynamicUIFilter");
		    		if (filter == null)
		    			filter = "";
		        	List<HTMLDynamicUIItem> dynamicItems = ownerEntity.getDynamicItems(compId, filter);
		        	((HTMLPanelType)htmlWidget).setDynamicItems(dynamicItems);
		        } 
				
    			Widget newAjax = htmlWidget.createAjaxWidget(ee);
                if (newAjax != null) {
                	htmlContext.addAjaxWidget(newAjax.getId(), newAjax);
                	if (newAjax.getClass() == Button.class) {
                    	// all express must be re-calculate when click button in every time.
                		((Button)newAjax).setExpressMap(expMap);
                	}
                }
    		}
    		AjaxActionHelper.updateFrameMap(ajaxContext.getRequest(), htmlContext.getPageAjaxWidgets());
    		
        	//ui panel support
        	String id = entityPrefix + tab.getPanel().getUIID();
        	HTMLPanelLayout panelLayout = new HTMLPanelLayout(tab.getPanel().getUIID(), ownerEntity);
        	TableLayoutConstraintType layoutConstraint = new TableLayoutConstraintType();
            panelLayout.setConstraints(layoutConstraint);
            panelLayout.setBody(tab.getPanel());
        	
            HTMLCellLayoutType layout = new HTMLCellLayoutType(id);
            panelLayout.generateComponentHTML(htmlContext, 0, false, layout);
            
            IJsGenerator jsGenerator = IServerServiceManager.INSTANCE.getService(IJsGenerator.class);
            StringBuilder js = DisposableBfString.getBuffer();
            try {
	            js.append(jsGenerator.gen(this.getUIEntityName(), entityPrefix, tab.getPanel()));
	            js.append("\ndefaultname.");
	            if (entityPrefix != null && entityPrefix.length() > 0) {
	            	js.append(entityPrefix).append('.');
	            }
	            js.append("Form.items.push(elementList['").append(tab.getPanel().getUIID()).append("']);");
	            
	            IDataItem dataItem = AjaxActionHelper.createAppendItemToTab(this.getId(), id);
	            dataItem.setData(htmlWriter.getBuffer().toString());
	            dataItem.setJs(js.toString());
	            dataItem.setFrameInfo(this.getFrameInfo());
	            AjaxActionHelper.getAjaxContext().addDataItem(dataItem);
            } finally {
    			DisposableBfString.release(js);
    		}
			
        } else if (tab.getRefEntity() != null) {
        	ODEntityContext refFormContext = null;
			if (tab.getOdmappings() != null && tab.getOdmappings().size() > 0) {
				// we assume only one SimpleComponentMappingType defined for referenced form.
				// the panel mappings can be multiple supported.
				ODContext odObject = new ODTabPaneContext(htmlContext, this.evalContext);
				for (SimpleComponentMappingType mapping : tab.getOdmappings()) {
					SimpleComponentMapping scMapping = new SimpleComponentMapping(mapping);
					scMapping.parse(odObject, null);
					ODEntityContext result = scMapping.executeDataToUI(odObject);
					if (result != null) {
						refFormContext = result;
					}
				}
				AjaxActionHelper.updateFrameMap(ajaxContext.getRequest(), htmlContext.getPageAjaxWidgets());
			}
        	if (refFormContext == null) {
        		throw new UIPageException("OD Mapping Evaluation Context is missing for TabPane lazyloading form! " + tab.getUiid());
        	}
        	DefaultEvaluationContext inputContext = (DefaultEvaluationContext)refFormContext.getEvaluationContextObject("$");
        	UIReferenceEntityType itemRef = (UIReferenceEntityType)tab.getRefEntity();
        	String UIID = entityPrefix + itemRef.getUIID();
            String type = itemRef.getReferenceEntity().getEntityName();
			RefForm form = new RefForm(UIID, type, inputContext.getVariableObjects());
			htmlContext.addAjaxWidget(form.getId(), form);
			AjaxActionHelper.updateFrameMap(ajaxContext.getRequest(), htmlContext.getPageAjaxWidgets());
			AjaxActionHelper.getAjaxContext().addAJAXComponent(form.getId(), form);
			htmlContext.setCurrentFormInfo(type, UIID + ".", "");
			try {
				PageDispatcher dispatcher = new PageDispatcher(refFormContext.getUIFormObject(), inputContext);
	            dispatcher.forwardForm(htmlContext, 0, isReadOnly(), null);
	            
	            // append the dynamic js files.
	            StringWriter jswriter1 = new StringWriter();
	            UserRequestContext jsContext = new UserRequestContext(ajaxContext.getRequest(), jswriter1);
	            refFormContext.getUIFormObject().getJSPathSet(jsContext, Collections.emptyMap(), true);
	        	String data = jswriter1.getBuffer().toString();
	        	IDataItem dataItem = AjaxActionHelper.createLoadJS(form.getId(), data);
	            dataItem.setFrameInfo(getFrameInfo());
	            ajaxContext.addDataItem(dataItem);
				AjaxActionHelper.getAjaxContext().addDataItem(dataItem);
				
		    	IDataItem dataItem1 = AjaxActionHelper.createAppendItemToTab(this.getId(), UIID);
	            dataItem1.setData(htmlWriter.getBuffer().toString());
		        dataItem1.setJs(form.generateJS());
	            dataItem1.setFrameInfo(this.getFrameInfo());
	            AjaxActionHelper.getAjaxContext().addDataItem(dataItem1);
			} finally {
				htmlContext.resetCurrentFormInfo();
			}
        } else if (tab.getFrames() != null && tab.getFrames().size() > 0) {
        	//page support
        	UIFrameType definedFrame = null;
        	for (UIFrameType f : tab.getFrames()) {
        		if (f.getRole() != null && UserContext.hasRole(f.getRole())) {
        			definedFrame = f;
        			break;
        		}
        	}
        	if (definedFrame == null) {
        		for (UIFrameType f : tab.getFrames()) {
            		if (f.getRole() == null) {
            			definedFrame = f;
            			break;
            		}
            	}
        		if (definedFrame == null) {
        			throw new IllegalStateException("Please configure a default role of frames for current user!");
        		}
        	}
			HttpServletRequest request = ajaxContext.getRequest();
        	request.setAttribute("_chunkname", definedFrame.getChunkName());
        	request.setAttribute("_nodename", definedFrame.getNodeName());
        	request.setAttribute("_framePagePrefix", ajaxContext.getFrameId());
        	request.setAttribute("_tabcontent", "true");
        	HTMLFrameType frame = new HTMLFrameType(tab.getUiid());
        	frame.addAttribute(HTMLFrameType.NEED_SRC, "true");
        	frame.generateBeginHTML(htmlContext, ownerEntity, 0);
        	frame.generateEndHTML(htmlContext, ownerEntity, 0);
        	
        	String UIID = entityPrefix + tab.getUiid();
        	IDataItem dataItem = AjaxActionHelper.createAppendItemToTab(this.getId(), UIID);
            dataItem.setData(htmlContext.getHtmlString());
            dataItem.setFrameInfo(this.getFrameInfo());
            AjaxActionHelper.getAjaxContext().addDataItem(dataItem);
        } 
		return true;
    	} finally {
    		UserRequestContext.UserContext.set(orginalUserContext);
    		if (this.accessedIndex.get() >= this.tabs.size()) {
    			this.tabs = null;
    			this.ownerEntity = null;
    			this.accessedIndex = null;
    		}
    	}
    }

    public void syncSelectedAction(AjaxContext context) throws EvaluationException {
    	if (this.selectedAction != null) {
			this.selectedAction.evaluate(context);
    	}
    }
    
    /**
     * Adds a component with a title which can be null at the end of the TabPane.
     * @param title
     * @param component
     */
    public void addTab(String title, RefForm component)
    {
        addTabAt(titles.size(), title, component);
    }
    
    /**
     * Adds a component with a title which can be null at index.
     * @param index: must be unique.
     * @param title
     * @param component
     */
    public void addTabAt(int index, String title, RefForm component)
    {
        titles.add(title);
        entities.add(component);
        if(index > entities.size() || index < 0)
        {
            throw new IllegalArgumentException("Fail to add tab in TabPane at " + index);
        }
        this.selectedIndex = index;
        
        AjaxContext ajaxContext = AjaxActionHelper.getAjaxContext();
        IDataItem dataItem = AjaxActionHelper.createDataItem();
        dataItem.setJs(component.generateJS());
        dataItem.setUiid(uiid);
        dataItem.setJsHandler(HANDLERNAME);
        
        Map data = new HashMap();
        data.put("cmd",CMD_ADDTAB);
        data.put("index",String.valueOf(index));
        data.put("title",title);
        data.put("entity",component.generateHTML());
        dataItem.setData((new JSONObject(data)).toString());
        dataItem.setFrameInfo(getFrameInfo());
        ajaxContext.addDataItem(dataItem);
    }
    
    /**
     * Removes the tab and component which corresponds to the specified index.
     * @param index
     */
    public void removeTabAt(int index)
    {
        if(index >= entities.size() || index < 0)
        {
            return;
        }
        entities.remove(index);
        titles.remove(index);
        if(getSelectedIndex() == index)
        {
            this.selectedIndex = 0;
        }
        
        Map data = new HashMap();
        data.put("cmd",CMD_REMOVETAB);
        data.put("index",String.valueOf(index));
        AjaxContext ajaxContext = AjaxActionHelper.getAjaxContext();
        IDataItem dataItem = createData((new JSONObject(data)).toString());
        ajaxContext.addDataItem(dataItem);
    }
    
    /**
     * Sets the component that is responsible for rendering the title for the specified tab
     * @param index
     * @param component
     */
    public void setTabComponentAt(int index, RefForm component)
    {
        if(index >= entities.size() || index < 0)
        {
            return;
        }
        entities.set(index, component);
        
        Map data = new HashMap();
        data.put("cmd",CMD_SETBODY);
        data.put("index",String.valueOf(index));
        data.put("entity",component.generateHTML());
        
        AjaxContext ajaxContext = AjaxActionHelper.getAjaxContext();
        IDataItem dataItem = createData((new JSONObject(data)).toString());
        dataItem.setJs(component.generateJS());
        ajaxContext.addDataItem(dataItem);
    }

    /**
     * Sets the title at index to title which can be null.
     * @param index
     * @param title
     */
    public void setTitleAt(int index, String title)
    {
        if(index >= titles.size() || index < 0)
        {
            return;
        }
        titles.set(index, title);
        
        Map data = new HashMap();
        data.put("cmd",CMD_SETTITLE);
        data.put("index",String.valueOf(index));
        data.put("title",title);
        
        AjaxContext ajaxContext = AjaxActionHelper.getAjaxContext();
        IDataItem dataItem = createData((new JSONObject(data)).toString());
        ajaxContext.addDataItem(dataItem);
    }
    
    private IDataItem createData(String data) 
    {
        IDataItem dataItem = AjaxActionHelper.createDataItem();
        dataItem.setUiid(uiid);
        dataItem.setJsHandler(HANDLERNAME);
        dataItem.setData(data);
        dataItem.setFrameInfo(getFrameInfo());
        return dataItem;
    }
    /**
     * Returns the component at index.
     * @param index
     * @return
     */
    public RefForm getComponentAt(int index)
    {
        if(index >= entities.size() || index < 0)
        {
            return null;
        }
        return (RefForm)entities.get(index);
    }
       
    /**
     * Returns the tab title at index.
     * @param index
     * @return
     */
    public String getTitleAt(int index)
    {
        return (String)titles.get(index);
    }
       
    /**
     * Returns the number of tabs in this tabbedpane
     * @return
     */
    public int getTabCount()
    {
        return titles.size();
    }
      
    /**
     * Returns the index of the tab for the specified component.
     * @param component
     * @return
     */
    public int indexOfComponent(RefForm component)
    {
        return entities.indexOf(component);
    }

    public List getEntities()
    {
        return entities;
    }

    public void setEntities(List entities)
    {
        this.entities = entities;
    }

    /**
     * Return the index of the selected tab.
     * @return
     */
    public int getSelectedIndex()
    {
        return this.selectedIndex;
    }
    
    /**
     * Select a tab.
     * @return
     */
    public void setSelectedIndex(int selectedIndex)
    {
        this.selectedIndex = selectedIndex;
        Map data = new HashMap();
        data.put("cmd",CMD_SETSELECTEDINDEX);
        data.put("index",String.valueOf(selectedIndex));
        
        AjaxContext ajaxContext = AjaxActionHelper.getAjaxContext();
        IDataItem dataItem = createData((new JSONObject(data)).toString());
        ajaxContext.addDataItem(dataItem);
    }
    
    public List getTitles()
    {
        return titles;
    }

    public void setTitles(List titles)
    {
        this.titles = titles;
    }

    /**
     * Whether this component can have editPermission.
     */
    @Override
    public boolean isEditPermissionEnabled()
    {
        return false;
    }

}
