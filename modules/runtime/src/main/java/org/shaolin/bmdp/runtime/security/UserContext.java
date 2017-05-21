package org.shaolin.bmdp.runtime.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
	
	String requestIP;
	
	String cityId;
	
	String lastLoginDate;
	
	boolean isAdmin;
	
	List<IConstantEntity> userRoles;
	
	long orgId;
	
	String orgCode;
	
	String orgType;
	
	String orgName;
	
	protected double longitude;
    
    protected double latitude;
	
	boolean verified;
	
	private String pullAction = "new";//mobile pull. we have new or history actions.
	private long pullId;
	
	private HashMap<String, Object> userData = new HashMap<String, Object>();
	
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

	public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getLongitude() {
    	return longitude;
    }
    
    public double getLatitude() {
    	return latitude;
    }
	
	public String getUserLocation() {
		return userLocation;
	}

	/**
	 * {
	 *  "code":0,"data":
	 *   {
	 *    "country":"\\u4e2d\\u56fd","country_id":"CN","area":"\\u534e\\u4e1c","area_id":"300000",
	 *    "region":"\\u4e0a\\u6d77\\u5e02","region_id":"310000","city":"\\u4e0a\\u6d77\\u5e02","city_id":"310100",
	 *    "county":"","county_id":"-1","isp":"\\u9e4f\\u535a\\u58eb","isp_id":"1000143","ip":"49.221.251.198"
	 *   }
	 * }
	 * 
	 * @param userLocation
	 */
	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}
	
	public void setUserRequestIP(String requestIP) {
		this.requestIP = requestIP;
	}
	
	public String getRequestIP() {
		return this.requestIP;
	}
	
	public void setCity(String cityId) {
		this.cityId = cityId;
	}
	
	public String getCity() {
		return cityId;
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

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
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
		if (userRoles != null) {
			userRolesCache.set(Collections.unmodifiableList(userRoles));
		}
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

	public static void addUserData(String key, Object value) {
		getUserContext().userData.put(key, value);
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
		
		return getUserContext().userData.get(key);
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
		
		Object value = getUserContext().userData.get(key);
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
	
	public static List<String> getUserRoleValues() {
		if (userRolesCache.get() != null) {
			ArrayList<String> result = new ArrayList<String>();
			List<IConstantEntity> items = userRolesCache.get();
			for (IConstantEntity entity : items) {
				result.add(entity.getEntityName() + "," + entity.getIntValue());
			} 
			return result;
		}
		return Collections.emptyList();
	}
	
	public static boolean hasRole(String role) {
		if (userRolesCache.get() == null || role == null) {
			return false;
		}
		String[] ceList = role.split(";");
		for (String ce: ceList) {
			for (IConstantEntity exist : userRolesCache.get()) {
				if (ce.equals(CEUtil.getValue(exist))) {
					return true;
				}
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
	
	public boolean isPullNew() {
		return pullAction != null && "new".equals(pullAction);
	}

	public boolean isPullHistory() {
		return pullAction != null && "history".equals(pullAction);
	}
	
	public boolean isPullRefresh() {
		return pullAction != null && "filter".equals(pullAction);
	}
	
	public void setPullAction(String pullAction) {
		this.pullAction = pullAction;
	}
	
	public long getPullId() {
		return pullId;
	}

	public void setPullId(long pullId) {
		this.pullId = pullId;
	}
}
