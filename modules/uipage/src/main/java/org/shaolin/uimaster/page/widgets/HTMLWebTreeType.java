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

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.shaolin.bmdp.datamodel.page.UITableActionType;
import org.shaolin.bmdp.json.JSONArray;
import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.ajax.TreeItem;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.UIVariableUtil;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;

public class HTMLWebTreeType extends HTMLWidgetType {
	
	private static final long serialVersionUID = 1587046878874940935L;

	private static final Logger logger = Logger.getLogger(HTMLWebTreeType.class);

	public HTMLWebTreeType(String id) {
		super(id);
	}

	@Override
	public void generateBeginHTML(UserRequestContext context, UIFormObject ownerEntity, int depth) {
		
	}
	
	
	@Override
	public void generateEndHTML(UserRequestContext context, UIFormObject ownerEntity, int depth) {
		try {
			String prefix = context.getHTMLPrefix();
			String selectedNodeEvent = (String)this.getAttribute("selectedNode");
			String overrided = getEventListener("onclick");
			if ((overrided != null) && (overrided.length() > 0))
				selectedNodeEvent = getReconfigurateFunction(overrided);
			else {
				selectedNodeEvent = "defaultname." + prefix + selectedNodeEvent + "(tree, e)";
			}
			String dblselectedNodeEvent = (String)this.getAttribute("dblselectedNode");
			String deleteNodeEvent = (String)this.getAttribute("deleteNode");
			String addNodeEvent = (String)this.getAttribute("addNode");
			if ((addNodeEvent != null) && (addNodeEvent.length() > 0)) {
				overrided = getEventListener(addNodeEvent);
				if ((overrided != null) && (overrided.length() > 0))
					addNodeEvent = getReconfigurateFunction(overrided);
				else {
					addNodeEvent = "defaultname." + prefix + addNodeEvent + "(tree, e)";
				}
			}
			String refreshNodeEvent = (String)this.getAttribute("refreshNode");
			List<UITableActionType> actions = (List<UITableActionType>)this.getAttribute("actions");
			String nodeIcon = (String)this.getAttribute("nodeIcon");
			String itemIcon = (String)this.getAttribute("itemIcon");
			Boolean isopened= (Boolean)this.getAttribute("opened");
			
			if (actions != null && actions.size() > 0) {
				String htmlPrefix = prefix.replace('.', '_');
				String htmlId = prefix.replace('.', '_') + this.getUIID();
				String defaultBtnSet = "defaultBtnSet_" + htmlId;
				context.generateHTML("<div class=\"ui-widget-header ui-corner-all\">");
				if (UserContext.isMobileRequest()) {
					context.generateHTML("<div id=\""+defaultBtnSet+"\" data-role=\"controlgroup\" data-type=\"horizontal\">");
					for (UITableActionType action: actions){
						context.generateHTML("<a href=\"javascript:defaultname.");
						context.generateHTML(prefix + action.getFunction());
						context.generateHTML("('" + prefix + this.getUIID() + "');\" class=\"ui-btn ui-corner-all\">");
						String i18nProperty = UIVariableUtil.getI18NProperty(action.getTitle());
						context.generateHTML(i18nProperty);
						context.generateHTML("</a>");
					}
					context.generateHTML("</div>");
				} else {
					context.generateHTML("<span style=\"display:none;\">");
					for (UITableActionType action: actions){
						context.generateHTML("<span event=\"javascript:defaultname.");
						context.generateHTML(prefix + action.getFunction());
						context.generateHTML("('" + prefix + this.getUIID() + "');\" title='");
						String i18nProperty = UIVariableUtil.getI18NProperty(action.getTitle());
						context.generateHTML(i18nProperty);
						context.generateHTML("'></span>");
						HTMLUtil.generateTab(context, depth + 3);
						String btnId = htmlPrefix + action.getUiid();
						context.generateHTML("<input type=\"radio\" name=\""+defaultBtnSet+"\" id=\""+ btnId + "\" ");
						context.generateHTML("onclick=\"javascript:defaultname.");
						context.generateHTML(prefix + action.getFunction());
						context.generateHTML("('" + prefix + this.getUIID() + "');\" title='");
						context.generateHTML(i18nProperty);
						context.generateHTML("' icon=\""+action.getIcon()+"\">");
						context.generateHTML("<label for=\""+ btnId + "\">");
						context.generateHTML(i18nProperty);
						context.generateHTML("</label></input>");
					}
					context.generateHTML("</span>");
				}
				context.generateHTML("</div>");
			}
			
			HTMLUtil.generateTab(context, depth);
			context.generateHTML("<div id=\"");
			context.generateHTML(getName());
			context.generateHTML("\" class=\"uimaster_tree\">");
			
			List<TreeItem> result = (List<TreeItem>)this.removeAttribute("initValue");
			JSONArray jsonArray = new JSONArray(result);
			
			context.generateHTML("<div style='display:none;' clickevent=\"");
			context.generateHTML(selectedNodeEvent);
			context.generateHTML("\" dblclickevent=\"defaultname.");
			context.generateHTML(prefix + dblselectedNodeEvent);
			context.generateHTML("(tree, e)\" ");
			if (addNodeEvent != null) {
		        context.generateHTML(" addnodeevent=\"");
		        context.generateHTML(addNodeEvent);
		        context.generateHTML("\"");
		    }
			if (deleteNodeEvent != null) {
				context.generateHTML(" deletenodeevent0=\"");
				context.generateHTML(deleteNodeEvent);
				context.generateHTML("\" deletenodeevent=\"defaultname.");
				context.generateHTML(prefix + deleteNodeEvent);
				context.generateHTML("(tree, e)\"");
			}
			if (refreshNodeEvent != null) {
				context.generateHTML(" refreshnodeevent0=\"");
				context.generateHTML(refreshNodeEvent);
				//TODO: add more actions
				context.generateHTML("\" refreshnodeevent=\"defaultname.");
				context.generateHTML(prefix + refreshNodeEvent);
				context.generateHTML("(tree, e)\"");
			}
			context.generateHTML(">");
			context.generateHTML(jsonArray.toString());
			context.generateHTML("</div></div>");
		} catch (Exception e) {
			logger.error("error in entity: " + getUIEntityName(), e);
		}
	}
	
