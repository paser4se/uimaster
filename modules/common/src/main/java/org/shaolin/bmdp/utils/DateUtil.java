/*
* Copyright 2015 The UIMaster Project
*
* The UIMaster Project licenses this file to you under the Apache License,
* version 2.0 (the "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at:
*
*   http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
* License for the specific language governing permissions and limitations
* under the License.
*/
package org.shaolin.bmdp.utils;

import java.util.Date;

public class DateUtil {

	public static long calCountDown(Date d) {
		long gap = d.getTime() - System.currentTimeMillis();
		if (gap <= 0) {
			return 0;
		}
		return gap;
	}
	
	public static void increase(Date d, long time) {
		d.setTime(d.getTime() + time);
	}
	
	public static void increaseHours(Date d, int hours) {
		d.setTime(d.getTime() + (1000 * 60 * 60 * hours));
	}
	
	public static void increaseOneHour(Date d) {
		d.setTime(d.getTime() + (1000 * 60 * 60));
	}
	
	public static void increaseHalfHour(Date d) {
		d.setTime(d.getTime() + (1000 * 60 * 30));
	}
	
	public static void increaseDays(Date d, int days) {
		d.setTime(d.getTime() + (1000 * 60 * 60 * days));
	}
	
	public static void increaseOneDay(Date d) {
		d.setTime(d.getTime() + (1000 * 60 * 60 * 24));
	}
	
	public static void increaseHalfDay(Date d) {
		d.setTime(d.getTime() + (1000 * 60 * 60 * 12));
	}
	
}
