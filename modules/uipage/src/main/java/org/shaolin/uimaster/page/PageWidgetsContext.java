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
import org.shaolin.uimaster.page.exception.UIComponentNotFoundException;
import org.shaolin.uimaster.page.exception.UIPageException;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;

/**
 * TODO: considering add this to the cache.
 * 
 * @author Administrator
 * @deprecated
 */
public class PageWidgetsContext implements java.io.Serializable {

	private final String pageName;
	
	public PageWidgetsContext(String pageName) {
		this.pageName = pageName;
	}
	
	public void loadComponent(UserRequestContext context, 
			String uiid, String entityName, boolean isEntity) throws EntityNotFoundException, UIPageException {
	}
	
	public HTMLWidgetType getComponent(String uiid) 
		throws UIComponentNotFoundException {
		return null;
	}
	
	public String getPageName() {
		return this.pageName;
	}
	
	public void printAllComponents() {
	}
	
}
