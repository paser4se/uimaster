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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.Registry;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.bmdp.runtime.spi.IConstantService;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.bmdp.utils.HttpSender;
import org.shaolin.uimaster.page.flow.WebflowConstants;

public class WebConfig {

	private static final String UIMASTER = "/uimaster";
	public static final String DEFAULT_LOGIN_PATH = "/login.do";
	public static final String DEFAULT_ACTION_PATH = "/webflow.do";
	public static final String DEFAULT_INDEX_PAGE = "/jsp/index.jsp";
	public static final String DEFAULT_LOGIN_PAGE = "/jsp/login.jsp";
	public static final String DEFAULT_ILOGIN_PAGE = "/jsp/ilogin.jsp";
	public static final String DEFAULT_MAIN_PAGE = "/jsp/main.jsp";
	public static final String DEFAULT_ERROR_PAGE = "/jsp/common/Failure.jsp";
	public static final String DEFAULT_TIMEOUT_PAGE = "/jsp/common/sessionTimeout.jsp";
	public static final String APP_ROOT_VAR = "$APPROOT";
	public static final String APP_RESOURCE_PATH = "_appstore";
	
	public static final String KEY_REQUEST_UIENTITY = "_UIEntity";
	
	private static String servletContextPath = "";
	
	private static final String commonCompressedJS = "/common.js";
	private static final String commonCompressedCss = "/common.css";
	private static final String commonCompressedMobJS = "/common-mob.js";
	private static final String commonCompressedMobCss = "/common-mob.css";
	
	public static final String WebContextRoot = UIMASTER;
	private static String resourceContextRoot;
	private static String uploadFileContextRoot;
	private static String resourcePath;
	
	// make sure cleaning the js cache after restart server in every time.
	private static int jsversion = (int)(Math.random() * 1000); 
	
	public static class WebConfigFastCache {
		final String runningMode;
		final boolean isHTTPs;
		final boolean customizedMode;
		final String hiddenValueMask;
		final String cssRootPath;
		final String jsRootPath;
		final String ajaxServiceURL;
		final String frameWrap;
		final boolean isJAAS;
		final boolean isFormatHTML;
		final boolean hotdeployeable;
		final String loginPath;
		final String actionPath;
		final String indexPage;
		final String loginPage;
		final String iloginPage;
		final String mainPage;
		final String errorPage;
		final String nopermissionPage;
		final String timeoutPage;
		final String hasAjaxErrorHandler;
		final String ipLocationURL;
		final String mapwebkey;
		final String mapappkey;
		
		final String[] commoncss;
		final String[] commonjs;
		final String[] commonMobcss;
		final String[] commonMobAppcss;
		final String[] commonMobjs;
		final String[] commonMobAppjs;
		final String[] syncLoadJs;
		final Map<String, String[]> singleCommonCss;
		final Map<String, String[]> singleCommonJs;
		final Map<String, String[]> singleCommonAppCss;
		final Map<String, String[]> singleCommonAppJs;
		
		final List<String> singleCustJs = new ArrayList<String>();
		final List<String> singleCustCss = new ArrayList<String>();
		final List<String> skipBackButtonPages;
		
