package org.shaolin.uimaster.page.skin;

import java.util.Collections;

import org.shaolin.bmdp.datamodel.page.UIPanelType;
import org.shaolin.uimaster.html.layout.IUISkin;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.WebConfig;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;

public class TitlePanel extends BaseSkin implements IUISkin {
	protected void initParam() {
		addParam("text", "");
		addParam("collapsed", "false");
		addParam("visible", "true");
		addParam("skin_titlepanel_content", "skin_titlepanel_content");
	}

	public boolean isOverwrite() {
		return false;
	}

	public void generatePreCode(HTMLWidgetType component)
			throws java.io.IOException {
		UserRequestContext context = UserRequestContext.UserContext.get();
		String webRoot = WebConfig.getResourceContextRoot();
		String name = component.getName();
		String titlePanelId = name + ".titlePanel";
		String arrowIconId = name + ".arrowIcon";
		String msgLocationId = name + ".msgLocation";
		String wrapperPanelId = name + ".wrapperPanel";
		context.generateHTML("<div class=\"skin_titlepanel_table\" name=\"titlePanel\" id=\"");
		context.generateHTML(titlePanelId);
		context.generateHTML("\"");
		if ("false".equals(component.getAttribute("visible"))
				|| "false".equals(getParam("visible"))) {
			context.generateHTML(" style=\"display:none;\"");
		} else {
			String w = getParam("width");
			if (w != null && w.trim().length() > 0) {
				context.generateHTML(" style=\"width:"+w+";\"");
			}
		}
		context.generateHTML(" ><div class=\"skin_titlepanel_title\" ");
		if ("true".equals(getParam("collapsed"))) {
			context.generateHTML("onclick=\"javascript:showAndHide($(this).next().children());\" ");
		}
		context.generateHTML("><div>");
		context.generateHTML(getParam("text"));
		context.generateHTML("</div></div>");
		context.generateHTML("<div class=\"");
		context.generateHTML(getParam("skin_titlepanel_content"));
		context.generateHTML("\"><div id=\"");
		context.generateHTML(wrapperPanelId);
		context.generateHTML("\"><div id=\"");
		context.generateHTML(msgLocationId);
		context.generateHTML("\"></div>");
	}

	public void generatePostCode(HTMLWidgetType component)
			throws java.io.IOException {
		UserRequestContext context = UserRequestContext.UserContext.get();
		if ("true".equals(getParam("collapsed"))) {
			context.generateHTML("<script>document.getElementById(\"");
			context.generateHTML(component.getName() + ".wrapperPanel");
			context.generateHTML("\").style.display='none';</script>");
		}
		context.generateHTML("</div></div></div>");
	}

	public Class getUIComponentType() {
		return UIPanelType.class;
	}
}