	public JSONObject createJsonModel(VariableEvaluator ee) throws JSONException 
    {
		if (!needAjaxSupport()) {
    		Object oneditable = this.getAttribute("oneditable");
            if (oneditable == null || oneditable.toString().equalsIgnoreCase("false")){
            	return null;
            }
    	}
//        Tree tree = new Tree(getName(), Layout.NULL, 
//        		(ExpressionType) this.removeAttribute("initExpr"),
//        		(ExpressionType) this.removeAttribute("expandExpr"));
//
//        tree.setReadOnly(isReadOnly());
//        tree.setUIEntityName(getUIEntityName());
//
//        tree.setListened(true);
//
//        List result = (List)this.getAttribute("initValue");
//        Object lastObject = result.get(result.size()-1);
//        if (lastObject instanceof Map) {
//        	tree.setDataModel((Map)lastObject);
//        	result.remove(result.size()-1);
//        }
        
		return super.createJsonModel(ee);
    }

	
	public static Map sortByValue(Map map) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				Object k1 = ((Map.Entry) (o1)).getKey();
				Object v1 = ((Map.Entry) (o1)).getValue();
				if (v1 instanceof Comparable) {
					// compare the items
					return ((Comparable) v1).compareTo(((Map.Entry) (o2)).getValue());
				} else {
					// compare the nodes for map object.
					return ((Comparable) k1).compareTo(((Map.Entry) (o2)).getKey());
				}
			}
		});
		Map result = new LinkedHashMap();

		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
