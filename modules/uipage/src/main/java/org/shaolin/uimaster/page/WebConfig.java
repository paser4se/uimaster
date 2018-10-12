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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.Registry;
import org.shaolin.bmdp.runtime.Registry.RunningMode;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.bmdp.runtime.spi.IConstantService;
import org.shaolin.bmdp.runtime.spi.IServerServiceManager;
import org.shaolin.bmdp.utils.HttpSender;
import org.shaolin.uimaster.page.flow.WebflowConstants;

public final class WebConfig {

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
	
	public static final String WebContextRoot = UIMASTER;
	
	// make sure cleaning the js cache after restart server in every time.
	private static int jsversion = (int)(Math.random() * 1000); 
	
	private static WebConfigSpringInstance instance;
	
	public WebConfig() {
	}
	
	public static void setSpringInstance(final WebConfigSpringInstance instance0) {
		instance = instance0;
	}
	
	public static boolean isProductMode() {
		return Registry.getAppRunningMode() == RunningMode.Production;
	}
	
	public static boolean isCustomizedMode() {
		return instance.isCustomizedMode();
	}
	
	public static void updateJsVersion(int version) {
		WebConfig.jsversion = version;
	}
	
	public static int getJsVersion() {
		return WebConfig.jsversion;
	}
	
	public static String getHiddenValueMask() {
		return instance.getHiddenValueMask();
	}

	public static Hashtable<?, ?> getInitialContext() {
		Map<String, String> items = Registry.getInstance().getNodeItems(
				"/System/jndi");
		// TODO: doing values replacement.
		return new Hashtable(items);
	}

	public static String getUserLocale(HttpServletRequest request) {
		// read from session
		HttpSession session = request.getSession();
		String locale = (String) session.getAttribute(WebflowConstants.USER_LOCALE_KEY);
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
		return instance.getUploadServer();
	}
	
	public static String getWebRoot() {
		return UIMASTER;
	}
	
	public static String getWebContextRoot() {
		return getWebRoot();
	}
	
	public static String getWebSocketServer() {
		return instance.getWebsocketServer();
	}
	
	public static String getJsWebSocketServer() {
		return instance.getJsWebsocketServer();
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
		return instance.getResourceServer();
	}
	
	public static String getAppResourceContextRoot(HttpServletRequest request) {
		String path = request.getParameter(WebConfig.APP_RESOURCE_PATH);
		return path != null ? ("file://" + path + "/uimaster") : "file:///storage/sdcard/uimaster";
	}

	public static String getAppImageContextRoot(HttpServletRequest request) {
		if (UserContext.isAppClient()) {
			return instance.getResourceServer();
		}
		return getResourceContextRoot();
	}
	
	public static String getResourcePath() {
		return instance.getResourcePath();
	}
	
	public static String getTempResourcePath() {
		return instance.getResourcePath() + "/temp";
	}
	
	public static void setResourcePath(String path) {
		if (instance.getResourcePath() == null || instance.getResourcePath().trim().length() == 0) {
			instance.setResourcePath(path);
		}
	}
	
	public static String getCssRootPath() {
		return instance.getCssRootPath();
	}
	
	public static String getJsRootPath() {
		return instance.getJsRootPath();
	}
	
	public static String getAjaxServiceURI() {
		if (UserContext.isAppClient()) {
			return instance.getAjaxServiceURL() + "?_appclient=" + UserContext.getAppClientType();
		}
		return instance.getAjaxServiceURL();
	}

	public static String getFrameWrap() {
		return instance.getFrameWrap();
	}

	public static String getActionPath() {
		return instance.getActionPath();
	}

	public static String getLoginPath() {
		return instance.getLoginPath();
	}

	public static String getIndexPage() {
		return instance.getIndexPage();
	}

	public static String getLoginPage() {
		return instance.getLoginPage();
	}

	public static String getILoginPage() {
		return instance.getIloginPage();
	}

	public static String getMainPage() {
		return instance.getMainPage();
	}

	public static String getErrorPage() {
		return instance.getErrorPage();
	}

	public static String getNoPermissionPage() {
		return instance.getNopermissionPage();
	}
	
	public static String getTimeoutPage() {
		return instance.getTimeoutPage();
	}

	public static String getTimeStamp() {
		return String.valueOf(WebConfig.getJsVersion());
	}
	
	public static boolean hasAjaxErrorHandler() {
		return "true".equals(instance.getHasAjaxErrorHandler());
	}

	public static boolean isHttps() {
		return instance.isHTTPs();
	}
	
