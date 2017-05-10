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
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.ajax.CellLayout;
import org.shaolin.uimaster.page.ajax.PreNextPanel;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLPreNextPanelType extends HTMLContainerType
{
    private static final Logger logger = LoggerFactory.getLogger(HTMLPreNextPanelType.class);

    private int selectedIndex;

    public HTMLPreNextPanelType(String id)
    {
        super(id);
        selectedIndex = 0;
    }
    
    public boolean isAjaxLoading() {
    	return false;
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

            List<UITabPaneItemType> tabs = (List<UITabPaneItemType>)this.removeAttribute("tabPaneItems");
            int tabSelected = 0;
            if(this.getAttribute("tabSelected") != null) {
                tabSelected = Integer.parseInt((String)this.getAttribute("tabSelected"));
            }
            List<HTMLReferenceEntityType> createdRefEntities = (List<HTMLReferenceEntityType>)this.removeAttribute("createdRefEntities");
            
            //Generate the titles of the tabpane
            String cmpName = getName().replace('.', '_');
            HTMLUtil.generateTab(context, depth + 1);
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
                    context.generateHTML("<div class =\"ui-state-default ui-corner-top ui-tabs-active ui-state-active uimaster_prenext_title\" style=\"border-bottom: 1px solid white;\" id = \"tab-" + cmpName + "-titles-" + i + "\" index=\""+ i +"\">");
                    selectedIndex = i;
                }
                else
                {
                    context.generateHTML("<div class =\"ui-state-default ui-corner-top uimaster_prenext_title\" id = \"tab-" + cmpName + "-titles-" + i + "\" index=\""+ i +"\" ajaxload=\"false\">");
                }
                context.generateHTML((i+1) + ") " + title);
                context.generateHTML("</div>");
            }
            context.generateHTML("</div>");

            //Generate the bodies of the tabpane
            HTMLUtil.generateTab(context, depth + 1);
            context.generateHTML("<div class=\"tab-bodies\" id=\"bodies-container-" + cmpName + "\">");
            for(int i = 0, n = tabs.size(); i < n; i++)
            {
            	//Generate one body
            	HTMLUtil.generateTab(context, depth + 2);
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
            	HTMLUtil.generateTab(context, depth + 2);
                UITabPaneItemType tab = tabs.get(i);
                if (tab.getPanel() != null) {
                	//ui panel support
                	String UIID = tab.getPanel().getUIID();
                	HTMLPanelLayout panelLayout = new HTMLPanelLayout(UIID, ownerEntity);
                	TableLayoutConstraintType layoutConstraint = new TableLayoutConstraintType();
                    panelLayout.setConstraints(layoutConstraint);
                    panelLayout.setBody(tab.getPanel());
                	
                    panelLayout.generateComponentHTML(context, depth+3, false, this.getHTMLLayout());
                } else if (tab.getRefEntity() != null) {
                	//form support
                	HTMLReferenceEntityType refEntity = createdRefEntities.get(i);
	                //Generate the uiform of the body
	                refEntity.generateBeginHTML(context, ownerEntity, depth+3);
	                refEntity.generateEndHTML(context, ownerEntity, depth+3);
                } else if (tab.getFrames() != null && tab.getFrames().size() > 0) {
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
                	//page support
                	context.getRequest().setAttribute("_chunkname", definedFrame.getChunkName());
                	context.getRequest().setAttribute("_nodename", definedFrame.getNodeName());
                	context.getRequest().setAttribute("_framePagePrefix", context.getFrameInfo());
                	context.getRequest().setAttribute("_tabcontent", "true");
                	HTMLFrameType frame = new HTMLFrameType(tab.getUiid());
                	frame.addAttribute(HTMLFrameType.NEED_SRC, "true");
                	frame.generateBeginHTML(context, ownerEntity, depth + 3);
                	frame.generateEndHTML(context, ownerEntity, depth + 3);
                } 
                HTMLUtil.generateTab(context, depth + 2);
                context.generateHTML("</div>");
            }
            HTMLUtil.generateTab(context, depth + 1);
            context.generateHTML("</div>");
            
            context.generateHTML("<div class=\"uimaster_prenext_buttons\"><button type=\"button\" data-inline=\"true\" title=\"Previous\">&lt;--</button>");
    		context.generateHTML("<button type=\"button\" data-inline=\"true\" title=\"Next\">--&gt;</button></div>");
            HTMLUtil.generateTab(context, depth + 1);
            
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
    	ExpressionType previousAction = (ExpressionType)this.removeAttribute("previousAction");
    	ExpressionType nextAction = (ExpressionType)this.removeAttribute("nextAction");
    	
    	List<UITabPaneItemType> tabs = (List<UITabPaneItemType>)this.getAttribute("tabPaneItems");
    	PreNextPanel panel = new PreNextPanel(getName(), tabs, selectedIndex, new CellLayout());
    	panel.setPreviousAction(previousAction);
    	panel.setNextAction(nextAction);
        panel.setReadOnly(isReadOnly());
        panel.setUIEntityName(getUIEntityName());
        
        List<HTMLReferenceEntityType> createdRefEntities = new ArrayList<HTMLReferenceEntityType>();
        for(int i = 0, n = tabs.size(); i < n; i++){
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
        this.addAttribute("createdRefEntities", createdRefEntities);
        panel.setListened(true);
        return panel;

    }

}
