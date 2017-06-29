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

import org.apache.log4j.Logger;
import org.shaolin.uimaster.page.AjaxContextHelper;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.ajax.PreNextPanel;

/**
 * Property Change handler.
 * 
 * @author swu
 */
public class PreNextPanelEventHandler implements IAjaxHandler {
	private static Logger log = Logger.getLogger(PreNextPanelEventHandler.class);

	public PreNextPanelEventHandler() {
	}

	public String trigger(AjaxContext context) throws AjaxHandlerException {
		try {
			AjaxContextHelper.createAjaxContext(context);
			String uiid = context.getRequest().getParameter(
					AjaxContext.AJAX_UIID);
			String propertyName = context.getRequest().getParameter(
					"_valueName");
			String index = context.getRequest().getParameter("_value");
			PreNextPanel comp = (PreNextPanel) context.getElement(uiid);
			if (log.isDebugEnabled())
				log.debug("uiid: " + uiid + ",propertyName: "
						+ propertyName + ",newValue: " + index);
			if ("prevbtn".equals(propertyName)) {
				comp.invokePreAction(context);
			} else if ("nextbtn".equals(propertyName)) {
				comp.invokeNextAction(context);
			} else if ("selectedIndex".equals(propertyName)) {
				comp.setSelectedIndex(Integer.parseInt(index));
			} else {
			}
			return AjaxContextHelper.getAjaxContext().getDataAsJSON();
		} catch (Exception e) {
			throw new AjaxHandlerException("Error", e);
		} finally {
			AjaxContextHelper.removeAjaxContext();
		}
	}
}