	public static boolean isJAAS() {
		return instance.isJAAS();
	}
	
	public static String isSyncLoadingJs(String url) {
		if (UserContext.isAppClient()) {
			return "";
		}
		String[] list = instance.getSyncLoadJs();
		for (String js : list) {
			if (url.indexOf(js) != -1) {
				return "";
			}
		}
		return "async";
	}
	
	public static boolean isFormatHTML() {
		return instance.isFormatHTML();
	}
	
	public static boolean enableHotDeploy() {
		return instance.isHotdeployeable();
	}
	
	public static boolean skipBackButton(String pageName) {
		return instance.getSkipBackButtonPages().contains(pageName);
	}
	
	/**
	 * Import one css.
	 * @param entityName
	 * @return
	 */
	public static String getImportCSS(String entityName) {
		String name = entityName.replace('.', '/');//firefox only support '/'
		return getResourceContextRoot() + "/css/" + name + ".css";
	}
	
	public static String getImportMobCSS(String entityName) {
		String name = entityName.replace('.', '/');//firefox only support '/'
		File f = new File(WebConfig.getRealPath("/css/" + name + "_mob.css"));
		if (f.exists()) {
			return getResourceContextRoot() + "/css/" + name + "_mob.css";
		}
		return getResourceContextRoot() + "/css/" + name + ".css";
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
		return getResourceContextRoot() + "/js/" + name + ".js";
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
		return instance.getCommoncss();
	}

	public static String[] getCommonJs() {
		return instance.getCommonjs();
	}
	
	public static String[] getCommonMobCss() {
		return instance.getCommonMobcss();
	}

	public static String[] getCommonMobJs() {
		return instance.getCommonMobjs();
	}
	
	public static String[] getCommonMobAppCss() {
		return instance.getCommonMobAppcss();
	}

	public static String[] getCommonMobAppJs() {
		return instance.getCommonMobAppjs();
	}
	
	public static boolean skipCommonJs(String pageName) {
		return instance.getSkipCommonJsPages().contains(pageName);
	}
	
	public static boolean skipCommonCss(String pageName) {
		return instance.getSkipCommonCssPages().contains(pageName);
	}
	
