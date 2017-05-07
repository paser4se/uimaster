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

import org.shaolin.bmdp.runtime.entity.EntityNotFoundException;
import org.shaolin.uimaster.page.cache.PageCacheManager;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.cache.UIPageObject;
import org.shaolin.uimaster.page.exception.UIComponentNotFoundException;
import org.shaolin.uimaster.page.exception.UIPageException;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: considering add this to the cache.
 * 
 * @author Administrator
 * @deprecated
 */
public class PageWidgetsContext implements java.io.Serializable {

	private static final Logger logger = LoggerFactory.getLogger(PageWidgetsContext.class);
	
	private final String pageName;
	
	private UIFormObject uientityObject;
	
	public PageWidgetsContext(String pageName) {
		this.pageName = pageName;
	}
	
	public void loadComponent(UserRequestContext context, 
			String uiid, String entityName, boolean isEntity) throws EntityNotFoundException, UIPageException {
		if (isEntity) {
			uientityObject = PageCacheManager.getUIFormObject(entityName);
		} else {
			UIPageObject pageObject = PageCacheManager.getUIPageObject(entityName);
			uientityObject = pageObject.getUIForm();
		}
		
//		if (logger.isDebugEnabled()) {
//			logger.debug("Create html components for Entity: {}", entityName);
//		}
//
//		Iterator<String> i = uientityObject.getAllComponentID();
//		while (i.hasNext()) {
//			String compId = i.next();
//			Map<String, Object> propMap = uientityObject.getComponentProperty(compId);
//			Map eventMap = uientityObject.getComponentEvent(compId);
//			String UIID = uiid + compId;
//			Boolean readOnly = Boolean.FALSE;
//			Map tempMap = new HashMap();
//			HTMLLayoutType layout = HTMLUtil.getHTMLLayoutType("CellLayoutType");
//
//			HTMLWidgetType component = HTMLUtil.getHTMLUIComponent(UIID, context, propMap, eventMap, readOnly, tempMap,
//					layout, false);
//			component.setPrefix(context.getHTMLPrefix());
//			if (component instanceof HTMLReferenceEntityType) {
//				HTMLReferenceEntityType oldReferObject = ((HTMLReferenceEntityType) component);
//				oldReferObject.setType((String) propMap.get("referenceEntity"));
//			}
//
//			components.put(component.getName(), component);
//			if (logger.isDebugEnabled()) {
//				logger.debug("Create component: {}, type: {}", component.getName(), component);
//			}
//		}
	}
	
	public HTMLWidgetType getComponent(String uiid) 
		throws UIComponentNotFoundException {
		if (uientityObject.getComponents().containsKey(uiid)) {
			return uientityObject.getComponents().get(uiid);
		} else {
			throw new UIComponentNotFoundException(
					"the component does not exist in the cache. uiid: " + uiid );
		}
	}
	
	public String getPageName() {
		return this.pageName;
	}
	
	public void printAllComponents() {
		uientityObject.printAllComponents();
	}
	
}
