package org.shaolin.uimaster.page;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class DistributedHttpSession implements HttpSession {

	private HashMap<String, Object> attribute = new HashMap<String, Object>();
	
	@Override
	public Object getAttribute(String arg0) {
		return attribute.get(arg0);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return null;
	}

	@Override
	public long getCreationTime() {
		return 0;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public long getLastAccessedTime() {
		return 0;
	}

	@Override
	public int getMaxInactiveInterval() {
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return null;
	}

	@Override
	public Object getValue(String arg0) {
		return attribute.get(arg0);
	}

	@Override
	public String[] getValueNames() {
		return null;
	}

	@Override
	public void invalidate() {
		
	}

	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public void putValue(String arg0, Object arg1) {
		attribute.put(arg0, arg1);
	}

	@Override
	public void removeAttribute(String arg0) {
		attribute.remove(arg0);
	}

	@Override
	public void removeValue(String arg0) {
		attribute.remove(arg0);
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		attribute.put(arg0, arg1);
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
		
	}
	
}
