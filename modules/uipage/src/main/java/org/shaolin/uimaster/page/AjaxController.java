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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.shaolin.bmdp.i18n.LocaleContext;
import org.shaolin.bmdp.json.JSONArray;
import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.persistence.HibernateUtil;
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
import org.shaolin.uimaster.page.monitor.RestUIPerfMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AjaxController {
	
	private static final Logger logger = LoggerFactory.getLogger(AjaxController.class);
	
	private static final String charset = "UTF-8";
	
	@RequestMapping("/ajaxservice/old")
	public void doAjaxService(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="serviceName", required=true) String serviceName
			) throws IOException {
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
			HibernateUtil.releaseSession(true);
		} 
		catch (Throwable ex) 
		{
			HibernateUtil.releaseSession(false);
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
	}
	
	@RequestMapping("/ajaxservice")
	public void doAjaxService(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="_ajaxUserEvent", required=true) String _ajaxUserEvent,
			@RequestParam(value="_framePrefix", required=false) String _framePrefix
			) throws IOException
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
				HibernateUtil.releaseSession(true);
			} 
			catch (Throwable ex) 
			{
				HibernateUtil.releaseSession(false);
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

}
