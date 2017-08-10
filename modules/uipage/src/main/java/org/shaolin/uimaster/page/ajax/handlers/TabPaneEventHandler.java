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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.shaolin.uimaster.page.AjaxContextHelper;
import org.shaolin.bmdp.persistence.HibernateUtil;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.ajax.TabPane;

/**
 * Property Change handler.
 * 
 * @author swu
 */
public class TabPaneEventHandler implements IAjaxHandler {
	private static Logger log = Logger.getLogger(TabPaneEventHandler.class);

	public TabPaneEventHandler() {
	}

	public String trigger(AjaxContext context) throws AjaxHandlerException {
		try {
			AjaxContextHelper.createAjaxContext(context);
			String propertyName = context.getRequest().getParameter(
					"_valueName");
			String index = context.getRequest().getParameter("_value");
			if ("removePage".equals(propertyName)) {
				AjaxContextHelper.getAjaxContext().removeFramePage(index);
				return "";
			} else if ("removeExcludedPage".equals(propertyName)) {
				boolean isRoot = index.equals(AjaxContext.GLOBAL_PAGE);
				List<String> list = AjaxContextHelper.getAllCachedPages(context.getRequest().getSession());
				if (list == null) {
					return "";
				}
				list = new ArrayList<String>(list);
				for (String page: list) {
					if (!isRoot && page.equals(AjaxContext.GLOBAL_PAGE)) {
						continue;
					}
					if (!page.equals(index)) {
						AjaxContextHelper.getAjaxContext().removeFramePage(page);
					}
				}
				return "";
			} 
			
			String uiid = context.getRequest().getParameter(
					AjaxContext.AJAX_UIID);
			TabPane comp = (TabPane) context.getElement(uiid);
			if (log.isDebugEnabled())
				log.debug("uiid: " + uiid + ",propertyName: "
						+ propertyName + ",newValue: " + index);
			if ("selectedIndex".equals(propertyName)) {
				comp.addAttribute("selectedIndex", index, false);
			} 
			if(!comp.loadContent(Integer.valueOf(index))) {
				comp.syncSelectedAction(context);
			}
			
			return AjaxContextHelper.getAjaxContext().getDataAsJSON();
		} catch (Exception e) {
			throw new AjaxHandlerException("Error", e);
		} finally {
			HibernateUtil.releaseSession(HibernateUtil.getSession(), true);
			AjaxContextHelper.removeAjaxContext();
		}
	}
}
