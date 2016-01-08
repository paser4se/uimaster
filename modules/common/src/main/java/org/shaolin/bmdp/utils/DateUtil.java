package org.shaolin.bmdp.utils;

import java.util.Date;

public class DateUtil {

	/**
	 * Calculate the count down.
	 * 
	 * @param d
	 * @return
	 */
	public static long calCountDown(Date d) {
		long gap = d.getTime() - System.currentTimeMillis();
		if (gap <= 0) {
			return 0;
		}
		return gap;
	}
	
}
