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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("webConstant")
/**
 * use Springboot typed bean annotation.
 * 
 * @author wushaol
 *
 */
public class WebConfigSpringInstance {
	
	private String resourceServer;
	private String uploadServer;
	private String resourcePath;
	private String contextRoot;
	private String runningMode;
	private boolean isHTTPs;
	private boolean customizedMode;
	private String hiddenValueMask;
	private String cssRootPath;
	private String jsRootPath;
	private String ajaxServiceURL;
	private String frameWrap;
	private boolean isJAAS;
	private boolean isFormatHTML;
	private boolean hotdeployeable;
	private String loginPath;
	private String actionPath;
	private String indexPage;
	private String loginPage;
	private String iloginPage;
	private String mainPage;
	private String errorPage;
	private String nopermissionPage;
	private String timeoutPage;
	private String hasAjaxErrorHandler;
	private String ipLocationURL;
	private String mapwebkey;
	private String mapappkey;
	
	private String[] commoncss;
	private String[] commonjs;
	private String[] commonMobcss;
	private String[] commonMobAppcss;
	private String[] commonMobjs;
	private String[] commonMobAppjs;
	private String[] syncLoadJs;
	
	private List<String> skipCommonJsPages = new ArrayList<String>();
	private List<String> skipCommonCssPages = new ArrayList<String>();
	private List<String> skipBackButtonPages = new ArrayList<String>();
	
	private Map<String, String[]> singleCommonCss = new HashMap<String, String[]>();
	private Map<String, String[]> singleCommonJs = new HashMap<String, String[]>();
	private Map<String, String[]> singleCommonAppCss = new HashMap<String, String[]>();
	private Map<String, String[]> singleCommonAppJs = new HashMap<String, String[]>();
	
	public WebConfigSpringInstance() {
	}

	public String getResourceServer() {
		return resourceServer;
	}

	public void setResourceServer(String resourceServer) {
		this.resourceServer = resourceServer;
	}

	public String getUploadServer() {
		return uploadServer;
	}

	public void setUploadServer(String uploadServer) {
		this.uploadServer = uploadServer;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getRunningMode() {
		return runningMode;
	}

	public void setRunningMode(String runningMode) {
		this.runningMode = runningMode;
	}

	public boolean isHTTPs() {
		return isHTTPs;
	}

	public void setHTTPs(boolean isHTTPs) {
		this.isHTTPs = isHTTPs;
	}

	public boolean isCustomizedMode() {
		return customizedMode;
	}

	public void setCustomizedMode(boolean customizedMode) {
		this.customizedMode = customizedMode;
	}

	public String getHiddenValueMask() {
		return hiddenValueMask;
	}

	public void setHiddenValueMask(String hiddenValueMask) {
		this.hiddenValueMask = hiddenValueMask;
	}

	public String getCssRootPath() {
		return resourceServer + cssRootPath;
	}

	public void setCssRootPath(String cssRootPath) {
		this.cssRootPath = cssRootPath;
	}

	public String getJsRootPath() {
		return resourceServer + jsRootPath;
	}

	public void setJsRootPath(String jsRootPath) {
		this.jsRootPath = jsRootPath;
	}

	public String getAjaxServiceURL() {
		return ajaxServiceURL;
	}

	public void setAjaxServiceURL(String ajaxServiceURL) {
		this.ajaxServiceURL = ajaxServiceURL;
	}

	public String getFrameWrap() {
		return frameWrap;
	}

	public void setFrameWrap(String frameWrap) {
		this.frameWrap = frameWrap;
	}

	public boolean isJAAS() {
		return isJAAS;
	}

	public void setJAAS(boolean isJAAS) {
		this.isJAAS = isJAAS;
	}

	public boolean isFormatHTML() {
		return isFormatHTML;
	}

	public void setFormatHTML(boolean isFormatHTML) {
		this.isFormatHTML = isFormatHTML;
	}

	public boolean isHotdeployeable() {
		return hotdeployeable;
	}

	public void setHotdeployeable(boolean hotdeployeable) {
		this.hotdeployeable = hotdeployeable;
	}

	public String getLoginPath() {
		return loginPath;
	}

	public void setLoginPath(String loginPath) {
		this.loginPath = loginPath;
	}

	public String getActionPath() {
		return actionPath;
	}

	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}

