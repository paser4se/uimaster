package org.shaolin.uimaster.page.security;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.shaolin.bmdp.runtime.ce.CEUtil;
import org.shaolin.bmdp.runtime.ce.IConstantEntity;

public class UserContext {

	private static ThreadLocal<Object> userSessionCache = new ThreadLocal<Object>();

	private static ThreadLocal<String> userLocaleCache = new ThreadLocal<String>();

	private static ThreadLocal<List<IConstantEntity>> userRolesCache = new ThreadLocal<List<IConstantEntity>>();

	private static ThreadLocal<Boolean> userAccessMode = new ThreadLocal<Boolean>();
	
	private static ThreadLocal<Boolean> andriodAppDevice = new ThreadLocal<Boolean>();
	
	private static ThreadLocal<HttpSession> userSession = new ThreadLocal<HttpSession>();
	
	public static void registerCurrentUserContext(HttpSession session, Object userContext,
			String userLocale, List<IConstantEntity> userRoles, Boolean isMobileAccess) {
		userSession.set(session);
		userSessionCache.set(userContext);
		userLocaleCache.set(userLocale);
		userRolesCache.set(userRoles);
		userAccessMode.set(isMobileAccess);
	}
	
	public static void unregister() {
		userSession.set(null);
		userSessionCache.set(null);
		userLocaleCache.set(null);
		userRolesCache.set(null);
		userAccessMode.set(null);
	}

	public static Object getCurrentUserContext() {
		return userSessionCache.get();
	}

	public static void unregisterCurrentUserContext() {
		userSessionCache.set(null);
		userLocaleCache.set(null);
		userRolesCache.set(null);
	}

	public static void addUserData(String key, String value) {
		userSession.get().setAttribute(key, value);
	}
	
	public static Object getUserData(String key) {
		return userSession.get().getAttribute(key);
	}
	
	public static String getUserLocale() {
		return userLocaleCache.get();
	}

	public static List<IConstantEntity> getUserRoles() {
		return userRolesCache.get();
	}
	
	public static boolean hasRole(String role) {
		for (IConstantEntity exist : userRolesCache.get()) {
			if (role.equals(CEUtil.getValue(exist))) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isMobileRequest() {
		if (userAccessMode.get() == null) {
			return false;
		}
		return userAccessMode.get();
	}
	
	public static boolean isAppClient() {
		if (andriodAppDevice.get() == null) {
			return false;
		}
		return andriodAppDevice.get();
	}
	
	public static void setAppClient(HttpServletRequest request) {
		String appClient = request.getParameter("_appclient");
		if (appClient != null) {
			andriodAppDevice.set(true);
		} 
	}

}
