package org.shaolin.uimaster.page.skin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.shaolin.uimaster.html.layout.IUISkin;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;

public abstract class BaseSkin implements IUISkin {
	private final Map<String, String> paramMap;

	public BaseSkin() {
		paramMap = new HashMap<String, String> ();
		initParam();
	}

	public final void setParam(String name, String value) {
		paramMap.put(name, value);
	}

	public final String[] getParamNames() {
		return (String[]) paramMap.keySet().toArray(new String[] {});
	}

	protected final String getParam(String name) {
		return (String) paramMap.get(name);
	}

	protected final void addParam(String name, String defaultValue) {
		paramMap.put(name, defaultValue);
	}

	protected abstract void initParam();

	public java.util.Map getAttributeMap(HTMLWidgetType component) {
		return Collections.emptyMap();
	}
}
