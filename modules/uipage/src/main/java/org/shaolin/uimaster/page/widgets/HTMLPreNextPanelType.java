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
import java.util.Collections;
import java.util.List;

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.datamodel.page.ResourceBundlePropertyType;
import org.shaolin.bmdp.datamodel.page.StringPropertyType;
import org.shaolin.bmdp.datamodel.page.TableLayoutConstraintType;
import org.shaolin.bmdp.datamodel.page.UIReferenceEntityType;
import org.shaolin.bmdp.datamodel.page.UITabPaneItemType;
import org.shaolin.bmdp.i18n.ResourceUtil;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.shaolin.uimaster.html.layout.HTMLPanelLayout;
import org.shaolin.uimaster.page.HTMLSnapshotContext;
import org.shaolin.uimaster.page.HTMLUtil;
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

    public HTMLPreNextPanelType()
    {
        selectedIndex = 0;
    }

    public HTMLPreNextPanelType(HTMLSnapshotContext context)
    {
        super(context);
        selectedIndex = 0;
    }

    public HTMLPreNextPanelType(HTMLSnapshotContext context, String id)
    {
        super(context, id);
        selectedIndex = 0;
    }
    
    public boolean isAjaxLoading() {
    	return (boolean)this.removeAttribute("ajaxLoad");
    }

    public void generateBeginHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth)
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
                    title = ResourceUtil.getResource(bundle, key);
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
                	OOEEContext ooee = OOEEContextFactory.createOOEEContext();
                    panelLayout.setConstraints(layoutConstraint, ooee);
                    panelLayout.setBody(tab.getPanel(), ooee);
                	
                    panelLayout.generateComponentHTML(context, depth+3, false, Collections.emptyMap(), ee, this.getHTMLLayout());
                } else if (tab.getRefEntity() != null) {
                	//form support
                	String prefix = context.getHTMLPrefix();
                	try {
	                	UIReferenceEntityType itemRef = (UIReferenceEntityType)tab.getRefEntity();
	                	String UIID = this.getPrefix() + itemRef.getUIID();
		                String type = itemRef.getReferenceEntity().getEntityName();
		                HTMLReferenceEntityType refEntity = new HTMLReferenceEntityType(context, UIID, type);
		                
		                Widget newWidget = refEntity.createAjaxWidget(ee);
		                context.addAjaxWidget(newWidget.getId(), newWidget);
		                //Generate the uiform of the body
		                refEntity.generateBeginHTML(context, ownerEntity, depth+3);
		                refEntity.generateEndHTML(context, ownerEntity, depth+3);
                	} finally {
                		context.setHTMLPrefix(prefix);
                	}
                } else if (tab.getFrame() != null) {
                	//page support
                	this.context.getRequest().setAttribute("_chunkname", tab.getFrame().getChunkName());
                	this.context.getRequest().setAttribute("_nodename", tab.getFrame().getNodeName());
                	this.context.getRequest().setAttribute("_framePagePrefix", this.context.getFrameInfo());
                	this.context.getRequest().setAttribute("_tabcontent", "true");
                	HTMLFrameType frame = new HTMLFrameType(this.context, tab.getUiid());
                	frame.setHTMLAttribute(HTMLFrameType.NEED_SRC, "true");
                	frame.generateBeginHTML(context, ownerEntity, depth + 3);
                	frame.generateEndHTML(context, ownerEntity, depth + 3);
                } 
                HTMLUtil.generateTab(context, depth + 2);
                context.generateHTML("</div>");
            }
            HTMLUtil.generateTab(context, depth + 1);
            context.generateHTML("</div>");
            
            context.generateHTML("<div class=\"uimaster_prenext_buttons\"><button type=\"button\" title=\"Previous\">&lt;--</button><button type=\"button\" title=\"Next\">--&gt;</button></div>");
            HTMLUtil.generateTab(context, depth + 1);
            
            generateEndWidget(context);
        }
        catch (Exception e)
        {
            logger.error("error. in entity: " + getUIEntityName(), e);
        }
    }

    public void generateEndHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth)
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

    public void generateAttribute(HTMLSnapshotContext context, String attributeName, Object attributeValue) throws IOException
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
    
    private VariableEvaluator ee;
    
    public Widget createAjaxWidget(VariableEvaluator ee)
    {
    	this.ee = ee;
    	ExpressionType previousAction = (ExpressionType)this.removeAttribute("previousAction");
    	ExpressionType nextAction = (ExpressionType)this.removeAttribute("nextAction");
    	
    	List<UITabPaneItemType> tabs = (List<UITabPaneItemType>)this.getAttribute("tabPaneItems");
    	PreNextPanel panel = new PreNextPanel(getName(), tabs, selectedIndex, new CellLayout());
    	panel.setPreviousAction(previousAction);
    	panel.setNextAction(nextAction);
        panel.setReadOnly(getReadOnly());
        panel.setUIEntityName(getUIEntityName());
        
        panel.setListened(true);
        panel.setFrameInfo(getFrameInfo());
        
        return panel;

    }

}
