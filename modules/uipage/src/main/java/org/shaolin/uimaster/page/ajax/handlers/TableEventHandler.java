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

import org.hibernate.criterion.Order;
import org.shaolin.bmdp.runtime.security.UserContext;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.AjaxContextHelper;
import org.shaolin.uimaster.page.ajax.Table;
import org.shaolin.uimaster.page.ajax.TableConditions;

/**
 * Table Event Handler.
 * 
 * @author swu
 */
public class TableEventHandler implements IAjaxHandler {

	public TableEventHandler() {
	}

	public String trigger(AjaxContext context) throws AjaxHandlerException {
		AjaxContextHelper.createAjaxContext(context);
		String uiid = context.getRequest().getParameter(AjaxContext.AJAX_UIID);
		String actionName = context.getRequest().getParameter("_actionName");
		Table comp = context.getTable(uiid);
		if (comp == null) {
			throw new AjaxHandlerException(uiid + " Table does not exist!");
		}
		if (actionName.endsWith("pull")) {
			String pullAction = context.getRequest().getParameter("_value");
			TableConditions conditions = comp.getConditions();
			if (pullAction != null && pullAction.length() > 0) {
				conditions.setPullAction(pullAction);
			} else {
				int offset = Integer
						.parseInt(context.getRequest().getParameter("start"));
				int count = Integer
						.parseInt(context.getRequest().getParameter("length"));
				if (context.getRequest().getParameter("order[0][column]") == null) {
					return "";
				}
				int columnIndex = Integer.parseInt(context.getRequest().getParameter(
						"order[0][column]"));
				String v = context.getRequest().getParameter("order[0][dir]");// :desc/asc
				boolean isAscending = "asc".equals(v);
				
				conditions.setCount(count);
				conditions.setOffset(offset);
				// TODO: only one order support temporary.
				String colId = comp.getColumnId(columnIndex-1);
				if (colId != null) {
					conditions.clearOrder();
					conditions.addOrder(isAscending ? Order.asc(colId) : Order
							.desc(colId));
				}
			}
			UserContext.getUserContext().setPullAction("filter");
			try {
				return comp.refresh0();
			} finally {
				try {
					context.getDataAsJSON();
				} catch (Exception e) {
					throw new AjaxHandlerException(e.getMessage(), e);
				}
			}
		} else if (actionName.endsWith("chart")) {
			comp.showStatistic();
			try {
				return context.getDataAsJSON();
			} catch (Exception e) {
				throw new AjaxHandlerException(e.getMessage(), e);
			}
		} else {
			throw new AjaxHandlerException("Unsupported table action: " + actionName);
		}
	}
}
