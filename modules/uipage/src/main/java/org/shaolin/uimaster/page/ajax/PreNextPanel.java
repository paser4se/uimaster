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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.datamodel.page.UITabPaneItemType;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.uimaster.page.AjaxActionHelper;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.DisposableBfString;
import org.shaolin.uimaster.page.ajax.json.IDataItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PreNext Panel does not support the ajax loading.
 * Please do not fill many components into it.
 * 
 * @author wushaol
 */
public class PreNextPanel extends Container implements Serializable 
{
	private static final Logger logger = LoggerFactory.getLogger(PreNextPanel.class);
    private static final long serialVersionUID = -1744731434666233557L;
    private static final String CMD_SETTITLE = "setTitle";
    private static final String CMD_SETSELECTEDINDEX = "setSelectedIndex";
    private static final String HANDLERNAME = "tabPaneHandler";
    
    //List of titles(String)
    private List titles=new ArrayList();
    //List of entities(RefEntity)
    private List entities=new ArrayList();
    //
    
    private ExpressionType previousAction;
    private ExpressionType nextAction;
    
	private String uiid;
    private int selectedIndex;
    
    /**
     * 
     * @param uiid 
     * @param titles List of titles(String)
     * @param entities List of bodies(String)
     */
    public PreNextPanel(String uiid)
    {
        this(uiid, new CellLayout());
    }
    
    public PreNextPanel(String id, Layout layout)
    {
        super(AjaxActionHelper.getAjaxContext().getEntityPrefix() +id, layout);
        this.setListened(true);
        this.uiid = this.getId();
    }
    
    public PreNextPanel addAttribute(String name, Object value, boolean update)
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
    public PreNextPanel(String id, List<UITabPaneItemType> tabs, int selectedIndex, Layout layout)
    {
        super(id, layout);
        this.setListened(true);
        this.selectedIndex = selectedIndex;
        this.uiid = this.getId();
    }

    public ExpressionType getPreviousAction() {
		return previousAction;
	}

	public void setPreviousAction(ExpressionType previousAction) {
		this.previousAction = previousAction;
	}

	public ExpressionType getNextAction() {
		return nextAction;
	}

	public void setNextAction(ExpressionType nextAction) {
		this.nextAction = nextAction;
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
    
    public void loadContent(int index) throws JspException, EvaluationException {
    	//unsupported
    }

    public void invokePreAction(AjaxContext context) throws EvaluationException {
    	if (this.previousAction != null) {
			this.previousAction.evaluate(context);
    	}
    }
    
    public void invokeNextAction(AjaxContext context) throws EvaluationException {
    	if (this.nextAction != null) {
			this.nextAction.evaluate(context);
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
    }
    
    /**
     * Removes the tab and component which corresponds to the specified index.
     * @param index
     */
    public void removeTabAt(int index)
    {
    }
    
    /**
     * Sets the component that is responsible for rendering the title for the specified tab
     * @param index
     * @param component
     */
    public void setTabComponentAt(int index, RefForm component)
    {
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