		public WebConfigFastCache() {
			Registry instance = Registry.getInstance();
			runningMode = instance.getValue("/System/webConstant/runningMode");
			resourceContextRoot = instance.getValue(
					"/System/webConstant/resourceServer");
			uploadFileContextRoot = instance.getValue(
					"/System/webConstant/uploadServer");
			String resourcePath0 = instance.getValue(
					"/System/webConstant/resourcePath");
			if (resourcePath0 != null && resourcePath0.trim().length() > 0) {
				resourcePath = resourcePath0;
			}
			isHTTPs = Boolean.valueOf(instance.getValue(
					"/System/webConstant/isHTTPs"));
			customizedMode = Boolean.valueOf(instance.getValue(
					"/System/webConstant/customizedMode"));
			hiddenValueMask = instance.getValue(
					"/System/webConstant/hiddenValueMask");
			cssRootPath = resourceContextRoot + "/css";
			jsRootPath = resourceContextRoot + "/js";
			ajaxServiceURL = WebContextRoot + instance.getValue(
					"/System/webConstant/ajaxServiceURL");
			frameWrap = WebContextRoot + instance.getValue(
					"/System/webConstant/frameWrap");
			isJAAS = "true".equals(instance.getValue(
					"/System/webConstant/isJAAS"));
			isFormatHTML = "true".equals(instance.getValue(
					"/System/webConstant/formatHTML"));
			hotdeployeable = "true".equals(instance.getValue(
					"/System/webConstant/hotdeployeable"));
			loginPath = WebContextRoot + instance.getValue(
					"/System/webConstant/loginPath");
			actionPath = WebContextRoot + instance.getValue(
					"/System/webConstant/actionPath");
			indexPage = WebContextRoot + instance.getValue(
					"/System/webConstant/indexPage");
			loginPage = WebContextRoot + instance.getValue(
					"/System/webConstant/loginPage");
			iloginPage = WebContextRoot + instance.getValue(
					"/System/webConstant/iloginPage");
			mainPage = WebContextRoot + instance.getValue(
					"/System/webConstant/mainPage");
			errorPage = instance.getValue(
					"/System/webConstant/errorPage");
			nopermissionPage = instance.getValue(
					"/System/webConstant/nopermissionPage");
			timeoutPage = instance.getValue(
					"/System/webConstant/timeoutPage");
			hasAjaxErrorHandler = instance.getValue(
					"/System/webConstant/ajaxHandlingError");
			syncLoadJs = instance.getValue(
					"/System/webConstant/syncLoadJs").split(";");
			ipLocationURL = instance.getValue(
					"/System/webConstant/ipLocationURL");
			mapwebkey = instance.getValue(
					"/System/webConstant/mapwebkey");
			mapappkey = instance.getValue(
					"/System/webConstant/mapappkey");
			
			Collection<String> values = (Collection<String>)
					instance.getNodeItems("/System/webConstant/commoncss").values();
			commoncss = values.toArray(new String[values.size()]);
			for (int i=0; i<commoncss.length; i++) {
				if (!commoncss[i].startsWith("http")
						&& !commoncss[i].startsWith("https")) {
					commoncss[i] = resourceContextRoot + commoncss[i];
				}
			}
			values = instance.getNodeItems("/System/webConstant/commonjs").values();
			commonjs = values.toArray(new String[values.size()]);
			for (int i=0; i<commonjs.length; i++) {
				if (!commonjs[i].startsWith("http")
						&& !commonjs[i].startsWith("https")) {
					commonjs[i] = resourceContextRoot + commonjs[i];
				}
			}
			
			values = (Collection<String>)
					instance.getNodeItems("/System/webConstant/commoncss-mob").values();
			commonMobcss = values.toArray(new String[values.size()]);
			commonMobAppcss = values.toArray(new String[values.size()]);
			for (int i=0; i<commonMobcss.length; i++) {
				if (!commonMobcss[i].startsWith("http")) {
					commonMobAppcss[i] = APP_ROOT_VAR + commonMobcss[i];
					commonMobcss[i] = resourceContextRoot + commonMobcss[i];
				}
			}
			values = instance.getNodeItems("/System/webConstant/commonjs-mob").values();
			commonMobjs = values.toArray(new String[values.size()]);
			commonMobAppjs = values.toArray(new String[values.size()]);
			for (int i=0; i<commonMobjs.length; i++) {
				if (!commonMobjs[i].startsWith("http")
						&& !commonMobjs[i].startsWith("https")) {
					commonMobAppjs[i] = APP_ROOT_VAR + commonMobjs[i];
					commonMobjs[i] = resourceContextRoot + commonMobjs[i];
				}
			}
			
			singleCommonCss = new HashMap<String, String[]>();
			singleCommonAppCss = new HashMap<String, String[]>();
			String commonssPath = "/System/webConstant/commoncss";
			List<String> children = instance.getNodeChildren(commonssPath);
			if (children != null && children.size() > 0) {
				for (String child: children) {
					values = (Collection<String>)instance.getNodeItems(commonssPath + "/" + child).values();
					String[] items = values.toArray(new String[values.size()]);
					String[] itemsApp = values.toArray(new String[values.size()]);
					for (int i=0; i<items.length; i++) {
						if (items[i].equals("skipCommonCss")) {
							// the common js files will be skipped if specified.
							singleCustCss.add(child);
							items[i] = "/uimaster/js/emtpy.js";
							itemsApp[i] = "/uimaster/js/emtpy.js";
						} else if (!items[i].startsWith("http")
								&& !items[i].startsWith("https")) {
							items[i] = resourceContextRoot + items[i];
							itemsApp[i] = APP_ROOT_VAR + itemsApp[i];
						}
					}
					singleCommonCss.put(child, items);
					singleCommonAppCss.put(child, itemsApp);
				}
			}
			
			singleCommonJs = new HashMap<String, String[]>();
			singleCommonAppJs = new HashMap<String, String[]>();
			String commonjsPath = "/System/webConstant/commonjs";
			children = instance.getNodeChildren(commonjsPath);
			if (children != null && children.size() > 0) {
				for (String child: children) {
					values = (Collection<String>)instance.getNodeItems(commonjsPath + "/" + child).values();
					String[] items = values.toArray(new String[values.size()]);
					String[] itemsApp = values.toArray(new String[values.size()]);
					for (int i=0; i<items.length; i++) {
						if (items[i].equals("skipCommonJs")) {
							// the common js files will be skipped if specified.
							singleCustJs.add(child);
							items[i] = "/uimaster/js/emtpy.js";
							itemsApp[i] = "/uimaster/js/emtpy.js";
						} else if (!items[i].startsWith("http")
								&& !items[i].startsWith("https")) {
							//skip http, https, www, 
							items[i] = resourceContextRoot + items[i];
							itemsApp[i] = APP_ROOT_VAR + itemsApp[i];
						}
					}
					singleCommonJs.put(child, items);
					singleCommonAppJs.put(child, itemsApp);
				}
			}
			
			skipBackButtonPages = new ArrayList<String>(instance.getNodeItems("/System/webConstant/skipBackButton").values());
		}
	}
	
