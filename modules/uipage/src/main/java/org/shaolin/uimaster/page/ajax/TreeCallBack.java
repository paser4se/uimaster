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

import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.uimaster.page.AjaxContextHelper;

/**
 * Due to javacc does not support the anonymous class definition in the script directly.
 * 
 * @author wushaol
 *
 */
public class TreeCallBack implements CallBack {

	private String uiid;
	
	private String entityPrefix;
	
	public TreeCallBack() {}//for serialization.
	
	public TreeCallBack(final String uiid) {
		this.entityPrefix = AjaxContextHelper.getAjaxContext().getEntityPrefix();
		this.uiid = uiid;
	}
	
	public void execute(Object... objects) {
		Tree tree = (Tree)AjaxContextHelper.getAjaxContext().getElementByAbsoluteId(entityPrefix + uiid, Tree.class);
		tree.refresh();
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("uiid", uiid);
		json.put("prefix", entityPrefix);
		return json;
	}
	
	public void fromJSON(JSONObject json) throws JSONException {
		this.uiid = json.getString("uiid");
		this.entityPrefix = json.getString("prefix");
	}
}
