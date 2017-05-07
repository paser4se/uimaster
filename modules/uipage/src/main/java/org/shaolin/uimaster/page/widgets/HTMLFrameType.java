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
package org.shaolin.uimaster.page.widgets;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.cache.UIFormObject;

public class HTMLFrameType extends HTMLWidgetType implements Serializable {
	private static final Logger logger = Logger.getLogger(HTMLImageType.class);
	public static final String NEED_SRC = "needSrc";
	private static final long serialVersionUID = 3905195996648815472L;

	public HTMLFrameType(String id) {
		super(id);
	}

	@Override
	public void generateBeginHTML(UserRequestContext context, UIFormObject ownerEntity, int depth) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void generateEndHTML(UserRequestContext context, UIFormObject ownerEntity, int depth) {
		try {
			HttpServletRequest request = context.getRequest();
			String name = getName();
			context.generateHTML("<iframe id=\"");
			context.generateHTML(name);
			context.generateHTML("\" name=\"");
			context.generateHTML(name);
			context.generateHTML("\" src=\"");
			StringBuffer results = new StringBuffer("webflow.do?z=1");
			appendParam(results, "_chunkname", this.getAttribute("_chunkname").toString(), true);
			appendParam(results, "_nodename", this.getAttribute("_nodename").toString(), true);
			appendParam(results, "_framename", getId(), true);
			String superPrefix = (String) request.getAttribute("_framePagePrefix");
			if (superPrefix != null) {
				appendParam(results, "_framePrefix", superPrefix, true);
			}
			context.generateHTML(results.toString());
			context.generateHTML("\"");
			generateAttributes(context);
//			generateEventListeners(context);
			context.generateHTML(" frameborder=\"0\" style=\"");
			//{height=400px, visible=true, align=FULL, width=100%, y=1, cellUIStyle=, x=0}
			context.generateHTML("height:");
			if (this.getHTMLLayout().getAttribute("height") != null) {
				context.generateHTML(this.getHTMLLayout().getAttribute("height").toString());
			} else {
				context.generateHTML("100%");
			}
			context.generateHTML(";width:");
			if (this.getHTMLLayout().getAttribute("width") != null) {
				context.generateHTML(this.getHTMLLayout().getAttribute("width").toString());
			} else {
				context.generateHTML("100%");
			}
			context.generateHTML(";\" scrolling='yes'");
			if (request.getAttribute("_tabcontent") != null) {
				context.generateHTML("_tabcontent=\"true\"");
			}
			context.generateHTML("></iframe>");
		} catch (Exception e) {
			logger.error("error. in entity: " + getUIEntityName(), e);
		}
	}

	private StringBuffer appendParam(StringBuffer sb, String key, String value,
			boolean hasParams) {
		if (hasParams) {
			sb.append("&");
		} else {
			sb.append("?");
		}
		sb.append(key);
		sb.append("=");
		sb.append(value);
		return sb;
	}

	private boolean isNeedSrc() {
		return !"false".equals((String) getAttribute(NEED_SRC));
	}

	public void generateAttribute(UserRequestContext context,
			String attributeName, Object attributeValue) throws IOException {
		if ("width".equals(attributeName)) {
			String attrValue = (String) attributeValue;
			attrValue = getLengthValue(attrValue);
			if (attrValue != null) {
				context.generateHTML(" width=");
				context.generateHTML(attrValue);
			}
		} else if ("height".equals(attributeName)) {
			String attrValue = (String) attributeValue;
			attrValue = getLengthValue(attrValue);
			if (attrValue != null) {
				context.generateHTML(" height=");
				context.generateHTML(attrValue);
			}
		} else {
			super.generateAttribute(context, attributeName, attributeValue);
		}
	}

	private String getLengthValue(String stringValue) {
		try {
			double doubleValue = Double.parseDouble(stringValue);
			if (doubleValue == -1.0D) {
				return "100%";
			}
			if (doubleValue >= 1.0D) {
				return String.valueOf((int) doubleValue);
			}

			return null;
		} catch (NumberFormatException e) {
			logger.warn(e);
		}
		return null;
	}

	public boolean isEditPermissionEnabled() {
		return false;
	}

}