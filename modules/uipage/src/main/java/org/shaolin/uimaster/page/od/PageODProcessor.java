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
import org.shaolin.bmdp.datamodel.page.PageOutType;
import org.shaolin.bmdp.i18n.ExceptionConstants;
import org.shaolin.bmdp.i18n.LocaleContext;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.EvaluationContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.ajax.Button;
import org.shaolin.uimaster.page.cache.PageCacheManager;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.cache.UIPageObject;
import org.shaolin.uimaster.page.exception.ODProcessException;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.shaolin.uimaster.page.monitor.RestUIPerfMonitor;
import org.shaolin.uimaster.page.widgets.HTMLDynamicUIItem;
import org.shaolin.uimaster.page.widgets.HTMLPanelType;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;

/**
 * Processing Data to UI/UI to Data operation.
 * 
 * page in steps:
 *  1. set the variables of page in were evaluated by web flow parameters(by context).
 *  2. set the variables of page od were evaluated by page in parameters(by context).
 *  3. execute server operations(processor).
 *  
 */
public class PageODProcessor 
{
	private static final Logger logger = Logger.getLogger(PageODProcessor.class);
	
	private UserRequestContext requestContext;
	
	private String pageName ;
	
	public PageODProcessor(UserRequestContext context, String pageName) 
	{
		this.requestContext = context;
		this.pageName = pageName;
	}

	public EvaluationContext process()throws ODProcessException 
	{
		boolean error = false;
		boolean isDataToUI = false;
		long start = System.currentTimeMillis();
		try 
		{
			if(logger.isTraceEnabled())
	        {
				logger.trace("\n\n\n");
				logger.trace("Start ui page entity processor.");
	        	requestContext.printHTMLAttributeValues();
	        }
			ODPageContext odPageContext = new ODPageContext( requestContext, pageName );
			odPageContext.setDeepLevel(0);
			odPageContext.initContext();
			
			if(!odPageContext.isEnableProcess())
			{
				if(logger.isDebugEnabled())
					logger.debug("OD mapping dosen't have configed, stop to process ui.");
				return new DefaultEvaluationContext();
			}
			if ( !odPageContext.getUIFormName().equals(requestContext.getODMapperName()) ) {
				throw new ODProcessException(ExceptionConstants.UIMASTER_ODMAPPER_052,
						new Object[]{requestContext.getODMapperName(), odPageContext.getUIFormName()});
			}
			isDataToUI = odPageContext.isDataToUI();
			if (odPageContext.isDataToUI())
			{
				UIPageObject pageObject = PageCacheManager.getUIPageObject(pageName);
				requestContext.setFormObject(pageObject.getUIForm()); //a page only need to be set at here.
				if(logger.isDebugEnabled()) {
					logger.debug("----->Execute page in.");
				}
				try
				{
					if (logger.isDebugEnabled()) {
						String[] keys = odPageContext.getLocalVariableKeys();
						if (keys != null) {
							EvaluationContext dEContext = odPageContext
									.getEvaluationContextObject(ODContext.LOCAL_TAG);
							for (int i = 0; i < keys.length; i++) {
								try {
									Object variableValue = dEContext
											.getVariableValue(keys[i]);
									if (variableValue == null) {
										logger.debug("the local variable["
												+ keys[i] + "] value is null.");
									}
								} catch (Exception e) {
								}
							}
						}
					}
    				
    				requestContext.setCurrentFormInfo(odPageContext.getUIFormName(), odPageContext.getHtmlPrefix(), "");
    				odPageContext.executePageIn();
    				
    				VariableEvaluator ee = new VariableEvaluator(odPageContext);
    				// calculate all dynamic variables in this form.
    				UIFormObject formObject = odPageContext.getUIFormObject();
    				// only search for current level excluding sub ref-entity since the evaluation context is different.
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
						HTMLWidgetType htmlWidget = formObject.getComponents().get(uiid);
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
    	                	if (newAjax.getString("type").equals(Button.class.getSimpleName())) {
    	                    	// all express must be re-calculate when click button in every time.
//    	                		((Button)newAjax).setExpressMap(expMap);
    	                	}
    	                }
    	    		}
    			}
    			finally
    			{
    			    LocaleContext.popDataLocale();
    			}
			}
			else 
			{
				try
				{
    				PageOutType outType = odPageContext.getOutNode();
					if (logger.isDebugEnabled()) {
						String[] keys = odPageContext.getLocalVariableKeys();
						if (keys != null) {
							EvaluationContext dEContext = odPageContext
									.getEvaluationContextObject(ODContext.LOCAL_TAG);
							for (int i = 0; i < keys.length; i++) {
								try {
									Object variableValue = dEContext
											.getVariableValue(keys[i]);
									if (variableValue == null)
										logger.debug("the local variable["
												+ keys[i] + "] value is null.");
								} catch (Exception e) {
								}
							}
						}
					}
					if(logger.isDebugEnabled()) {
						logger.debug("----->Execute page out: " + outType.getName());
					}
					requestContext.setCurrentFormInfo(odPageContext.getUIFormName(), odPageContext.getHtmlPrefix(), "");
					odPageContext.executePageOut(outType.getName());
    				
    				Map<String, Object> outResult = new HashMap<String, Object>();
    				String[] keys = odPageContext.getLocalVariableKeys();
					if (keys != null) {
						EvaluationContext dEContext = odPageContext
								.getEvaluationContextObject(ODContext.LOCAL_TAG);
						for (String key: keys) {
							if (!odPageContext.isOutVariable(key)) {
								continue;
							}
							try {
								Object variableValue = dEContext.getVariableValue(key);
								outResult.put(key, variableValue);
							} catch (Exception e) {
							}
						}
					}
    				requestContext.setODMapperData(outResult);
    			}
    			finally
    			{
    			    LocaleContext.popDataLocale();
    			}
			}
			
			if(logger.isTraceEnabled())
	        {
				logger.info("UI page entity processor stop.\n\n");
	        	requestContext.printHTMLAttributeValues();
	        }
			return odPageContext;
		}
		catch (Throwable e)
		{
			error = true;
			throw new ODProcessException(ExceptionConstants.UIMASTER_ODMAPPER_067, e, new Object[]{pageName});
		} 
		finally {
			long end = System.currentTimeMillis() - start;
			if (isDataToUI) {
				RestUIPerfMonitor.updateKPI(RestUIPerfMonitor.PAGE_DATA_TO_UI_COUNT, end);
				RestUIPerfMonitor.updateKPI(RestUIPerfMonitor.PAGE_DATA_TO_UI_PROCESS_TIME, end);
				RestUIPerfMonitor.updateKPI(RestUIPerfMonitor.PAGE_DATA_TO_UI_TPS, end);
				if (error) {
					RestUIPerfMonitor.updateKPI(RestUIPerfMonitor.PAGE_DATA_TO_UI_ERROR_COUNT, end);
				}
			} else {
				RestUIPerfMonitor.updateKPI(RestUIPerfMonitor.PAGE_UI_TO_DATA_COUNT, end);
				RestUIPerfMonitor.updateKPI(RestUIPerfMonitor.PAGE_UI_TO_DATA_PROCESS_TIME, end);
				if (error) {
					RestUIPerfMonitor.updateKPI(RestUIPerfMonitor.PAGE_UI_TO_DATA_ERROR_COUNT, end);
				}
			}
		}
	}

}
