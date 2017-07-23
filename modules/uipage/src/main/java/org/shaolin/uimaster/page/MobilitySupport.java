package org.shaolin.uimaster.page;

public class MobilitySupport {

	public static final String MOB_PAGE_SUFFIX = "_mob";
	
	public static boolean isMobileRequest(String userAgent) {
		userAgent = userAgent.toLowerCase();
		if (userAgent.indexOf("okhttp") != -1
		 || userAgent.indexOf("android") != -1
		 || userAgent.indexOf("webos") != -1
		 || userAgent.indexOf("iphone") != -1
		 || userAgent.indexOf("ipad") != -1
		 || userAgent.indexOf("ipod") != -1
		 || userAgent.indexOf("blackberry") != -1
		 || userAgent.indexOf("opera mobi") != -1
		 || userAgent.indexOf("opera mini") != -1
		 || userAgent.indexOf("symbian") != -1
		 || userAgent.indexOf("windows phone") != -1) {
			return true;
		}
		return false;
	}
	
}
