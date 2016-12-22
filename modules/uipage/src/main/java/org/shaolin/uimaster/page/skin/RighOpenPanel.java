package org.shaolin.uimaster.page.skin;

import java.util.concurrent.atomic.AtomicInteger;

import org.shaolin.bmdp.datamodel.page.UIPanelType;
import org.shaolin.uimaster.html.layout.IUISkin;
import org.shaolin.uimaster.page.HTMLSnapshotContext;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;

public class RighOpenPanel extends BaseSkin implements IUISkin {
	
	private static AtomicInteger id = new AtomicInteger();
	
	protected void initParam() {
		addParam("openAction", "");
	}

	public boolean isOverwrite() {
		return false;
	}

	public java.util.Map getAttributeMap(HTMLWidgetType component) {
		return null;
	}

	public void generatePreCode(HTMLWidgetType component)
			throws java.io.IOException {
		HTMLSnapshotContext context = component.getContext();
		
		String panelId = "rightopenpanel" + id.incrementAndGet();
		String uiid = component.getUIID();
		context.generateHTML("<div id=\"");
		context.generateHTML(panelId);
		context.generateHTML("\" class=\"ui-icon ui-icon-carat-1-e uimaster-rightopenpanel\" style=\"display:none;\" onclick=\"javascript:defaultname.");
		context.generateHTML(context.getHTMLPrefix() + this.getParam("openAction"));
		context.generateHTML("(defaultname." + uiid + ", event)\">");
		context.generateHTML("</div>");
		context.generateHTML("<script>");
		context.generateHTML("UIMaster.pageInitFunctions.push(function(){\n");
		context.generateHTML("UIMaster.ajustToMiddle('#"+panelId+"');");
		context.generateHTML("});\n");
		context.generateHTML("</script>");
	}

	public void generatePostCode(HTMLWidgetType component)
			throws java.io.IOException {
	}

	public Class getUIComponentType() {
		return UIPanelType.class;
	}
}
