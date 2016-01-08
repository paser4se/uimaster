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

import org.apache.log4j.Logger;
import org.shaolin.uimaster.page.HTMLSnapshotContext;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.security.UserContext;

public class HTMLCountDownType extends HTMLWidgetType {
	
	private static final long serialVersionUID = 1587046878874940935L;

	private static final Logger logger = Logger.getLogger(HTMLCountDownType.class);

	public HTMLCountDownType() {
	}

	public HTMLCountDownType(HTMLSnapshotContext context) {
		super(context);
	}

	public HTMLCountDownType(HTMLSnapshotContext context, String id) {
		super(context, id);
	}

	@Override
	public void generateBeginHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth) {
		if (context.getRequest().getAttribute("_hasCountDown") == null) {
			context.getRequest().setAttribute("_hasCountDown", Boolean.TRUE);
			HTMLUtil.generateTab(context, depth);
	    	String root = (UserContext.isMobileRequest() && UserContext.isAppClient()) 
	    			? WebConfig.getAppResourceContextRoot() : WebConfig.getResourceContextRoot();
			context.generateHTML("<!--[if IE]><script type=\"text/javascript\" src=\""+root+"/js/controls/countdown/excanvas.js\"></script><![endif]-->\n");
			context.generateHTML("<script type=\"text/javascript\" src=\""+root+"/js/controls/countdown/jquery.ba-throttle-debounce.min.js\"></script>\n");
			context.generateHTML("<script type=\"text/javascript\" src=\""+root+"/js/controls/countdown/jquery.knob.min.js\"></script>\n");
			context.generateHTML("<script type=\"text/javascript\" src=\""+root+"/js/controls/countdown/jquery.redcountdown.js\"></script>\n");
		}
	}
	
	@Override
	public void generateEndHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth) {
		try {
			context.generateHTML("<div id=\"");
            context.generateHTML(getName());
            context.generateHTML("\" name=\"");
            context.generateHTML(getName());
            context.generateHTML("\"");
            generateAttributes(context);
            generateEventListeners(context);
            context.generateHTML("class=\"uimastet_countdown\"></div>");
		} catch (Exception e) {
			logger.error("error. in entity: " + getUIEntityName(), e);
		}
	}

}