	private static WebConfigFastCache getCacheObject() {
		Registry instance = Registry.getInstance();
		if(!instance.existInFastCache("webconfig")) {
			WebConfigFastCache fastCache = new WebConfigFastCache();
			instance.putInFastCache("webconfig", fastCache);
		}
		return (WebConfigFastCache)instance.readFromFastCache("webconfig");
	}
	
//	public static boolean isProductMode() {
//		return getCacheObject().runningMode != null && "product".equalsIgnoreCase(getCacheObject().runningMode);
//	}
	
	public static boolean isCustomizedMode() {
		return getCacheObject().customizedMode;
	}
	
	public static void updateJsVersion(int version) {
		WebConfig.jsversion = version;
	}
	
	public static int getJsVersion() {
		return WebConfig.jsversion;
	}
	
	public static String getHiddenValueMask() {
		return getCacheObject().hiddenValueMask;
	}

	public static Hashtable<?, ?> getInitialContext() {
		Map<String, String> items = Registry.getInstance().getNodeItems(
				"/System/jndi");
		// TODO: doing values replacement.
		return new Hashtable(items);
	}

	public static String getUserLocale(HttpServletRequest request) {
		// read from session
		HttpSession session = request.getSession(true);
		String locale = (String) session
				.getAttribute(WebflowConstants.USER_LOCALE_KEY);
		if (locale != null) {
			return locale;
		}

		// read from cookie
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0, n = cookies.length; i < n; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals(WebflowConstants.USER_LOCALE_KEY)) {
					locale = cookie.getValue();
					if (locale != null) {
						session.setAttribute(WebflowConstants.USER_LOCALE_KEY,
								locale);
						return locale;
					}
				}
			}
		}

		return null;
	}

	public static void setServletContextPath(String servletContextPath) {
		WebConfig.servletContextPath = servletContextPath;
	}
	
	public static String getRealPath(String relativePath) {
		return WebConfig.getResourcePath() + relativePath;
	}
	
	public static String getUploadFileRoot() {
		return uploadFileContextRoot;
	}
	
	public static String getWebRoot() {
		return UIMASTER;
	}
	
	public static String getWebContextRoot() {
		return getWebRoot();
	}
	
	/**
	 * Andriod or IOS app context root.
	 * 
	 * @return
	 */
	public static String getAppContextRoot(HttpServletRequest request) {
		String path = request.getParameter(WebConfig.APP_RESOURCE_PATH);
		return path != null ? ("file://" + path + "/uimaster") : "file:///storage/sdcard/uimaster";
	}
	
	public static String getResourceContextRoot() {
		return resourceContextRoot;
	}
	
	public static String getAppResourceContextRoot(HttpServletRequest request) {
		String path = request.getParameter(WebConfig.APP_RESOURCE_PATH);
		return path != null ? ("file://" + path + "/uimaster") : "file:///storage/sdcard/uimaster";
	}

	public static String getAppImageContextRoot(HttpServletRequest request) {
		if (UserContext.isAppClient()) {
			return resourceContextRoot;
		}
		return getResourceContextRoot();
	}
	
	public static String getResourcePath() {
		return resourcePath;
	}
	
	public static String getTempResourcePath() {
		return resourcePath + "/temp";
	}
	
	public static void setResourcePath(String path) {
		if (resourcePath == null || resourcePath.trim().length() == 0) {
			resourcePath = path;
		}
	}
	
	public static String getCssRootPath() {
		return getCacheObject().cssRootPath;
	}
	
	public static String getJsRootPath() {
		return getCacheObject().jsRootPath;
	}
	
	public static String getAjaxServiceURI() {
		if (UserContext.isAppClient()) {
			return getCacheObject().ajaxServiceURL + "?_appclient=" + UserContext.getAppClientType();
		}
		return getCacheObject().ajaxServiceURL;
	}

	public static String getFrameWrap() {
		return getCacheObject().frameWrap;
	}

	public static String getActionPath() {
		return getCacheObject().actionPath;
	}

	public static String getLoginPath() {
		return getCacheObject().loginPath;
	}

	public static String getIndexPage() {
		return getCacheObject().indexPage;
	}

	public static String getLoginPage() {
		return getCacheObject().loginPage;
	}

	public static String getILoginPage() {
		return getCacheObject().iloginPage;
	}

	public static String getMainPage() {
		return getCacheObject().mainPage;
	}

	public static String getErrorPage() {
		return getCacheObject().errorPage;
	}

	public static String getNoPermissionPage() {
		return getCacheObject().nopermissionPage;
	}
	
	public static String getTimeoutPage() {
		return getCacheObject().timeoutPage;
	}

	public static String getTimeStamp() {
		return String.valueOf(WebConfig.getJsVersion());
	}
	
	public static boolean hasAjaxErrorHandler() {
		return "true".equals(getCacheObject().hasAjaxErrorHandler);
	}

	public static boolean isHttps() {
		return getCacheObject().isHTTPs;
	}
	
	public static boolean isJAAS() {
		return getCacheObject().isJAAS;
	}
	
	public static String isSyncLoadingJs(String url) {
		if (UserContext.isAppClient()) {
			return "";
		}
		String[] list = getCacheObject().syncLoadJs;
		for (String js : list) {
			if (url.indexOf(js) != -1) {
				return "";
			}
		}
		return "async";
	}
	
	public static boolean isFormatHTML() {
		return getCacheObject().isFormatHTML;
	}
	
	public static boolean enableHotDeploy() {
		return getCacheObject().hotdeployeable;
	}
	
	public static boolean skipBackButton(String pageName) {
		return getCacheObject().skipBackButtonPages.contains(pageName);
	}
	
	/**
	 * Import one css.
	 * @param entityName
	 * @return
	 */
	public static String getImportCSS(String entityName) {
		String name = entityName.replace('.', '/');//firefox only support '/'
		return resourceContextRoot + "/css/" + name + ".css";
	}
	
	public static String getImportMobCSS(String entityName) {
		String name = entityName.replace('.', '/');//firefox only support '/'
		File f = new File(WebConfig.getRealPath("/css/" + name + "_mob.css"));
		if (f.exists()) {
			return resourceContextRoot + "/css/" + name + "_mob.css";
		}
		return resourceContextRoot + "/css/" + name + ".css";
	}
	
	public static String getImportAppMobCSS(String entityName) {
		String name = entityName.replace('.', '/');//firefox only support '/'
		File f = new File(WebConfig.getRealPath("/css/" + name + "_mob.css"));
		if (f.exists()) {
			return "$APPROOT/css/" + name + "_mob.css";
		}
		return "$APPROOT/css/" + name + ".css";
	}
	
	/**
	 * Import one js.
	 * @param entityName
	 * @return
	 */
	public static String getImportJS(String entityName) {
		String name = entityName.replace('.', '/');//firefox only support '/'
		return resourceContextRoot + "/js/" + name + ".js";
	}
	
	public static String getImportAppJS(String entityName) {
		String name = entityName.replace('.', '/');//firefox only support '/'
		return "$APPROOT/js/" + name + ".js";
	}

	public static String replaceAppCssWebContext(HttpServletRequest request, final String str) {
		if (str.indexOf(APP_ROOT_VAR) != -1) {
			return str.replace(APP_ROOT_VAR, WebConfig.getAppContextRoot(request));
		}
		return str;
	}
	
	public static String replaceAppJsWebContext(HttpServletRequest request, final String str) {
		if (str.indexOf(APP_ROOT_VAR) != -1) {
			return str.replace(APP_ROOT_VAR, WebConfig.getAppContextRoot(request));
		}
		return str;
	}
	
	public static String replaceCssWebContext(String str) {
		return str;
	}
	
	public static String replaceJsWebContext(String str) {
		return str;
	}
	
	public static String replaceWebContext(String str) {
		return str;
	}
	
	public static String[] getCommonCss() {
		return getCacheObject().commoncss;
	}

	public static String[] getCommonJs() {
		return getCacheObject().commonjs;
	}
	
	public static String[] getCommonMobCss() {
		return getCacheObject().commonMobcss;
	}

	public static String[] getCommonMobJs() {
		return getCacheObject().commonMobjs;
	}
	
	public static String[] getCommonMobAppCss() {
		return getCacheObject().commonMobAppcss;
	}

	public static String[] getCommonMobAppJs() {
		return getCacheObject().commonMobAppjs;
	}
	
	public static boolean skipCommonJs(String pageName) {
		return getCacheObject().singleCustJs.contains(pageName);
	}
	
	public static boolean skipCommonCss(String pageName) {
		return getCacheObject().singleCustCss.contains(pageName);
	}
	
	public static String getCommoncompressedjs() {
		return commonCompressedJS;
	}

	public static String getCommoncompressedcss() {
		return commonCompressedCss;
	}

	public static String getCommoncompressedmobjs() {
		return commonCompressedMobJS;
	}

	public static String getCommoncompressedmobcss() {
		return commonCompressedMobCss;
	}
	
	public static String[] getSingleCommonJS(String entityName) {
		String pack = entityName.substring(0, entityName.lastIndexOf('.'));
		Set<String> keys = getCacheObject().singleCommonJs.keySet();
		List<String> results = new ArrayList<String>();
		for (String key : keys) {
			if (key.startsWith("*")) {
				String keyA = key.substring(1, 2);
				if (keyA.endsWith(".*")) {
					keyA = keyA.substring(0, keyA.length() - 2);
					if (pack.indexOf(keyA) != -1) {
						String[] vs = getCacheObject().singleCommonJs.get(key);
						for (String v: vs) {
							results.add(v);
						}
					}
				} else {
					if (pack.lastIndexOf(keyA) != -1) {
						String[] vs = getCacheObject().singleCommonJs.get(key);
						for (String v: vs) {
							results.add(v);
						}
					}
				}
			} else if (key.endsWith(".*")) {
				String keyPack = key.substring(0, key.length() - 2);
				if (pack.startsWith(keyPack)) {
					String[] vs = getCacheObject().singleCommonJs.get(key);
					for (String v: vs) {
						results.add(v);
					}
				}
			} else if (key.equals(entityName)) {
				String[] vs = getCacheObject().singleCommonJs.get(entityName);
				for (String v: vs) {
					results.add(v);
				}
			}
		}
		// keep the load sequence.
		for (String key : keys) {
			if (key.equals(pack)) {
				results.add(key);
			} 
		} 
		return results.toArray(new String[results.size()]);
	}
	
	public static String[] getSingleCommonAppJS(String entityName) {
		String pack = entityName.substring(0, entityName.lastIndexOf('.'));
		Set<String> keys = getCacheObject().singleCommonAppJs.keySet();
		List<String> results = new ArrayList<String>();
		for (String key : keys) {
			if (key.startsWith("*")) {
				String keyA = key.substring(1, 2);
				if (keyA.endsWith(".*")) {
					keyA = keyA.substring(0, keyA.length() - 2);
					if (pack.indexOf(keyA) != -1) {
						String[] vs = getCacheObject().singleCommonAppJs.get(key);
						for (String v: vs) {
							results.add(v);
						}
					}
				} else {
					if (pack.lastIndexOf(keyA) != -1) {
						String[] vs = getCacheObject().singleCommonAppJs.get(key);
						for (String v: vs) {
							results.add(v);
						}
					}
				}
			} else if (key.endsWith(".*")) {
				String keyPack = key.substring(0, key.length() - 2);
				if (pack.startsWith(keyPack)) {
					String[] vs = getCacheObject().singleCommonAppJs.get(key);
					for (String v: vs) {
						results.add(v);
					}
				}
			} else if (key.equals(entityName)) {
				String[] vs = getCacheObject().singleCommonAppJs.get(entityName);
				for (String v: vs) {
					results.add(v);
				}
			}
		}
		// keep the load sequence.
		for (String key : keys) {
			if (key.equals(pack)) {
				results.add(key);
			} 
		} 
		return results.toArray(new String[results.size()]);
	}
	
	public static String[] getSingleCommonCSS(String entityName) {
		String pack = entityName.substring(0, entityName.lastIndexOf('.'));
		Set<String> keys = getCacheObject().singleCommonCss.keySet();
		List<String> results = new ArrayList<String>();
		for (String key : keys) {
			if (key.endsWith(".*")) {
				String keyPack = key.substring(0, key.length() - 2);
				if (pack.startsWith(keyPack)) {
					String[] vs = getCacheObject().singleCommonCss.get(key);
					for (String v: vs) {
						results.add(v);
					}
				}
			} else if (key.equals(entityName)) {
				String[] vs = getCacheObject().singleCommonCss.get(entityName);
				for (String v: vs) {
					results.add(v);
				}
			}
		}
		// keep the load sequence.
		for (String key : keys) {
			if (key.equals(pack)) {
				results.add(key);
			} 
		} 
		return results.toArray(new String[results.size()]);
	}
	
	public static String[] getSingleCommonAppCSS(String entityName) {
		String pack = entityName.substring(0, entityName.lastIndexOf('.'));
		Set<String> keys = getCacheObject().singleCommonAppCss.keySet();
		List<String> results = new ArrayList<String>();
		for (String key : keys) {
			if (key.endsWith(".*")) {
				String keyPack = key.substring(0, key.length() - 2);
				if (pack.startsWith(keyPack)) {
					String[] vs = getCacheObject().singleCommonAppCss.get(key);
					for (String v: vs) {
						results.add(v);
					}
				}
			} else if (key.equals(entityName)) {
				String[] vs = getCacheObject().singleCommonAppCss.get(entityName);
				for (String v: vs) {
					results.add(v);
				}
			}
		}
		// keep the load sequence.
		for (String key : keys) {
			if (key.equals(pack)) {
				results.add(key);
			} 
		} 
		return results.toArray(new String[results.size()]);
	}

	private static Map skinSettingMap = new ConcurrentHashMap();

	/**
	 * get css root path
	 * 
	 * @param entityName
	 *            the ui entity name
	 * 
	 * @return css root path
	 * 
	 */
	public static String getJspUrl(String entityName) {
		return getJspUrl(entityName, "");
	}

	/**
	 * suffix: UIENTITY_JSP_SUFFIX,ODMAPPER_JSP_SUFFIX,UIPAGE_HTML_JSP_SUFFIX
	 */
	public static String getJspUrl(String entityName, String componentType) {
		// TODO:
		return "";
	}

	public static String getSystemUISkin(String componentTypeName) {
		return (String) skinSettingMap.get(componentTypeName);
	}

	public static String getDefaultJspName(String entityName) {
		int index = entityName.indexOf(".uientity.");
		if (index != -1) {
			return entityName.substring(0, index) + ".uientityhtml."
					+ entityName.substring(index + 10);
		}

		index = entityName.indexOf(".uipage.");
		if (index != -1) {
			return entityName.substring(0, index) + ".uipagehtml."
					+ entityName.substring(index + 8);
		}

		index = entityName.indexOf(".od.");
		if (index != -1) {
			return entityName.substring(0, index) + ".odhtml."
					+ entityName.substring(index + 4);
		}

		return entityName + "HTML";
	}

	/**
	 * Determine whether entity name and pattern is matched Currently use java
	 * package like styles, support ** for all package Maybe we will use regular
	 * expression pattern sometime later
	 */
	private static boolean matchPattern(String entityName, String pattern) {
		boolean matched = false;
		if (entityName.equals(pattern)) {
			matched = true;
		} else if (pattern.endsWith(".**")) {
			String packageName = pattern.substring(0, pattern.length() - 3);
			if (entityName.startsWith(packageName)) {
				matched = true;
			}
		}
		return matched;
	}

	private static String convertJSPath(String input) {
		int curIndex = input.indexOf('.');
		if (curIndex == -1) {
			return input + ".js";
		}
		int nextIndex = input.indexOf('.', curIndex + 1);
		if (nextIndex == -1) {
			return input;
		}
		StringBuffer sb = new StringBuffer(input.substring(0, curIndex));
		do {
			sb.append('/');
			sb.append(input.substring(curIndex + 1, nextIndex));

			curIndex = nextIndex;
			nextIndex = input.indexOf('.', curIndex + 1);
		} while (nextIndex != -1);
		sb.append(input.substring(curIndex));
		return new String(sb);
	}

	private static HttpSender sender = new HttpSender();
	
	public static final String IP_Service = "http://ip.taobao.com/service/getIpInfo.php?ip=";
	
	/**
	 * Only for web user! App user will use SDK api.
	 * 
	 * @param request
	 * @return
	 */
	public static double[] getUserLocation(String ipAddress) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append(getCacheObject().ipLocationURL).append("?key=");
		sb.append(getCacheObject().mapwebkey);
		sb.append("&output=JSON").append("&ip=").append(ipAddress);
		
		JSONObject json1 = new JSONObject(sender.get(sb.toString()));
		if (json1.has("rectangle")) {
			String value = json1.getString("rectangle");
			String[] items = value.split(";");
			if (items.length ==2) {
				String[] righttop = items[0].split(",");
				String[] leftbottom = items[1].split(",");
				double longti = (Double.parseDouble(leftbottom[0]) - Double.parseDouble(righttop[0]))/2 + Double.parseDouble(righttop[0]);
				double lati = (Double.parseDouble(leftbottom[1]) - Double.parseDouble(righttop[1]))/2 + Double.parseDouble(righttop[1]);
				return new double[]{longti, lati};
			}
		}
		return null;
	}
	
	
	/**
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getUserCityInfo(String ipAddress) throws Exception {
		String jsonStr = sender.get(IP_Service + ipAddress);
		JSONObject json = new JSONObject(jsonStr);
		if (json.has("code") && json.getInt("code") == 0) {
			String regionId = json.getJSONObject("data").getString("region_id");
			String cityId = json.getJSONObject("data").getString("city_id");
			if (regionId != null && regionId.trim().length() > 0) {
				IConstantService cs = IServerServiceManager.INSTANCE.getConstantService();
				String entityName = cs.getChildren("CityList", Integer.parseInt(regionId)).getEntityName();
				return entityName +","+cityId;
			}
		}
		return null;
	}
}
