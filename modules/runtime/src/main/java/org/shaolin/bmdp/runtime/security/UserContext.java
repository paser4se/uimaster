package org.shaolin.bmdp.runtime.security;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.shaolin.bmdp.runtime.ce.CEUtil;
import org.shaolin.bmdp.runtime.ce.IConstantEntity;

public class UserContext implements Serializable {

	private static final long serialVersionUID = -6470104359346201893L;

	long userId;
	
	String userName;
	
	String userAccount;
	
	String userLocale;
	
	String userLocation;
	
	String lastLoginDate;
	
	boolean isAdmin;
	
	List<IConstantEntity> userRoles;
	
	long orgId;
	
	String orgCode;
	
	String orgType;
	
	String orgName;
	
	public UserContext() {
	}
	
	public boolean isAdmin() {
		return this.isAdmin;
	}
	
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long id) {
		this.userId = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}
	
	public String getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgType() {
		return this.orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public void setUserLocale(String userLocale) {
		this.userLocale = userLocale;
	}
	
	public String getLocale() {
		return this.userLocale;
	}

	public void setUserRoles(List<IConstantEntity> userRoles) {
		this.userRoles = userRoles;
	}
	
	public List<IConstantEntity> getUserRole() {
		return this.userRoles;
	}

	public static final String CURRENT_USER_ORGID = "CurrentUserOrgId";
	
	public static final String CURRENT_USER_ORGNAME = "CurrentUserOrgName";

	public static final String CURRENT_USER_ORGTYPE = "CurrentUserOrgType";
	
	public static final String CURRENT_USER_ID = "CurrentUserId";
	
	private static ThreadLocal<UserContext> userSessionCache = new ThreadLocal<UserContext>();

	private static ThreadLocal<String> userLocaleCache = new ThreadLocal<String>();

	private static ThreadLocal<List<IConstantEntity>> userRolesCache = new ThreadLocal<List<IConstantEntity>>();

	private static ThreadLocal<Boolean> userAccessMode = new ThreadLocal<Boolean>();
	
	private static ThreadLocal<String> andriodAppDevice = new ThreadLocal<String>();
	
	private static ThreadLocal<HttpSession> userSession = new ThreadLocal<HttpSession>();
	
	public static void register(HttpSession session, UserContext userContext,
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
	
	public static UserContext getUserContext() {
		return userSessionCache.get();
	}

	public static void addUserData(String key, String value) {
		userSession.get().setAttribute(key, value);
	}
	
	public static Object getUserData(String key) {
		if (userSessionCache.get() == null) {
			return null;
		}
		if (key.equals(CURRENT_USER_ORGID)) {
			return userSessionCache.get().getOrgId();
		}
		if (key.equals(CURRENT_USER_ORGNAME)) {
			return userSessionCache.get().getOrgCode();
		}
		if (key.equals(CURRENT_USER_ORGTYPE)) {
			return userSessionCache.get().getOrgType();
		}
		if (key.equals("CurrentUserId")) {
			return userSessionCache.get().getUserId();
		}
		if (key.equals("CurrentUserAccount")) {
			return userSessionCache.get().getUserAccount();
		}
		
		return userSession.get().getAttribute(key);
	}
	
	public static Object getUserData(String key, boolean needException) {
		if (userSessionCache.get() == null) {
			throw new IllegalStateException("No user context exist!");
		}
		
		if (key.equals(CURRENT_USER_ORGID)) {
			return userSessionCache.get().getOrgId();
		}
		if (key.equals(CURRENT_USER_ORGNAME)) {
			return userSessionCache.get().getOrgCode();
		}
		if (key.equals(CURRENT_USER_ORGTYPE)) {
			return userSessionCache.get().getOrgType();
		}
		if (key.equals("CurrentUserId")) {
			return userSessionCache.get().getUserId();
		}
		if (key.equals("CurrentUserAccount")) {
			return userSessionCache.get().getUserAccount();
		}
		
		Object value = userSession.get().getAttribute(key);
		if (value == null) {
			throw new IllegalArgumentException(key + " can not be found!");
		}
		return value;
	}
	
	public static String getUserLocale() {
		return userLocaleCache.get();
	}

	public static List<IConstantEntity> getUserRoles() {
		return userRolesCache.get();
	}
	
	public static boolean hasRole(String role) {
		if (userRolesCache.get() == null) {
			return false;
		}
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
		return true;
	}
	
	public static String getAppClientType() {
		if (andriodAppDevice.get() == null) {
			return "";
		}
		return andriodAppDevice.get();
	}
	
	public static void setAppClient(HttpServletRequest request) {
		String appClient = request.getParameter("_appclient");
		if (appClient != null) {
			andriodAppDevice.set(appClient);
		} else {
			andriodAppDevice.set(null);
		}
	}
	
	private static OnlineUserChecker checker;
	
	public static void setOnlineUserChecker(OnlineUserChecker checker0) {
		checker = checker0;
	}
	
	public static boolean isOnlineUser(long userId) {
		return checker != null && checker.isOnline(userId);
	}

	public interface OnlineUserChecker {
		boolean isOnline(long userId);
	}
}