	public static String[] getSingleCommonJS(final String entityName) {
		String pack = entityName.substring(0, entityName.lastIndexOf('.')).replace('.', '_');
		String entityName0 = entityName.replace('.', '_');
		Map<String, String[]> singleCommonJs = instance.getSingleCommonJs();
		Set<String> keys = singleCommonJs.keySet();
		List<String> results = new ArrayList<String>();
		for (String key : keys) {
			if (key.startsWith("*")) {
				String keyA = key.substring(1, 2);
				if (keyA.endsWith(".*")) {
					keyA = keyA.substring(0, keyA.length() - 2);
					if (pack.indexOf(keyA) != -1) {
						String[] vs = singleCommonJs.get(key);
						for (String v: vs) {
							results.add(v);
						}
					}
				} else {
					if (pack.lastIndexOf(keyA) != -1) {
						String[] vs = singleCommonJs.get(key);
						for (String v: vs) {
							results.add(v);
						}
					}
				}
			} else if (key.endsWith(".*")) {
				String keyPack = key.substring(0, key.length() - 2);
				if (pack.startsWith(keyPack)) {
					String[] vs = singleCommonJs.get(key);
					for (String v: vs) {
						results.add(v);
					}
				}
			} else if (key.equals(entityName0)) {
				String[] vs = singleCommonJs.get(entityName0);
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
	
	public static String[] getSingleCommonAppJS(final String entityName) {
		String pack = entityName.substring(0, entityName.lastIndexOf('.')).replace('.', '_');
		String entityName0 = entityName.replace('.', '_');
		Map<String, String[]> singleCommonAppJs = instance.getSingleCommonAppJs();
		Set<String> keys = singleCommonAppJs.keySet();
		List<String> results = new ArrayList<String>();
		for (String key : keys) {
			if (key.startsWith("*")) {
				String keyA = key.substring(1, 2);
				if (keyA.endsWith(".*")) {
					keyA = keyA.substring(0, keyA.length() - 2);
					if (pack.indexOf(keyA) != -1) {
						String[] vs = singleCommonAppJs.get(key);
						for (String v: vs) {
							results.add(v);
						}
					}
				} else {
					if (pack.lastIndexOf(keyA) != -1) {
						String[] vs = singleCommonAppJs.get(key);
						for (String v: vs) {
							results.add(v);
						}
					}
				}
			} else if (key.endsWith(".*")) {
				String keyPack = key.substring(0, key.length() - 2);
				if (pack.startsWith(keyPack)) {
					String[] vs = singleCommonAppJs.get(key);
					for (String v: vs) {
						results.add(v);
					}
				}
			} else if (key.equals(entityName0)) {
				String[] vs = singleCommonAppJs.get(entityName0);
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
		String pack = entityName.substring(0, entityName.lastIndexOf('.')).replace('.', '_');
		String entityName0 = entityName.replace('.', '_');
		Map<String, String[]> singleCommonCss = instance.getSingleCommonCss();
		Set<String> keys = singleCommonCss.keySet();
		List<String> results = new ArrayList<String>();
		for (String key : keys) {
			if (key.endsWith(".*")) {
				String keyPack = key.substring(0, key.length() - 2);
				if (pack.startsWith(keyPack)) {
					String[] vs = singleCommonCss.get(key);
					for (String v: vs) {
						results.add(v);
					}
				}
			} else if (key.equals(entityName0)) {
				String[] vs = singleCommonCss.get(entityName0);
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
		String pack = entityName.substring(0, entityName.lastIndexOf('.')).replace('.', '_');
		String entityName0 = entityName.replace('.', '_');
		Map<String, String[]> singleCommonAppCss = instance.getSingleCommonAppCss();
		Set<String> keys = singleCommonAppCss.keySet();
		List<String> results = new ArrayList<String>();
		for (String key : keys) {
			if (key.endsWith(".*")) {
				String keyPack = key.substring(0, key.length() - 2);
				if (pack.startsWith(keyPack)) {
					String[] vs = singleCommonAppCss.get(key);
					for (String v: vs) {
						results.add(v);
					}
				}
			} else if (key.equals(entityName0)) {
				String[] vs = singleCommonAppCss.get(entityName0);
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
		return "";
	}

	public static String getSystemUISkin(String componentTypeName) {
		return null;
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
		sb.append(instance.getIpLocationURL()).append("?key=");
		sb.append(instance.getMapwebkey());
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
	
	public static ThreadPoolExecutor getServletThreadPool(RejectedExecutionHandler handler) {
		Registry instance = Registry.getInstance();
		int maxMsgsQueue = instance.getValue("/System/webConstant/AsyncServlet/maxMsgsQueue", 1000);
		int minThreadNum = instance.getValue("/System/webConstant/AsyncServlet/minThreadNum", Runtime.getRuntime().availableProcessors());
		int maxThreadNum = instance.getValue("/System/webConstant/AsyncServlet/maxThreadNum", Runtime.getRuntime().availableProcessors());
		return createThreadPool("ServletRequest", maxMsgsQueue, minThreadNum, maxThreadNum, handler);
	}
	
	public static ThreadPoolExecutor getAjaxThreadPool(RejectedExecutionHandler handler) {
		Registry instance = Registry.getInstance();
		int maxMsgsQueue = instance.getValue("/System/webConstant/AsyncAjax/maxMsgsQueue", 5000);
		int minThreadNum = instance.getValue("/System/webConstant/AsyncAjax/minThreadNum", Runtime.getRuntime().availableProcessors());
		int maxThreadNum = instance.getValue("/System/webConstant/AsyncAjax/maxThreadNum", Runtime.getRuntime().availableProcessors());
		return createThreadPool("AjaxRequest", maxMsgsQueue, minThreadNum, maxThreadNum, handler);
	}
	
	public static ThreadPoolExecutor createThreadPool(String threadPoolName, 
            int maxMsgsQueue, int minThreadNum, int maxThreadNum, RejectedExecutionHandler handler) {
        ThreadPoolExecutor workerPool_;
        if (maxMsgsQueue <= 0) {
            workerPool_ = new ThreadPoolExecutor(minThreadNum, maxThreadNum, 0L, 
                    TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
                    new DefaultThreadFactory(threadPoolName + "-worker"));
        } else {
            BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(maxMsgsQueue);
            workerPool_ = new ThreadPoolExecutor(minThreadNum, maxThreadNum, 0L, 
                    TimeUnit.MILLISECONDS, queue,
                    new DefaultThreadFactory(threadPoolName + "-worker"),
                    handler);
        }
        return workerPool_;
    }
	
	public static final class DefaultThreadFactory implements ThreadFactory {
        final ThreadGroup group;
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namePrefix;

        DefaultThreadFactory(String prefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = prefix + "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
