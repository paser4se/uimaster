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
package org.shaolin.uimaster.page.ajax.handlers;

import java.util.List;

import org.apache.log4j.Logger;
import org.shaolin.bmdp.datamodel.page.OpCallAjaxType;
import org.shaolin.bmdp.json.JSONArray;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.cache.PageCacheManager;
import org.shaolin.uimaster.page.exception.AjaxInitializedException;

public class WebServiceHandler implements IAjaxHandler {
	
	private static Logger log = Logger.getLogger(WebServiceHandler.class);

	public String trigger(AjaxContext context) throws AjaxHandlerException {
		String actionName = null;
		try {
			actionName = context.getRequest().getParameter("_serviceName");
			if ((actionName == null) || (actionName.length() == 0)) {
				throw new AjaxInitializedException(
						"The action name can not be empty!");
			}
			if (log.isDebugEnabled()) {
				log.debug("execute the function of web service: " + actionName);
			}
			String entityName = actionName.substring(0, actionName.lastIndexOf("."));
			String serviceName = actionName.substring(actionName.lastIndexOf(".") + 1);
			
			OpCallAjaxType callAjaxOp = PageCacheManager.getWebService(entityName, serviceName);
			Object value = null;
			try {
				if (callAjaxOp.getFilter() != null) {
					boolean passed = (boolean)callAjaxOp.getFilter().evaluate(context);
					if (!passed) {
						return "";
					}
				}
				value = callAjaxOp.getExp().evaluate(context);
			} catch (EvaluationException ex) {
				log.warn("This statement can not be evaluated: \n"+ callAjaxOp.getExp().getExpressionString());
				throw ex;
			}

			context.synchVariables();
			if ((value != null) && (value.getClass() != Void.class)) {
				// if value is not null, return directly.
				if (log.isDebugEnabled()) {
					log.debug("evaluate result: " + value.toString());
				}
				if (((value instanceof Number)) || ((value instanceof String))
						|| ((value instanceof Boolean))) {
					return "{'value': '"+value.toString()+"'}";
				} else if ((value instanceof List)) {
					JSONArray json = new JSONArray((List) value);
					return json.toString();
				} else if (value instanceof JSONArray) {
					return ((JSONArray)value).toString();
				} else if (value instanceof JSONObject) {
					return ((JSONObject)value).toString();
				} else {
					JSONObject json = new JSONObject(value);
					return json.toString();
				}
			} else {
				if (log.isDebugEnabled()) {
					context.printUiMap();
				}
				return context.getDataAsJSON();
			}
		} catch (Throwable ex) {
			String message = "Ajax executor has interrupted when execute " + actionName
					+ " web service: " + ex.getMessage();
			throw new AjaxHandlerException(message, ex);
		} 
	}

}
