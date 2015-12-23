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

import org.shaolin.uimaster.page.AjaxActionHelper;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.ajax.Tree;

/**
 * Tree Event Handler.
 * 
 * @author swu
 */
public class TreeEventHandler implements IAjaxHandler {

	public TreeEventHandler() {
	}

	public String trigger(AjaxContext context) throws AjaxHandlerException {
		AjaxActionHelper.createAjaxContext(context);
		String uiid = context.getRequest().getParameter(AjaxContext.AJAX_UIID);
		String actionName = context.getRequest().getParameter("_actionName");
		Tree comp = context.getTree(uiid);
		if (comp == null) {
			throw new AjaxHandlerException(uiid + " Tree does not exist!");
		}
		if (actionName.endsWith("expand")) {
			return comp.expand();
		} 
		return "[]";
	}
}
