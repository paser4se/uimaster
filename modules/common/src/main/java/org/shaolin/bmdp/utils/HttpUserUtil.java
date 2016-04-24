package org.shaolin.bmdp.utils;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

public class HttpUserUtil {

	private static final String IP_Service = "http://ip.taobao.com/service/getIpInfo.php";

	public static String getIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getRealLocationInfo(String ipAddress,
			String encodingString) throws Exception {
		return getResult(IP_Service, "ip=" + ipAddress, encodingString);
	}

	/**
	 * @param urlStr
	 * @param queryString
	 * @param encoding
	 *            GBK,UTF-8
	 * @return
	 * @throws Exception 
	 */
	private static String getResult(String urlStr, String queryString,
			String encoding) throws Exception {
		HttpSender sender = new HttpSender();
		try {
			return sender.get(urlStr + "?" + queryString);
		} finally {
			sender.shutdown();
		}
	}

}
