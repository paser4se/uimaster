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
package org.shaolin.uimaster.page;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.shaolin.bmdp.i18n.LocaleContext;
import org.shaolin.bmdp.json.JSONArray;
import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.Registry;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.uimaster.page.ajax.Table;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.ajax.handlers.ErrorHelper;
import org.shaolin.uimaster.page.ajax.handlers.IAjaxCommand;
import org.shaolin.uimaster.page.ajax.json.IDataItem;
import org.shaolin.uimaster.page.ajax.json.IErrorItem;
import org.shaolin.uimaster.page.flow.ProcessHelper;
import org.shaolin.uimaster.page.flow.WebflowConstants;
import org.shaolin.uimaster.page.monitor.QueueKPI;
import org.shaolin.uimaster.page.monitor.RestUIPerfMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AjaxServlet extends HttpServlet implements RejectedExecutionHandler {
	
	private static final long serialVersionUID = 236538261853041089L;
	private static final Logger logger = LoggerFactory.getLogger(AjaxServlet.class);
	
	private String charset = "UTF-8";
	
	private ThreadPoolExecutor msgExecutor;
	
	/**
     * Redis session manager integration
     */
    private static java.util.function.Function redisSession;
	
	public void init() throws ServletException {
    	this.msgExecutor = WebConfig.getAjaxThreadPool(this);

    	RestUIPerfMonitor.addKPI(new QueueKPI("AjaxRequestQueue", msgExecutor.getQueue()));
		String value = getServletConfig().getInitParameter("content");
        if (value != null)
        {
            String content = value;
            //parse charset
            String[] s = content.split(";", 0);
            for (int i = 0, n = s.length; i < n; i++)
            {
                if (s[i].startsWith("charset="))
                {
                    charset = s[i].substring(8);
                    break;
                }
            }
        }
        
        if (System.getProperties().containsKey("REDIS_SESSION")) {
        	this.redisSession = (java.util.function.Function)System.getProperties().get("REDIS_SESSION");
    	}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		AsyncContext aCtx = request.startAsync(request, response); 
    	this.msgExecutor.execute(new RunAysncContext(aCtx, this));
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		AsyncContext aCtx = request.startAsync(request, response); 
    	this.msgExecutor.execute(new RunAysncContext(aCtx, this));
	}
	
	private static class RunAysncContext implements Runnable {

    	final AsyncContext aCtx;
    	final AjaxServlet ajax;
    	public RunAysncContext(AsyncContext aCtx, AjaxServlet ajax) {
    		this.aCtx = aCtx;
    		this.ajax = ajax;
    	}
    	
		@Override
		public void run() {
			HttpServletRequest request = (HttpServletRequest)aCtx.getRequest();
        	HttpServletResponse response = (HttpServletResponse)aCtx.getResponse();
        	try {
        		ajax.process(request, response);
        	} catch (Throwable e) {
        		//must not have this exception.
				logger.warn(e.getMessage(), e);
			} finally {
	        	String sessionId = request.getSession().getId();
	        	aCtx.complete();
	        	if (redisSession != null) {
	        		redisSession.apply(sessionId);
	        	}
        	}
		}
	}
	
	protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
		if (request.getProtocol().compareTo("HTTP/1.0") == 0) {
			response.setHeader("Pragma", "no-cache");
		} else if (request.getProtocol().compareTo("HTTP/1.1") == 0) {
			response.setHeader("Cache-Control", "no-cache");
		}
		response.setDateHeader("Expires", 0);
		response.setContentType("json");
		response.setCharacterEncoding(charset);
		request.setCharacterEncoding(charset);

		AppContext.register(IServerServiceManager.INSTANCE);
		
		String serviceName = request.getParameter("serviceName");
		if (serviceName != null && serviceName.trim().length() > 0) {
			try 
			{
				IAjaxCommand ajaxCommand = AjaxFactory.getIAjaxCommand(serviceName);
				if (ajaxCommand.needUserSession()) {
					HttpSession httpSession = request.getSession(false);
					if (httpSession == null || httpSession.getAttribute(AjaxContext.AJAX_COMP_MAP) == null) {
						PrintWriter out = response.getWriter();
						IDataItem dataItem = AjaxContextHelper
								.createSessionTimeOut(WebConfig.replaceWebContext(WebConfig.getTimeoutPage()));
						JSONArray array = new JSONArray();
						array.put(new JSONObject(dataItem));
						out.print(array.toString());
						return;
					}
				}
				Object result = ajaxCommand.execute(request, response);
				if (result != null) 
				{
					PrintWriter out = response.getWriter();
					out.print(result.toString());
				}
			} 
			catch (Throwable ex) 
			{
				logger.error(ex.getMessage(), ex);

				StringBuilder sb = new StringBuilder();
				sb.append("[ajax_error]");
				sb.append((new JSONException(ex)).toString());
				PrintWriter out = response.getWriter();
				out.print(sb.toString());
			} finally {
				AjaxContextHelper.removeAjaxContext();
				UserContext.unregister();
				LocaleContext.clearLocaleContext();
			}
			return;
		}
		
		HttpSession httpSession = request.getSession(false);
		if (!AjaxProcessor.EVENT_WEBSERVICE.equals(request.getParameter(AjaxContext.AJAX_USER_EVENT)) && 
				(httpSession == null || httpSession.getAttribute(AjaxContext.AJAX_COMP_MAP) == null)) {
			PrintWriter out = response.getWriter();
			IDataItem dataItem = AjaxContextHelper
					.createSessionTimeOut(WebConfig.replaceWebContext(WebConfig.getTimeoutPage()));
			JSONArray array = new JSONArray();
			array.put(new JSONObject(dataItem));
			out.print(array.toString());
			return;
		}
		HttpSession session = request.getSession();
		UserContext currentUserContext = (UserContext)session.getAttribute(WebflowConstants.USER_SESSION_KEY);
		if (currentUserContext == null) {
			currentUserContext = new UserContext();
			currentUserContext.setOrgCode(Registry.getInstance().getValue("/System/webConstant/defaultOrgCode"));
			currentUserContext.setUserRequestIP(request.getRemoteAddr());
		}
		String userLocale = WebConfig.getUserLocale(request);
		List userRoles = (List)session.getAttribute(WebflowConstants.USER_ROLE_KEY);
		String userAgent = request.getHeader("user-agent");
		boolean isMobile = MobilitySupport.isMobileRequest(userAgent);
		//add user-context thread bind
        UserContext.register(session, currentUserContext, userLocale, userRoles, isMobile);
        UserContext.setAppClient(request);
		LocaleContext.createLocaleContext(userLocale);
		
		if (request.getParameter("_ajaxUserEvent") != null) 
		{ // for new UI framework.
			boolean error = false;
			long start = System.currentTimeMillis();
			try 
			{
				String actionName = request.getParameter("_actionName");
				if (actionName != null && "exportTable".equals(actionName)) {
					response.setContentType("application/x-download");    
				    response.addHeader("Content-Disposition","attachment;filename=TableReport.xls");
				    Map<String, JSONObject> uiMap = AjaxContextHelper.getFrameMap(request);
				    JSONObject json = uiMap.get(request.getParameter("_uiid"));
				    Table table = (Table)Widget.covertFromJSON(json);
				    table.exportAsExcel(response.getOutputStream());
				} else {
		            ProcessHelper.processSyncValues(request);
		            
					UserRequestContext htmlContext = new UserRequestContext(request);
					AjaxProcessor ajxProcessor = new AjaxProcessor(htmlContext);
					response.setContentType("application/json"); 
					PrintWriter out = response.getWriter();
					out.print(ajxProcessor.execute());
				}
			} 
			catch (Throwable ex) 
			{
				error = true;
				logger.error(ex.getMessage(), ex);
//				JSONException json = new JSONException(ex);			
//				IDataItem dataItem = AjaxActionHelper.createErrorDataItem(json.toString());
//	            dataItem.setParent(ex.getMessage());
				JSONException json = new JSONException(ex);
				String uiid = request.getParameter("_uiid");
	    		String errorMsgTitle = "Error Action";
	    		String errorMsgBody = ex.getMessage(); 
	    		String exceptionTrace = json.toString(); 
	            String image = "/images/Error.png";
	            String jsSnippet = "";
	            String html = null;
	            IErrorItem dataItem = ErrorHelper.createErrorItem(uiid, errorMsgTitle, 
	            				errorMsgBody, exceptionTrace, image, jsSnippet, html);
	            JSONArray array = new JSONArray();
				array.put(new JSONObject(dataItem));
				PrintWriter out = response.getWriter();
				out.print(array.toString());
			} finally {
				long end = System.currentTimeMillis() - start;
				RestUIPerfMonitor.updateKPI(RestUIPerfMonitor.AJAX_DATA_TO_UI_COUNT, end);
				RestUIPerfMonitor.updateKPI(RestUIPerfMonitor.AJAX_DATA_TO_UI_PROCESS_TIME, end);
				RestUIPerfMonitor.updateKPI(RestUIPerfMonitor.AJAX_DATA_TO_UI_TPS, end);
				if (error) {
					RestUIPerfMonitor.updateKPI(RestUIPerfMonitor.AJAX_DATA_TO_UI_ERROR_COUNT, end);
				}
				AjaxContextHelper.removeAjaxContext();
				UserContext.unregister();
				LocaleContext.clearLocaleContext();
			}
		} else {
			//unsupported.
		}
    }

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		logger.warn("UIMaster web server is overloaded!!!!" + r);
	}
}
