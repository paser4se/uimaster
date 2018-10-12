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

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.shaolin.bmdp.datamodel.page.OpCallAjaxType;
import org.shaolin.bmdp.datamodel.page.OpInvokeWorkflowType;
import org.shaolin.bmdp.datamodel.page.OpType;
import org.shaolin.bmdp.json.JSONArray;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.persistence.HibernateUtil;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.spi.EventProcessor;
import org.shaolin.bmdp.runtime.spi.FlowEvent;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.ajax.Button;
import org.shaolin.uimaster.page.ajax.Dialog;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.PageCacheManager;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;

public class EventHandler implements IAjaxHandler {
	private static Class<?> WF_PROCESSOR = null;
	private static final String UI_INACTION_FLAG = "_uiinactionflag";
	private static Logger log = Logger.getLogger(EventHandler.class);
	public static final String AJAX_ACTION_NAME = "_actionName";

	static {
		try {
			WF_PROCESSOR = Class.forName("org.shaolin.bmdp.workflow.internal.WorkFlowEventProcessor");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String trigger(AjaxContext context) throws AjaxHandlerException {
		String actionName = context.getRequest().getParameter("_actionName");
		if ((actionName == null) || (actionName.length() == 0)) {
			throw new AjaxHandlerException(
					"The action name can not be empty!");
		}
		try {
			if (context.getRequest().getSession().getAttribute(UI_INACTION_FLAG) != null) {
				Dialog.showMessageDialog("\u5DF2\u5904\u7406\u4E2D\uFF0C\u8BF7\u7A0D\u7B49\u3002", "", Dialog.WARNING_MESSAGE, null);
				return context.getDataAsJSON();
			}
			context.getRequest().getSession().setAttribute(UI_INACTION_FLAG, "true");
			UserRequestContext userRequest = new UserRequestContext(context.getRequest());
			userRequest.setJSONStyle(true);
			UserRequestContext.UserContext.set(userRequest);
			
			return trigger0(context, actionName);
		} catch (AjaxHandlerException e1) {
			throw e1;
		} catch (Throwable e) {
			throw new AjaxHandlerException(e.getMessage(), e);
		} finally {
			context.getRequest().getSession().removeAttribute(UI_INACTION_FLAG);
			UserRequestContext.UserContext.set(null);
		}
	}
	
	public String trigger0(AjaxContext context, String actionName)
			throws Exception {
		if (context.getEntityName() == null || context.getEntityName().trim().length() == 0) {
        	throw new AjaxHandlerException(actionName + " event does not specified the page name!");
        }
		UIFormObject currentUIForm = PageCacheManager.getUIForm(context.getEntityName());
		String eventSourceId = context.getRequestData().getUiid();
		Widget w = context.getEventSource(eventSourceId);
		if (w != null) {
			if ( w.getClass() == Button.class) {
				context.setEventSource((Button)w);
			}
			if (!w.isVisible()) {
				return "{'value': 'event source does not have the privilege.'}"; 
			}
			if (w.getAttribute("disabled") != null && "true".equals(w.getAttribute("disabled"))) {
				return "{'value': 'event source does not have the privilege.'}"; 
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("executing the function of ajax call: " + actionName);
		}
		try {
		List<OpType> ops = currentUIForm.getEventHandler(actionName);
		Object value = null;
		if (ops == null) {
			Dialog.showMessageDialog("\u5F53\u524D\u4E8B\u4EF6\u5F02\u5E38\uFF0C\u8BF7\u5237\u65B0\u9875\u9762\uFF01", "", Dialog.WARNING_MESSAGE, null);
			log.warn("The action name " + actionName + " can't be found from current page!");
			return context.getDataAsJSON();
		}
		for (OpType op : ops) {
				if (op instanceof OpCallAjaxType) {
					OpCallAjaxType callAjaxOp = (OpCallAjaxType) op;
					try {
						value = callAjaxOp.getExp().evaluate(context);
						// the user transaction could happen in JAVACC expression.
						HibernateUtil.releaseSession(true);
					} catch (Throwable ex) {
						HibernateUtil.releaseSession(false);
						if (context.isInvalidEventSource()) {
							break;
						}
						log.warn("This statement can not be evaluated: \n"+ callAjaxOp.getExp().getExpressionString(), ex.getCause());
						Dialog.showMessageDialog("\u64CD\u4F5C\u5F02\u5E38\uFF0C\u8BF7\u5237\u65B0\u9875\u9762\u91CD\u8BD5\u3002", "", Dialog.WARNING_MESSAGE, null);
						//TODO: log error to alarm!
					}
				} else if (op instanceof OpInvokeWorkflowType) {
					OpInvokeWorkflowType wfOp = (OpInvokeWorkflowType) op;
					try {
						FlowEvent e = new FlowEvent(wfOp.getEventConsumer());
						//BuiltInAttributeConstant.KEY_AdhocNodeName
						e.setAttribute("_AdhocNodeName", wfOp.getAdhocNodeName());
						Object obj = wfOp.getExpression().evaluate(context);
						if (obj instanceof Map) {
							Map map = (Map)obj;
							if (log.isDebugEnabled()) {
								log.debug("Invoke workflow "+wfOp.getAdhocNodeName()+" with parameters: " + map);
							}
							if (map.size() > 0) {
								Iterator i = map.keySet().iterator();
								while (i.hasNext()) {
									String key = (String)i.next();
									Object v = map.get(key);
									if (v instanceof Serializable) {
										e.setAttribute(key, (Serializable)v);
									} else {
										log.warn("Variable " + key + " is not seriablizable.");
									}
								}
								String note = context.getRequest().getParameter("_comments");
								e.setComments((note != null && !"null".equals(note)) ? note : null);
								EventProcessor processor = (EventProcessor)AppContext.get().getService(WF_PROCESSOR);
								processor.process(e);
								
								if (e.getAttribute("_ErrorType") != null) {
									Dialog.showMessageDialog("\u64CD\u4F5C\u5F02\u5E38\uFF0C\u8BF7\u5237\u65B0\u9875\u9762\u91CD\u8BD5\u3002", "", Dialog.WARNING_MESSAGE, null);
								}
							}
						} else {
							//if (obj instanceof Boolean)
							if (log.isDebugEnabled()) {
								log.debug("Workflow action failed result: " + obj);
							}
						}
						// worklow controls the JTA session.
						// HibernateUtil.releaseSession(true);
					} catch (Throwable ex) {
						if (context.isInvalidEventSource()) {
							break;
						}
						log.warn("This("+context.getEntityName() + "." + actionName+") statement can not be evaluated: \n"+ wfOp.getExpression().getExpressionString(), ex.getCause());
						Dialog.showMessageDialog("\u64CD\u4F5C\u5F02\u5E38\uFF0C\u8BF7\u5237\u65B0\u9875\u9762\u91CD\u8BD5\u3002", "", Dialog.WARNING_MESSAGE, null);
						//TODO: log error to alarm!
					}
				}
		}

		context.synchVariables();
		if (w != null && w.getClass() == Button.class) {
			if (context.isInvalidEventSource()) {
				//((Button)w).setValue(((Button)w).getValue() + " Invalid!");
			} else {
				((Button)w).setAsEnabled();
			}
		}
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
			if (context.itemSize() == 0) {
				// at least needs one.
				context.executeJavaScript("");
			}
			return context.getDataAsJSON();
		}
		} catch (Throwable ex) {
			String message = "Ajax executor has interrupted when execute " + actionName
					+ " ajax calling: " + ex.getMessage();
			throw new AjaxHandlerException(message, ex);
		}
	}

}
