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

import java.awt.ComponentOrientation;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.shaolin.bmdp.i18n.LocaleContext;
import org.shaolin.bmdp.i18n.ResourceUtil;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.Registry;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.EvaluationContext;
import org.shaolin.uimaster.html.layout.IUISkin;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.cache.UIPageObject;
import org.shaolin.uimaster.page.exception.AjaxException;
import org.shaolin.uimaster.page.exception.UIComponentNotFoundException;
import org.shaolin.uimaster.page.exception.UIPageException;
import org.shaolin.uimaster.page.flow.WebflowConstants;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.shaolin.uimaster.page.monitor.RestUIPerfMonitor;
import org.shaolin.uimaster.page.od.PageODProcessor;
import org.shaolin.uimaster.page.od.formats.FormatUtil;
import org.shaolin.uimaster.page.widgets.HTMLReferenceEntityType;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageDispatcher {

	private static Logger logger = LoggerFactory.getLogger(PageDispatcher.class);
	
	private final UIFormObject formObject;
	
	private final UIPageObject pageObject;
	
	private final EvaluationContext evaContext;
	
	public PageDispatcher(UIFormObject form, EvaluationContext evaContext) {
		this.formObject = form;
		this.pageObject = null;
		this.evaContext = evaContext;
	}
	
	public PageDispatcher(UIPageObject pageObject) {
		this.formObject = null;
		this.pageObject = pageObject;
		this.evaContext = new DefaultEvaluationContext();
	}
	
	/**
	 * only for page embeded form
	 * 
	 * @param context
	 * @param depth
	 * @throws JspException
	 */
	void forwardForm(UserRequestContext context, int depth) throws UIPageException
    {
        forwardForm(context, depth, null, null);
    }

	/**
	 * HTMLUIEntity.forward
	 * 
	 * @param context
	 * @param depth
	 * @param readOnly
	 * @param parent
	 * @throws JspException
	 */
    public void forwardForm(UserRequestContext context, int depth, Boolean readOnly,
            HTMLReferenceEntityType parent) throws UIPageException
    {
        try {
        	if (formObject == null) {
        		logger.warn("Please invoke UIEntityObject.forward");
        		return;
        	}
			if (logger.isDebugEnabled()) {
				logger.debug("<---HTMLUIEntity.forward--->start to access the uientity: "
						+ formObject.getName());
			}
			// set a null value for all tables as a mark.
			if (evaContext instanceof DefaultEvaluationContext 
					&& !((DefaultEvaluationContext)evaContext).hasVariable("tableCondition")) {
				evaContext.setVariableValue("tableCondition", null);
			}
            VariableEvaluator ee = new VariableEvaluator(this.evaContext);
            // setReconfigurablePropertyValue(context, appendComponentMap);

            Boolean realReadOnly = readOnly;
			if (realReadOnly == null) {
				String selfReadOnly = (String)formObject.getComponentProperty(formObject.getBodyName()).get("readOnly");
				realReadOnly = Boolean.valueOf(ee.evaluateReadOnly(selfReadOnly));
			}
            if (logger.isTraceEnabled()) {
                logger.trace("The readOnly value for component: "
                        + formObject.getBodyName() + " in the uientity: " + formObject.getName() + " is "
                        + (realReadOnly == null ? "null" : realReadOnly.toString()));
            }

            HTMLWidgetType htmlComponent = null;
            if (parent != null) {
            	try {
            		//context.getHTMLPrefix() + 
            		htmlComponent = context.getHtmlWidget(formObject.getBodyName());
            	} catch (UIComponentNotFoundException e) {
            		String formId = parent.getName() + "." + formObject.getBodyName();
            		throw new IllegalStateException("Make sure the od mapping is invoked for this form: " + formId, e);
            	}
            	htmlComponent.setHTMLLayout(parent.getHTMLLayout());

                String visible = (String)parent.getAttribute("visible");
                if (visible != null) {
					if ("true".equalsIgnoreCase(visible)) {
						visible = "true";
					}
                    htmlComponent.addAttribute("visible", visible);
                }
            } else {
            	htmlComponent = context.getHtmlWidget(formObject.getBodyName());
            }
            
            if (UserContext.isMobileRequest() && parent == null) {
            	// jquery mobile page supported.
//            	propMap.put("data-role", "main");
//            	propMap.put("class", "ui-content");
            }
            if (realReadOnly == null) {
            	realReadOnly = htmlComponent.isReadOnly();
            }
            IUISkin uiskinObj = htmlComponent.getUISkin();
            if (uiskinObj != null) {
				try {
					uiskinObj.generatePreCode(htmlComponent);
				} catch (Exception e) {
					logger.error("uiskin error: ", e);
				}
			} 
			htmlComponent.generateBeginHTML(context, formObject, depth);

            formObject.getBodyLayout().generateHTML(context, depth + 1, realReadOnly, uiskinObj, htmlComponent);
            HTMLUtil.generateTab(context, depth);

            htmlComponent.generateEndHTML(context, formObject, depth);
			if (uiskinObj != null) {
				try {
					uiskinObj.generatePostCode(htmlComponent);
				} catch (Exception e) {
					logger.error("uiskin error: ", e);
				}
			} 
        }
        catch (Exception e)
        {
            throw new UIPageException("<---HTMLUIEntity.forward--->Exception occurred while accessing form: " + 
            			formObject.getName(), e);
        }
    }
    
    /**
     * HTMLUIPage.forward
     * 
     * @param context
     */
    public void forwardPage(UserRequestContext context) throws UIPageException
    {
    	boolean error = false;
		long start = System.currentTimeMillis();
        try
        {
        	if (pageObject == null) {
        		logger.warn("Please invoke UIPageObject.forward");
        		return;
        	}
            String entityName = pageObject.getRuntimeEntityName();
			if (logger.isDebugEnabled()) {
				logger.debug("<---HTMLUIPage.forward--->start to access the uipage: {}", entityName);
			}

            String userLocale = LocaleContext.getUserLocale();
            ComponentOrientation ce = ComponentOrientation.getOrientation(ResourceUtil.getLocaleObject(userLocale));
            context.setLeftToRight(ce.isLeftToRight());
            UserRequestContext.UserContext.set(context);
            
			if (logger.isDebugEnabled()) {
				logger.debug("Call od to set data for the uipage: {}",
						entityName);
			}
            String frameName = context.getRequest().getParameter(WebflowConstants.FRAME_NAME);
            boolean frameMode = false;
            String superPrefix = null;
            String framePrefix = context.getRequest().getParameter("_framePrefix");
            String frameTarget = context.getRequest().getParameter("_frameTarget");
			if (frameTarget != null && !frameTarget.equals("null")) {
				superPrefix = frameTarget;
			} else if (framePrefix != null && !framePrefix.equals("null")
					&& framePrefix.length() != 0) {
				superPrefix = framePrefix;
				if (frameName != null) {
					superPrefix += '.' + frameName;
				}
			} else if (frameName != null) {
				superPrefix = frameName;
			}

            PageODProcessor pageODProcessor = new PageODProcessor(context, entityName);
            EvaluationContext evaContext = pageODProcessor.process();

            Map<String, UIFormObject> referenceEntityMap = new HashMap<String, UIFormObject>();
            referenceEntityMap.put(entityName, pageObject.getUIFormObject());
            pageObject.getUIFormObject().parseReferenceEntity(referenceEntityMap);
            context.setRefEntityMap(referenceEntityMap);

            String charset = Registry.getInstance().getEncoding();
            String actionPath = getActionPath(context);
            context.getRequest().setAttribute(WebflowConstants.FORM_URL, actionPath);
            
            //prepare the static values for client javascript
            Calendar currentDate = Calendar.getInstance();
            Map constraintStyleMap = FormatUtil.getConstraintFormat(null,null);
            String language = HTMLUtil.getLanguageCode(ResourceUtil.getLocale(userLocale));
            DecimalFormatSymbols dfs = HTMLUtil.getLocaleFormatSymbol(ResourceUtil.getLocale(userLocale));

            context.generateHTML("<!DOCTYPE html>\n");
            context.generateHTML("<html>\n<head>\n<title>");
            UIFormObject uiForm = pageObject.getUIForm();
            String title = uiForm.getDescription();
			if (uiForm.getDescriptionExpr() != null) {
            	Object t1 = uiForm.getDescriptionExpr().evaluate(evaContext);
            	title = (t1 != null ? title.toString() : "");
				context.generateHTML(title);
            } else {
				context.generateHTML(title);
            }
			String pageHintLink = uiForm.getPageHintLink();
            //is the title need i18n? -- the name should not be i18n, but the <title> should be i18n
            //currently all uipages are embedded in frame, so the title can't be seen by user
            context.generateHTML("</title>\n");
            context.generateHTML("<link rel=\"shortcut icon\" href=\"favicon.ico\" type=\"image/x-icon\" />\n");
            context.generateHTML("<link rel=\"apple-touch-icon\" href=\"favicon.ico\">\n");
            context.generateHTML("<!--[if lt ie 9]>");
            context.generateHTML("<script src=\"");
            if (UserContext.isMobileRequest() && UserContext.isAppClient()) {
            	context.generateHTML(WebConfig.getAppContextRoot(context.getRequest()));
            } else {
            	context.generateHTML(WebConfig.getResourceContextRoot());
            }
            context.generateHTML("/js/html5support.js\"></script>\n");
        	context.generateHTML("<![endif]-->\n");
            context.generateHTML("<script type=\"text/javascript\">\nvar defaultname;\nvar USER_CONSTRAINT_IMG=\"");
            context.generateHTML((String)constraintStyleMap.get("constraintSymbol"));
            context.generateHTML("\";\nvar USER_CONSTRAINT_LEFT=");
            context.generateHTML(((Boolean)constraintStyleMap.get("isLeft")).toString());
            context.generateHTML(";\nvar LANG=\"");
            context.generateHTML(language);
            context.generateHTML("\";\nvar CURRENCY_GROUP_SEPARATOR=\""+dfs.getGroupingSeparator()+"\"");
            context.generateHTML(";\nvar CURRENCY_MONETARY_SEPARATOR=\""+dfs.getMonetaryDecimalSeparator()+"\"");
            context.generateHTML(";\nvar CURTIME=");
            context.generateHTML(String.valueOf(currentDate.getTimeInMillis()));
            context.generateHTML(";\nvar TZOFFSET=");
            context.generateHTML(String.valueOf(currentDate.getTimeZone().getOffset(currentDate.getTimeInMillis())));
            context.generateHTML(";\nvar IS_MOBILEVIEW=");
            context.generateHTML(String.valueOf(UserContext.isMobileRequest()));
            context.generateHTML(";\nvar MOBILE_APP_TYPE=\"");
            context.generateHTML(UserContext.getAppClientType());
            context.generateHTML("\";\nvar WEB_CONTEXTPATH=\"");
            context.generateHTML(WebConfig.getWebContextRoot());
            context.generateHTML("\";\nvar RESOURCE_CONTEXTPATH=\"");
            if (UserContext.isMobileRequest() && UserContext.isAppClient()) {
            	context.generateHTML(WebConfig.getAppContextRoot(context.getRequest()));
            } else {
            	context.generateHTML(WebConfig.getResourceContextRoot());
            }
            context.generateHTML("\";\nvar UPLOAD_CONTEXTPATH=\"");
            context.generateHTML(WebConfig.getUploadFileRoot());
            context.generateHTML("\";\nvar PAGE_HINT=\"");
            context.generateHTML(pageHintLink);
            context.generateHTML("\";\nvar FRAMEWRAP=\"");
            context.generateHTML(WebConfig.replaceWebContext(WebConfig.getFrameWrap()));
            context.generateHTML("\";\nvar IS_SERVLETMODE=true;\nvar AJAX_SERVICE_URL=\"");
            if (UserContext.isAppClient()) {
	            String address = context.getRequest().getLocalAddr() + ":" + context.getRequest().getLocalPort();
	            if (WebConfig.isHttps()) {
	            	address = "https://" + address;
	            } else {
	            	address = "http://" + address;
	            }
	            context.generateHTML(address);
            }
	        context.generateHTML(WebConfig.replaceWebContext(WebConfig.getAjaxServiceURI()));
	        context.generateHTML("\";\n</script>\n");
            context.generateHTML("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=");
            context.generateHTML(charset);
            context.generateHTML("\">\n");
            context.generateHTML("<meta http-equiv=\"x-ua-compatible\" content=\"ie=7\" />\n");
            
            if (logger.isDebugEnabled())
            {
                logger.debug("import css to the uipage: " + entityName);
            }
            if (UserContext.isMobileRequest()) {
            	context.generateHTML("<meta name=\"viewport\" id=\"WebViewport\" content=\"width=device-width,initial-scale=1.0,minimum-scale=0.5,maximum-scale=1.0,user-scalable=1\" />\n");
            	context.generateHTML("<meta name=\"apple-mobile-web-app-title\" content=\"UIMaster\">\n");
            	context.generateHTML("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n");
            	context.generateHTML("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black-translucent\">\n");
            	context.generateHTML("<meta name=\"format-detection\" content=\"telephone=no\">\n");
            	if (UserContext.isAppClient()) {
            		context.generateHTML(WebConfig.replaceAppCssWebContext(context.getRequest(), pageObject.getMobAppPageCSS().toString()));
            	} else {
            		context.generateHTML(pageObject.getMobPageCSS().toString());
            	}
            } else {
        		context.generateHTML(pageObject.getPageCSS().toString());
            }
            
            if (logger.isDebugEnabled())
            {
                logger.debug("import js to the uipage: " + entityName);
            }
            importAllJS(context, 0);

            if (!frameMode)
            { 
                context.generateHTML("<script type=\"text/javascript\">\n");
                context.generateHTML("function checkUIMasterReady0() \n{\n");
                context.generateHTML("    if (jQuery && UIMaster.El && checkUIMasterReady) {\n");
                context.generateHTML("       initPage();\n");
                context.generateHTML("    } else { window.setTimeout(checkUIMasterReady0,500);}\n");
                context.generateHTML("}\n");
                context.generateHTML("function initPage() \n{\n");
                context.generateHTML("    UIMaster.ui.mask.open();\n");
                context.generateHTML("    UIMaster.addResource(\"" + entityName + "\");\n");
                context.generateHTML("    getElementList();\n    defaultname = new ");
                context.generateHTML(entityName.replaceAll("\\.", "_"));
                String clickRemembered = "";
                if (context.getRequest().getQueryString() != null) {
	                String[] queryStrParams = context.getRequest().getQueryString().split("&");
	                for (String param : queryStrParams) {
	                	if (param.startsWith("_clickRemembered=")) {
	                		clickRemembered = param.substring("_clickRemembered=".length());
	                		break;
	                	}
	                }
                }
                context.generateHTML("(\"\");\n    defaultname.initPageJs();\n    dPageLinkOnPage('"+clickRemembered+"');\n}\n");
                context.generateHTML("function finalizePage() \n{\n    defaultname.finalizePageJs();\n    releaseMem();\n}\n");
                if (UserContext.isMobileRequest()) {
                	context.generateHTML("window.onload=checkUIMasterReady0;\n");
                	context.generateHTML("window.onunload=finalizePage;\n");
                }
                context.generateHTML("</script>\n</head>\n");
                context.generateHTML("<body");
                if (UserContext.isMobileRequest()) {
                	context.generateHTML(" data-role=\"page\">\n");
                } else {
                	context.generateHTML(" onload=\"checkUIMasterReady0()\" onunload=\"finalizePage()\">\n");
                }
                context.generateHTML(genLoaderMask());
            }
            if (!checkSupportAccess(context.getRequest())) {
            	context.generateHTML("<H1 style=\"color:red;\">Ops!!! We are so sorry that your browser is unsupported(");
            	context.generateHTML(context.getRequest().getHeader("User-Agent"));
            	context.generateHTML("). Please choose Firefox, Chrome, Safari, IE 9 and above.</H1>");
            	context.generateHTML("<H1 style=\"color:red;\">\u60A8\u597D\uFF0C\u672C\u7CFB\u7EDF\u4E0D\u652F\u6301\u4F4E\u7248\u672C\u7684\u6D4F\u89C8\u5668(");
            	context.generateHTML(context.getRequest().getHeader("User-Agent"));
            	context.generateHTML(")\u3002\u8BF7\u9009\u62E9\u9AD8\u7248\u672C\u7684\u6D4F\u89C8\u5668\u4F7F\u7528\uFF0C\u8C22\u8C22\uFF01</H1>");
            	context.generateHTML("\n</body>\n</html>\n");
            	return;
            }
            
            HTMLUtil.generateJSBundleConstants(context);
            
            // function made for solution of hinting back button for only page except the iframes.
            String chunk = (String)context.getRequest().getAttribute(WebflowConstants.SOURCE_CHUNK_NAME);
            String node = (String)context.getRequest().getAttribute(WebflowConstants.SOURCE_NODE_NAME);
            if (chunk != null && node != null && superPrefix == null) {
        		context.getRequest().getSession().setAttribute(WebflowConstants.USER_PAGE, chunk+"."+node);
            }
            if (UserContext.isMobileRequest()) {
            	context.generateHTML("<form action=\"" + actionPath + "\" method=\"post\" name=\"everything\" onsubmit=\"return false;\"");
            } else {
            	context.generateHTML("<form action=\"" + actionPath + "\" method=\"post\" name=\"everything\" onsubmit=\"return false;\"");
            }
            if (superPrefix != null)
            {
                context.generateHTML(" _framePrefix=\"" + superPrefix + "\"");
            }
            if (UserContext.isAppClient()) {
            	context.generateHTML(" style=\"height:" + context.getRequest().getParameter("app_height") + "px;overflow-y:auto;\"");
            }
            context.generateHTML(">\n");
            
            context.generateHTML("<input type=\"hidden\" name=\"_pagename\" value=\"");
            context.generateHTML(entityName);
            context.generateHTML("\"/>\n<input type=\"hidden\" name=\"_outname\" value=\"\"/>\n");
            context.generateHTML("<input type=\"hidden\" id=\"isLeftToRight\" name=\"isLeftToRight\" value=\"");
			if (context.isLeftToRight()) {
				context.generateHTML("true\"/>\n");
			} else {
				context.generateHTML("false\"/>\n");
			}
			
			if (UserContext.isMobileRequest() && !UserContext.isAppClient() && pageObject.needBackButton()) {
				context.generateHTML("<div data-role=\"header\" class=\"ui-widget-header\"><a href=\"javascript:void(0);\" onclick=\"javascript:UIMaster.goBack(this);\" data-role=\"button\" data-rel=\"back\" class=\"ui-btn ui-icon-back ui-btn-icon-left\">\u8FD4\u56DE</a>");
				context.generateHTML("<div class=\"uimaster_mob_title\">"+ title + "</div></div>");
			}

			// ajax handler.
            HttpSession session = context.getRequest().getSession();
            AjaxProcessor ajxProcessor = (AjaxProcessor) session.getAttribute("_ajaxProcessorInWebflow");
            if (ajxProcessor != null)
            {
                try
                {
                    session.removeAttribute("_ajaxProcessorInWebflow");
                    ajxProcessor.execute();
                }
                catch (AjaxException e)
                {
                    //Nothing to do.
                }
            }
            Map ajaxWidgetMap = AjaxContextHelper.getAjaxWidgetMap(session);
            Map<String, JSONObject> pageComponentMap = context.getPageAjaxWidgets();
            if(ajaxWidgetMap == null)
            {
                ajaxWidgetMap = new HashMap();
                if (superPrefix == null)
                {
                	AjaxContextHelper.addCachePage(session, AjaxContext.GLOBAL_PAGE);
                    ajaxWidgetMap.put(AjaxContext.GLOBAL_PAGE, pageComponentMap);
                }
                else
                {
                	AjaxContextHelper.addCachePage(session, superPrefix);
                    ajaxWidgetMap.put(superPrefix, pageComponentMap);
                }
            }
            else
            {
                if (frameTarget == null || frameTarget.equals("null"))
                {
                    frameTarget = superPrefix;
                }

                if(frameTarget == null || frameTarget.equals("null"))
                {
                    if(logger.isDebugEnabled())
                        logger.debug("Remove all components in cache of ui map.");
                    ajaxWidgetMap.clear();
                    
                    ajaxWidgetMap.put(AjaxContext.GLOBAL_PAGE, pageComponentMap);
                    AjaxContextHelper.removeAllCachedPages(session);
                    AjaxContextHelper.addCachePage(session, AjaxContext.GLOBAL_PAGE);
                }
                else
                {
	                	AjaxContextHelper.addCachePage(session, frameTarget);
	                	ajaxWidgetMap.put(frameTarget, pageComponentMap);
                }
            }
            
            session.setAttribute(AjaxContext.AJAX_COMP_MAP, ajaxWidgetMap);
            if (superPrefix != null)
            {
                context.getRequest().setAttribute("_framePagePrefix", superPrefix);
            }
            AjaxContext.registerPageAjaxContext(entityName, pageComponentMap, context.getRequest());
            PageDispatcher dispatcher = new PageDispatcher(pageObject.getUIFormObject(), evaContext);
            dispatcher.forwardForm(context, 0);
            session.setAttribute(AjaxContext.AJAX_COMP_MAP, ajaxWidgetMap);//sync again!
            
            context.generateHTML("\n</form>");
            if (!frameMode)
            {
                context.generateHTML("\n</body>\n</html>\n");
            }
            else
            {
            }
            context.generateHTML("");
            
            if (logger.isTraceEnabled()) {
            	debugEnableToSerializable(context.getPageAjaxWidgets());
            }
        }
        catch ( Exception e )
        {
        	error = true;
            logger.warn("<---HTMLUIPage.forward--->Be interrupted when access uipage: " + pageObject.getRuntimeEntityName());
            throw new UIPageException(pageObject.getRuntimeEntityName() + " : " + e.getMessage(), e);
        }
        finally {
			long end = System.currentTimeMillis() - start;
			RestUIPerfMonitor.updateKPI(RestUIPerfMonitor.PAGE_RENDERING, end);
			RestUIPerfMonitor.updateKPI(RestUIPerfMonitor.PAGE_RENDERING_COUNT, end);
			if (error) {
				RestUIPerfMonitor.updateKPI(RestUIPerfMonitor.PAGE_RENDERING_ERROR_COUNT, end);
			}
		}
    }
    
    private boolean checkSupportAccess(HttpServletRequest request) {
    	if (request.getHeader("User-Agent") == null) {
    		return true;
    	}
    	String userAgent = request.getHeader("User-Agent").toLowerCase();
    	if (userAgent.indexOf("msie") != -1
    			&& (userAgent.indexOf("msie 5.0") != -1 
    				|| userAgent.indexOf("msie 6.0") != -1 
    				|| userAgent.indexOf("msie 7.0") != -1
    				|| userAgent.indexOf("msie 8.0") != -1)) {
    		return false;
    	}
    	return true;
    }
    
    private String getActionPath(UserRequestContext context) throws JspException
    {
        HttpServletResponse response = context.getResponse();
        HttpServletRequest request = context.getRequest();;

        if ( logger.isDebugEnabled() )
        {
            logger.debug("Get actionPath from servletContext for the uipage: {}", pageObject.getRuntimeEntityName());
        }
        String actionPath = WebConfig.replaceWebContext(WebConfig.getActionPath());
        if ( actionPath == null )
        {
            logger.error("****actionPath is not set!!!, please check the webflow engine servlet is initiated or parameter \"actionPath\" is set in web.xml!!!");
            throw new JspException("****actionPath is not set!!!, please check the webflow engine servlet is initiated or parameter \"actionPath\" is set in web.xml!!!");
        }

        StringBuffer results = new StringBuffer(actionPath);
        if ( logger.isDebugEnabled() )
        {
            logger.debug("Action path:" + actionPath);
        }

        boolean hasParams = ( actionPath.indexOf("?") != -1 );
        HttpSession session = request.getSession(false);
        String tempStr = (String)request.getAttribute(WebflowConstants.SOURCE_CHUNK_NAME);
        if ( tempStr != null )
        {
            if ( logger.isDebugEnabled() )
            {
                logger.debug("Append param sourcechunkname to action path");
            }
            appendParam(results, WebflowConstants.SOURCE_CHUNK_NAME, tempStr, hasParams);
            hasParams = true;
        }
        tempStr = (String)request.getAttribute(WebflowConstants.SOURCE_NODE_NAME);
        if ( tempStr != null )
        {
            if ( logger.isDebugEnabled() )
            {
                logger.debug("Append param sourcenodename to action path");
            }
            appendParam(results, WebflowConstants.SOURCE_NODE_NAME, tempStr, hasParams);
            hasParams = true;
        }

        if( session.getAttribute(WebflowConstants.USER_SESSION_KEY) != null )
        {
            if ( logger.isDebugEnabled() )
            {
                logger.debug("Append param needCheckSessionTimeOut to action path");
            }
            appendParam(results, "_needCheckSessionTimeOut", "true", hasParams);
            hasParams = true;
        }
        if ( logger.isDebugEnabled() )
        {
            logger.debug("Append param timestamp to action path");
        }
        appendParam(results, "_timestamp", WebConfig.getTimeStamp(), hasParams);
        return response.encodeURL(results.toString());
    }
    
    private StringBuffer appendParam(StringBuffer sb, String name, String value, boolean hasParams)
    {
        if ( name == null )
        {
            return sb;
        }

        if (hasParams)
            sb.append("&");
        else
            sb.append("?");

        sb.append(name);
        sb.append("=");
        sb.append(value);

        return sb;
    }
    
    private void debugEnableToSerializable(Map<String, JSONObject> ajaxComponentMap) 
    {
        logger.debug("Created UI ajax widgets.");
        for(Map.Entry<String, JSONObject> entry : ajaxComponentMap.entrySet())
        {
            logger.debug("Ajax widget: "+ entry.getValue().toString());
        }
    }
    

    private String genLoaderMask()
    {
    	StringBuilder sb = DisposableBfString.getBuffer();
    	try {
	    	sb.append("<div id=\"ui-mask-shadow\" class=\"ui-overlay\" style=\"display:block;\">\n");
	    	sb.append("<div class=\"ui-widget-overlay\"></div>\n");
	    	sb.append("<div id=\"uimaster_mask_content\" class=\"outer\">\n");
	    	sb.append("<div class=\"inner\"><p class=\"ui-info-msg\"><span></span>");
	    	sb.append("\u62FC\u6B7B\u73A9\u547D\u52A0\u8F7D\u4E2D\u3002\u3002\u3002</p></div>\n");
	    	sb.append("</div>\n");
	    	sb.append("</div>\n");
	        
	        return sb.toString();
    	} finally {
    		DisposableBfString.release(sb);
    	}
    }

    private void importAllJS(UserRequestContext context, int depth) throws JspException
    {
        Map refEntityMap = context.getRefEntityMap();
        UIFormObject tEntityObj = (UIFormObject)refEntityMap.remove(pageObject.getRuntimeEntityName());
        tEntityObj.importSelfJS(context, depth, false);
        Iterator entityIterator = refEntityMap.values().iterator();
        while ( entityIterator.hasNext() )
        {
            UIFormObject entityObj = (UIFormObject)entityIterator.next();
            entityObj.importSelfJS(context, depth, false);
        }
        refEntityMap.put(pageObject.getRuntimeEntityName(), tEntityObj);
    }
    
}
