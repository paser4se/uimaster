package org.shaolin.uimaster.page.security;

import java.util.List;

import org.shaolin.bmdp.runtime.ce.CEUtil;
import org.shaolin.bmdp.runtime.ce.IConstantEntity;

public class UserContext {

	private static ThreadLocal<Object> userSessionCache = new ThreadLocal<Object>();

	private static ThreadLocal<String> userLocaleCache = new ThreadLocal<String>();

	private static ThreadLocal<List<IConstantEntity>> userRolesCache = new ThreadLocal<List<IConstantEntity>>();

	private static ThreadLocal<Boolean> userAccessMode = new ThreadLocal<Boolean>();
	
	public static void registerCurrentUserContext(Object userContext,
			String userLocale, List<IConstantEntity> userRoles, Boolean isMobileAccess) {
		userSessionCache.set(userContext);
		userLocaleCache.set(userLocale);
		userRolesCache.set(userRoles);
		userAccessMode.set(isMobileAccess);
	}

	public static Object getCurrentUserContext() {
		return userSessionCache.get();
	}

	public static void unregisterCurrentUserContext() {
		userSessionCache.set(null);
		userLocaleCache.set(null);
		userRolesCache.set(null);
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

}
