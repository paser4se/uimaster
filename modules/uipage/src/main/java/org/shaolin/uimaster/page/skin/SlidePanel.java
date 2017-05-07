package org.shaolin.uimaster.page.skin;

import org.shaolin.bmdp.datamodel.page.UIPanelType;
import org.shaolin.uimaster.html.layout.IUISkin;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;

public class SlidePanel extends BaseSkin implements IUISkin {
	protected void initParam() {
		addParam("leftPanel", "");
		addParam("rightPanel", "");
		//addParam("ratio", "");
	}

	public boolean isOverwrite() {
		return false;
	}

	public void generatePreCode(HTMLWidgetType component)
			throws java.io.IOException {
	}

	public void generatePostCode(HTMLWidgetType component)
			throws java.io.IOException {
		UserRequestContext context = UserRequestContext.UserContext.get();
		
		String uiid = component.getUIID();
		context.generateHTML("<script>");
		context.generateHTML("UIMaster.pageInitFunctions.push(function(){\n");
		context.generateHTML("sideBar(\"" + context.getHTMLPrefix() + uiid
				+ "\", \"" + context.getHTMLPrefix() + this.getParam("leftPanel")
				+ "\", \"" + context.getHTMLPrefix() + this.getParam("rightPanel")
				+ "\"); $(window).resize(function() {sideBar(\"" + context.getHTMLPrefix() + uiid
				+ "\", \"" + context.getHTMLPrefix() + this.getParam("leftPanel")
				+ "\", \"" + context.getHTMLPrefix() + this.getParam("rightPanel")
				+ "\");});");
		context.generateHTML("});\n");
		context.generateHTML("</script>");
	}

	public Class getUIComponentType() {
		return UIPanelType.class;
	}
}
