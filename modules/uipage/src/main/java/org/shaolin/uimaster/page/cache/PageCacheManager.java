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
package org.shaolin.uimaster.page.cache;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.shaolin.bmdp.datamodel.page.OpCallAjaxType;
import org.shaolin.bmdp.datamodel.page.WebService;
import org.shaolin.bmdp.runtime.cache.CacheManager;
import org.shaolin.bmdp.runtime.cache.ICache;
import org.shaolin.bmdp.runtime.entity.EntityNotFoundException;
import org.shaolin.javacc.context.DefaultParsingContext;
import org.shaolin.javacc.exception.ParsingException;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.TransOpsExecuteContext;
import org.shaolin.uimaster.page.exception.UIPageException;

/**
 * The ui entities shared for the whole system, which can be multiple applications running.
 * 
 * @author wushaol
 *
 */
public class PageCacheManager {
	private static final String PAGE_OD_CACHE = "__sys_uipage_odcache";

	private static final String OD_CACHE = "__sys_od_cache";

	private static final String UI_CACHE = "__sys_uipage_cache";
	
	private static final String UI_Entity_CACHE = "__sys_uiform_cache";

	private static final String WEB_SERVICE_CACHE = "__sys_webservice_cache";
	
    private static ICache<String, UIPageObject> uipageCache;
    
    private static ICache<String, UIFormObject> uiformCache;
	
	private static ICache<String, ODPageObject> pageODCache;

	private static ICache<String, ODFormObject> odCache;
	
	private static ICache<String, Map> webServiceCache;
	
	static {
		uipageCache = CacheManager.getInstance().getCache(UI_CACHE, String.class, 
				UIPageObject.class);
		uiformCache = CacheManager.getInstance().getCache(UI_Entity_CACHE, String.class, 
				UIFormObject.class);
		pageODCache = CacheManager.getInstance().getCache(PAGE_OD_CACHE,
				String.class, ODPageObject.class);
		odCache = CacheManager.getInstance().getCache(OD_CACHE, String.class,
				ODFormObject.class);
		webServiceCache = CacheManager.getInstance().getCache(WEB_SERVICE_CACHE, String.class,
				Map.class);
	}

	public static void addWebService(WebService webService) throws ParsingException {
		if (!webServiceCache.containsKey(webService.getEntityName())) {
			webServiceCache.put(webService.getEntityName(), new HashMap());
		}
		Map services = webServiceCache.get(webService.getEntityName());
		services.clear();
		
		TransOpsExecuteContext opContext = new TransOpsExecuteContext();
        DefaultParsingContext globalPContext = new DefaultParsingContext();
        globalPContext.setVariableClass("request", HttpServletRequest.class);
        globalPContext.setVariableClass("page", AjaxContext.class);
        opContext.setParsingContextObject("@", globalPContext);
		
		for (OpCallAjaxType ops : webService.getServices()) {
			if (services.containsKey(ops.getName())) {
				throw new IllegalStateException("The web service name is duplicated: " + webService.getEntityName() +"."+ops.getName());
			}
			services.put(ops.getName(), ops);
			
			if (ops.getFilter() != null) {
				ops.getFilter().parse(opContext);
			}
			ops.getExp().parse(opContext);
		}
	}
	
	public static OpCallAjaxType getWebService(String entityName, String serviceName) {
		if (!webServiceCache.containsKey(entityName)) {
			throw new IllegalStateException("The web service name is not defined: " + entityName +"."+serviceName);
		}
		Map services = webServiceCache.get(entityName);
		OpCallAjaxType ops = (OpCallAjaxType)services.get(serviceName);
		if (ops == null) {
			throw new IllegalStateException("The web service name is not defined: " + entityName +"."+serviceName);
		}
		return ops;
	}
	
	
	public static ODPageObject getODPageEntityObject(String pageName)
			throws ParsingException, ClassNotFoundException, EntityNotFoundException {
		ODPageObject odPageEntity = pageODCache.get(pageName);
		if (odPageEntity == null) {
			odPageEntity = new ODPageObject(pageName);
			ODPageObject old = pageODCache.putIfAbsent(pageName, odPageEntity);
			odPageEntity = (old != null) ? old : odPageEntity;
		} 
		return odPageEntity;
	}

	public static ODFormObject getODFormObject(String formName)
			throws ParsingException, ClassNotFoundException, EntityNotFoundException {
		ODFormObject odEntityObject = odCache.get(formName);
		if (odEntityObject == null) {
			odEntityObject = new ODFormObject(formName);
			ODFormObject old = odCache.putIfAbsent(formName, odEntityObject);
			odEntityObject = (old != null) ? old : odEntityObject;
		} 
		return odEntityObject;
	}
	
	public static UIPageObject getUIPageObject(String pageName) throws EntityNotFoundException, UIPageException {
		UIPageObject pageObject = uipageCache.get(pageName);
		if (pageObject == null) {
			pageObject = new UIPageObject(pageName);
			UIPageObject old = uipageCache.putIfAbsent(pageName, pageObject);
			pageObject = (old != null) ? old : pageObject;
		} 
		return pageObject;
	}
	
	public static UIFormObject getUIFormObject(String entityName) throws EntityNotFoundException, UIPageException {
		UIFormObject formObject = uiformCache.get(entityName);
		if (formObject == null) {
			formObject = new UIFormObject(entityName);
			UIFormObject old = uiformCache.putIfAbsent(entityName, formObject);
			formObject = (old != null) ? old : formObject;
		}
		return formObject;
	}
	
	public static boolean isUIPage(String entityName) {
		return uipageCache.containsKey(entityName);
	}
	
	public static void removeUIPageCache(String pageName) {
		pageODCache.remove(pageName);
		uipageCache.remove(pageName);
	}
	
	public static void removeUIFormCache(String pageName) {
		odCache.remove(pageName);
		uiformCache.remove(pageName);
	}

}