	public String getIndexPage() {
		return indexPage;
	}

	public void setIndexPage(String indexPage) {
		this.indexPage = indexPage;
	}

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public String getIloginPage() {
		return iloginPage;
	}

	public void setIloginPage(String iloginPage) {
		this.iloginPage = iloginPage;
	}

	public String getMainPage() {
		return mainPage;
	}

	public void setMainPage(String mainPage) {
		this.mainPage = mainPage;
	}

	public String getErrorPage() {
		return errorPage;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}

	public String getNopermissionPage() {
		return nopermissionPage;
	}

	public void setNopermissionPage(String nopermissionPage) {
		this.nopermissionPage = nopermissionPage;
	}

	public String getTimeoutPage() {
		return timeoutPage;
	}

	public void setTimeoutPage(String timeoutPage) {
		this.timeoutPage = timeoutPage;
	}

	public String getHasAjaxErrorHandler() {
		return hasAjaxErrorHandler;
	}

	public void setHasAjaxErrorHandler(String hasAjaxErrorHandler) {
		this.hasAjaxErrorHandler = hasAjaxErrorHandler;
	}

	public String getIpLocationURL() {
		return ipLocationURL;
	}

	public void setIpLocationURL(String ipLocationURL) {
		this.ipLocationURL = ipLocationURL;
	}

	public String getMapwebkey() {
		return mapwebkey;
	}

	public void setMapwebkey(String mapwebkey) {
		this.mapwebkey = mapwebkey;
	}

	public String getMapappkey() {
		return mapappkey;
	}

	public void setMapappkey(String mapappkey) {
		this.mapappkey = mapappkey;
	}

	public String[] getCommoncss() {
		return commoncss;
	}

	public void setCommoncss(String[] commoncss) {
		this.commoncss = commoncss;
	}

	public String[] getCommonjs() {
		return commonjs;
	}

	public void setCommonjs(String[] commonjs) {
		this.commonjs = commonjs;
	}

	public String[] getCommonMobcss() {
		return commonMobcss;
	}

	public void setCommonMobcss(String[] commonMobcss) {
		this.commonMobcss = commonMobcss;
	}

	public String[] getCommonMobAppcss() {
		return commonMobAppcss;
	}

	public void setCommonMobAppcss(String[] commonMobAppcss) {
		this.commonMobAppcss = commonMobAppcss;
	}

	public String[] getCommonMobjs() {
		return commonMobjs;
	}

	public void setCommonMobjs(String[] commonMobjs) {
		this.commonMobjs = commonMobjs;
	}

	public String[] getCommonMobAppjs() {
		return commonMobAppjs;
	}

	public void setCommonMobAppjs(String[] commonMobAppjs) {
		this.commonMobAppjs = commonMobAppjs;
	}

	public String[] getSyncLoadJs() {
		return syncLoadJs;
	}

	public void setSyncLoadJs(String[] syncLoadJs) {
		this.syncLoadJs = syncLoadJs;
	}

	public Map<String, String[]> getSingleCommonCss() {
		return singleCommonCss;
	}

	public Map<String, String[]> getSingleCommonJs() {
		return singleCommonJs;
	}

	public Map<String, String[]> getSingleCommonAppCss() {
		return singleCommonAppCss;
	}

	public Map<String, String[]> getSingleCommonAppJs() {
		return singleCommonAppJs;
	}

	public List<String> getSkipBackButtonPages() {
		return skipBackButtonPages;
	}

	public void setSkipBackButtonPages(List<String> skipBackButtonPages) {
		this.skipBackButtonPages = skipBackButtonPages;
	}

	public List<String> getSkipCommonJsPages() {
		return skipCommonJsPages;
	}

	public void setSkipCommonJsPages(List<String> skipCommonJsPages) {
		this.skipCommonJsPages = skipCommonJsPages;
	}

	public List<String> getSkipCommonCssPages() {
		return skipCommonCssPages;
	}

	public void setSkipCommonCssPages(List<String> skipCommonCssPages) {
		this.skipCommonCssPages = skipCommonCssPages;
	}

	public String getContextRoot() {
		return contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}
	
}
