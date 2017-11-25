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
package org.shaolin.uimaster.page.od;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.shaolin.bmdp.i18n.ExceptionConstants;
import org.shaolin.bmdp.i18n.LocaleContext;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.exception.ODEntityProcessException;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.shaolin.uimaster.page.widgets.HTMLDynamicUIItem;
import org.shaolin.uimaster.page.widgets.HTMLPanelType;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;

/**
 * Processing Data to UI/UI to Data operation.
 * 
 */
public class ODProcessor 
{
	private static final Logger logger = Logger.getLogger(ODProcessor.class);
	
	private UserRequestContext requestContext;

	private String odEntityName;
	
	private String uiid;// the ui widget id defined in parent form or page.
	
	private int deepLevel;
	
	public ODProcessor(UserRequestContext context, String odEntityName, String uiid, int deepLevel) 
	{
		this.requestContext = context;
		this.odEntityName = odEntityName;
		this.uiid = uiid;
		this.deepLevel = deepLevel;
	}

	public ODEntityContext process() throws ODEntityProcessException 
	{
		String orignalHtmlPrefix = requestContext.getHTMLPrefix();
		try 
		{
//			IODEntityPlugin plugger = new ODEntityPlugin(); 
			ODEntityContext odContext = new ODEntityContext(odEntityName, requestContext);
			odContext.setDeepLevel(++deepLevel);//next deep level.
			if (logger.isDebugEnabled()) {
				logger.debug("\n\n");
				logger.debug("Start od entity processor( **[the depth of level is " + odContext.getDeepLevel() + ".]** ).");
			}
			
			if (orignalHtmlPrefix != null && orignalHtmlPrefix.trim().length() > 0) {
				if (orignalHtmlPrefix.equals(this.uiid + ".")) {
					//do nothing.
				} else {
					requestContext.setCurrentFormInfo(odEntityName, orignalHtmlPrefix + this.uiid + ".", "");
				}
			} else {
				requestContext.setCurrentFormInfo(odEntityName, this.uiid + ".", "");
			}
			LocaleContext.pushDataLocale(odContext.evalDataLocale());
			odContext.initContext();
			
			if (odContext.isDataToUI())
			{
				if(logger.isDebugEnabled()) {
		    		logger.debug("----->Processing od entity data to ui...");
				}
				
				odContext.executeDataToUI();
				
				VariableEvaluator ee = new VariableEvaluator(odContext);
				// calculate all dynamic variables in this form.
				UIFormObject formObject = odContext.getUIFormObject();
				// only search for current level excluding sub ref-entity.
	            List<String> componentIds = formObject.getAllComponentID();
		    		for(String compId : componentIds) {
		    			Map propMap = formObject.getComponentProperty(compId);
		    			Map i18nMap = formObject.getComponentI18N(compId);
		    			Map expMap = formObject.getComponentExpression(compId);
		    			Map<String, Object> tempMap = new HashMap<String, Object>();
		    			if (expMap != null && expMap.size() > 0) {
		    				HTMLUtil.evaluateExpression(propMap, expMap, tempMap, ee);
		    			}
		    			if (i18nMap != null && i18nMap.size() > 0) {
		    				HTMLUtil.internationalization(propMap, i18nMap, tempMap, requestContext);
		    			}
		    			// added the combined dynamic attributes for ui widget.
		    			// so, we can access these value through HTMLWidgetType.getAttribute(name);
		    			String uiid = requestContext.getHTMLPrefix() + compId;
					requestContext.addAttribute(uiid, tempMap);
					HTMLWidgetType htmlWidget = formObject.getHTMLComponent(compId);
					if (htmlWidget.getClass() == HTMLPanelType.class && ((HTMLPanelType)htmlWidget).hasDynamicUI()) {
			        	String filter = (String)requestContext.getAttribute(htmlWidget.getName(), "dynamicUIFilter");
			    		if (filter == null)
			    			filter = "";
			        	List<HTMLDynamicUIItem> dynamicItems = formObject.getDynamicItems(compId, filter);
			        	((HTMLPanelType)htmlWidget).setDynamicItems(dynamicItems);
			        } 
					
	    				JSONObject newAjax = htmlWidget.createJsonModel(ee);
	                if (newAjax != null) {
	                	requestContext.addAjaxWidget(htmlWidget.getName(), newAjax);
	                }
		    		}
			}
			else 
			{
				if(logger.isDebugEnabled()) {
		    		logger.debug("----->Processing od entity ui to data...");
				}
				
				odContext.executeUITOData();
				requestContext.setODMapperData(odContext.getLocalVariableValues());	
			}
			
			if(logger.isDebugEnabled()) {
	    		logger.debug("OD entity processor stop ( **[the depth of level is "+odContext.getDeepLevel()+".]** ).");
			}
			// save the context for either the ui form variables evaluation or ajax loading.
//			requestContext.(requestContext.getHTMLPrefix() + evaContext.getUiEntity().getUIID() + ".", evaContext);
						
			return odContext;
		}
		catch(Throwable e)
		{
			throw new ODEntityProcessException(
			        ExceptionConstants.UIMASTER_ODMAPPER_068, e, new Object[]{odEntityName});
		}
		finally 
		{
			requestContext.resetCurrentFormInfo();
			LocaleContext.popDataLocale();
		}
	}
	
}
