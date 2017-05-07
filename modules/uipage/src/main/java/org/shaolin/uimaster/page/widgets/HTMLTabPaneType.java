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
import java.util.ArrayList;
import java.util.List;

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.datamodel.page.ResourceBundlePropertyType;
import org.shaolin.bmdp.datamodel.page.StringPropertyType;
import org.shaolin.bmdp.datamodel.page.TableLayoutConstraintType;
import org.shaolin.bmdp.datamodel.page.UIFrameType;
import org.shaolin.bmdp.datamodel.page.UIReferenceEntityType;
import org.shaolin.bmdp.datamodel.page.UITabPaneItemType;
import org.shaolin.bmdp.i18n.LocaleContext;
import org.shaolin.bmdp.i18n.ResourceUtil;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.uimaster.html.layout.HTMLPanelLayout;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.ajax.CellLayout;
import org.shaolin.uimaster.page.ajax.TabPane;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLTabPaneType extends HTMLContainerType
{
    private static final Logger logger = LoggerFactory.getLogger(HTMLTabPaneType.class);

    public HTMLTabPaneType(String id)
    {
        super(id);
    }
    
    public boolean isAjaxLoading() {
    	return (boolean)this.removeAttribute("ajaxLoad");
    }

    public void generateBeginHTML(UserRequestContext context, UIFormObject ownerEntity, int depth)
    {
        try
        {
            generateWidget(context);

            context.generateHTML("<div id=\"");
            context.generateHTML(getName());
            context.generateHTML("\" class=\"tab ui-tabs ui-widget ui-corner-all\">");

            super.generateBeginHTML(context, ownerEntity, depth);

            int selectedIndex = 0;
            List<UITabPaneItemType> tabs = (List<UITabPaneItemType>)this.getAttribute("tabPaneItems");
            int tabSelected = 0;
            if(this.getAttribute("tabSelected") != null) {
                tabSelected = Integer.parseInt((String)this.getAttribute("tabSelected"));
            }
            boolean ajaxLoad = (boolean)this.getAttribute("ajaxLoad");
            if (ajaxLoad) {
            	TabPane tabPane = ((TabPane)context.getPageAjaxWidgets().get(this.getName()));
            	tabPane.setOwnerEntity(ownerEntity);
            }
            List<HTMLReferenceEntityType> createdRefEntities = (List<HTMLReferenceEntityType>)this.removeAttribute("createdRefEntities");
            
            //Generate the titles of the tabpane
            String cmpName = getName().replace('.', '_');
            context.generateHTML("<div class =\"tab-titles ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all\" id=\"titles-container-");
            context.generateHTML(cmpName);
            context.generateHTML("\" selectedIndex=\"");
            context.generateHTML(tabSelected + "");
            context.generateHTML("\">");
            for(int i = 0, n = tabs.size(); i < n; i++)
            {
                UITabPaneItemType tab = tabs.get(i);
                String title = "";
                if (tab.getTitle() instanceof ResourceBundlePropertyType)
                {
                	ResourceBundlePropertyType resourceBundle = ((ResourceBundlePropertyType)tab.getTitle());
                    String bundle = resourceBundle.getBundle();
                    String key = resourceBundle.getKey();
                    title = ResourceUtil.getResource(LocaleContext.getUserLocale(), bundle, key);
                } 
                else if (tab.getTitle() instanceof StringPropertyType)
                {
                	title = ((StringPropertyType)tab.getTitle()).getValue();
                }
                if(i == tabSelected)
                {
                    context.generateHTML("<div class =\"ui-state-default ui-corner-top ui-tabs-active ui-state-active\" style=\"border-bottom: 1px solid white;\" id = \"tab-" + cmpName + "-titles-" + i + "\" index=\""+ i +"\">");
                    selectedIndex = i;
                }
                else
                {
                    context.generateHTML("<div class =\"ui-state-default ui-corner-top\" id = \"tab-" + cmpName + "-titles-" + i + "\" index=\""+ i +"\" ajaxload=\"" + ajaxLoad + "\">");
                }
                context.generateHTML("" + title);
                context.generateHTML("</div>");
            }
            context.generateHTML("</div>");

            //Generate the bodies of the tabpane
            context.generateHTML("<div class=\"tab-bodies\" id=\"bodies-container-" + cmpName + "\">");
            for(int i = 0, n = tabs.size(); i < n; i++)
            {
            	//Generate one body
            	context.generateHTML("<div id=\"tab-");
            	context.generateHTML(cmpName + "-body-"+ i + "\"");
            	context.generateHTML(" class=\"");
            	if(tabSelected == i)
            	{
            		context.generateHTML("tab-selected-body ");
            	}
            	else
            	{
            		context.generateHTML("tab-unselected-body ");
            	}
            	context.generateHTML("\" index=\"");
            	context.generateHTML(String.valueOf(i));
            	context.generateHTML("\"");
            	context.generateHTML(" uiid=\"");
            	context.generateHTML(tabs.get(i).getUiid());
            	context.generateHTML("\">");
            	
            	if (!ajaxLoad || i==0) {
	                UITabPaneItemType tab = tabs.get(i);
	                if (tab.getPanel() != null) {
	                	//ui panel support
	                	String UIID = tab.getPanel().getUIID();
	                	HTMLPanelLayout panelLayout = new HTMLPanelLayout(UIID, ownerEntity);
	                	TableLayoutConstraintType layoutConstraint = new TableLayoutConstraintType();
	                    panelLayout.setConstraints(layoutConstraint);
	                    panelLayout.setBody(tab.getPanel());
	                	
	                    panelLayout.generateComponentHTML(context, depth, false, this.getHTMLLayout());
	                } else if (tab.getRefEntity() != null) {
	                	//form support
                		HTMLReferenceEntityType refEntity = createdRefEntities.get(i);
		                //Generate the uiform of the body
		                refEntity.generateBeginHTML(context, ownerEntity, depth+1);
		                refEntity.generateEndHTML(context, ownerEntity, depth+1);
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
	                	
	                	context.getRequest().setAttribute("_chunkname", definedFrame.getChunkName());
	                	context.getRequest().setAttribute("_nodename", definedFrame.getNodeName());
	                	context.getRequest().setAttribute("_framePagePrefix", context.getFrameInfo());
	                	context.getRequest().setAttribute("_tabcontent", "true");
	                	HTMLFrameType frame = new HTMLFrameType(tab.getUiid());
	                	frame.addAttribute(HTMLFrameType.NEED_SRC, "true");
	                	frame.addAttribute("_chunkname", definedFrame.getChunkName());
	                	frame.addAttribute("_nodename", definedFrame.getNodeName());
	                	frame.addAttribute("_framePagePrefix", context.getFrameInfo());
	                	frame.addAttribute("_tabcontent", "true");
	                	frame.setHTMLLayout(this.getHTMLLayout());
	                	frame.generateBeginHTML(context, ownerEntity, depth + 1);
	                	frame.generateEndHTML(context, ownerEntity, depth + 1);
	                } 
            	}
                context.generateHTML("</div>");
            }
            context.generateHTML("</div>");
            
            generateEndWidget(context);
        }
        catch (Exception e)
        {
            logger.error("error. in entity: " + getUIEntityName(), e);
        }
    }

    public void generateEndHTML(UserRequestContext context, UIFormObject ownerEntity, int depth)
    {
        try
        {
            super.generateEndHTML(context, ownerEntity, depth);
            context.generateHTML("</div>");
        }
        catch (Exception e)
        {
            logger.error("error. in entity: " + getUIEntityName(), e);
        }
    }

    public void generateAttribute(UserRequestContext context, String attributeName, Object attributeValue) throws IOException
    {
        if( !(attributeValue instanceof String) )
            return;
        
        String attrValue = (String)attributeValue;

        if ("align".equals(attributeName))
        {
            if(!"full".equals(attrValue.toLowerCase()))
            {
                addStyle("text-align", attrValue);
            }
        }
        else if ( "valign".equals(attributeName) || "rowUIStyle".equals(attributeName)
                || "x".equals(attributeName) || "y".equals(attributeName) )
        {
        }
        else if ("width".equals(attributeName))
        {
            addStyle("min-width", attrValue);
            addStyle("_width", attrValue);
        }
        else if ("height".equals(attributeName))
        {
            addStyle("min-height", attrValue);
            addStyle("_height", attrValue);
        }
        else
        {
            super.generateAttribute(context, attributeName, attributeValue);
        }
    }

    /**
     * Whether this component can have editPermission.
     */
    @Override
    public boolean isEditPermissionEnabled()
    {
        return false;
    }
    
    public Widget createAjaxWidget(VariableEvaluator ee)
    {
    	ExpressionType selectedAction = (ExpressionType)this.removeAttribute("selectedAction");
    	List<UITabPaneItemType> tabs = (List<UITabPaneItemType>)this.getAttribute("tabPaneItems");
    	TabPane panel = new TabPane(getName(), tabs, 0, new CellLayout());
    	panel.setOriginalUIID(this.getUIID());
    	panel.setAjaxLoad((boolean)this.getAttribute("ajaxLoad"));
    	panel.setSelectedAction(selectedAction);
        panel.setReadOnly(isReadOnly());
        panel.setUIEntityName(getUIEntityName());
        panel.setListened(true);
        
        List<HTMLReferenceEntityType> createdRefEntities = new ArrayList<HTMLReferenceEntityType>();
        boolean ajaxLoad = (boolean)this.getAttribute("ajaxLoad");
        for(int i = 0, n = tabs.size(); i < n; i++) {
        	if (ajaxLoad && i > 0) {
        		// save context if it's ajax loading.
        		String uiid = tabs.get(i).getUiid();
        		panel.addODMapperContext(uiid, UserRequestContext.UserContext.get().getODMapperContext(uiid));
        	} 
        	if (!ajaxLoad || i==0) {
                UITabPaneItemType tab = tabs.get(i);
                if (tab.getRefEntity() != null) {
                	UIReferenceEntityType itemRef = (UIReferenceEntityType)tab.getRefEntity();
                	String UIID = itemRef.getUIID();
	                String type = itemRef.getReferenceEntity().getEntityName();
	                HTMLReferenceEntityType refEntity = new HTMLReferenceEntityType(UIID, type);
	                
	                Widget newWidget = refEntity.createAjaxWidget(ee);
	                UserRequestContext.UserContext.get().addAjaxWidget(newWidget.getId(), newWidget);
	                createdRefEntities.add(refEntity);
                }
        	}
        }
        this.addAttribute("createdRefEntities", createdRefEntities);
        return panel;

    }

}
