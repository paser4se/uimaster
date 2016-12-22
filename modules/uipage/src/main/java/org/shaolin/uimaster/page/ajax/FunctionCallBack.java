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
package org.shaolin.uimaster.page.ajax;

import java.util.List;

import org.apache.log4j.Logger;
import org.shaolin.bmdp.datamodel.page.OpCallAjaxType;
import org.shaolin.bmdp.datamodel.page.OpType;
import org.shaolin.javacc.exception.EvaluationException;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.ajax.handlers.EventHandler;
import org.shaolin.uimaster.page.cache.PageCacheManager;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.cache.UIPageObject;

/**
 * 
 * @author wushaol
 *
 */
public class FunctionCallBack implements CallBack {

	private static Logger log = Logger.getLogger(EventHandler.class);
	
	private static final long serialVersionUID = -4745513005742512534L;

	private AjaxContext context;
	
	private final String functionName;
	
	public FunctionCallBack(AjaxContext context, String functionName) {
		this.context = context;
		this.functionName = functionName;
	}
	
	public void execute() {
		String uientity = context.getEntityName();
		List<OpType> ops = null;
		if (PageCacheManager.isUIPage(uientity)) {
			UIPageObject uipage = PageCacheManager.getUIPageObject(uientity);
			ops = uipage.getUIForm().getEventHandler(functionName);
		} else {
			UIFormObject uiEntity = PageCacheManager.getUIFormObject(uientity);
			ops = uiEntity.getEventHandler(functionName);
		}
		if (ops == null) {
			log.warn("The action name " + functionName + " can't be found from current page: " + uientity);
			return;
		}
		for (OpType op : ops) {
			if (op instanceof OpCallAjaxType) {
				OpCallAjaxType callAjaxOp = (OpCallAjaxType) op;
				try {
					callAjaxOp.getExp().evaluate(context);
					context.synchVariables();
				} catch (EvaluationException ex) {
					log.warn("This statement can not be evaluated: \n"+ callAjaxOp.getExp().getExpressionString(), ex);
				}
				break;
			}
		}
		context = null;// must release manually!
	}
}
