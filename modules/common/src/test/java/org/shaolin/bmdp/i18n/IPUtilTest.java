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
package org.shaolin.bmdp.i18n;

import org.junit.Test;
import org.shaolin.bmdp.utils.HttpUserUtil;

public class IPUtilTest {

	@Test
	public void testGetRealAddress() throws Exception {
		String realInfo = HttpUserUtil.getRealLocationInfo("210.75.225.254", "UTF-8");
		System.out.println(realInfo);
		//{"code":0,"data":{"country":"\u4e2d\u56fd","country_id":"CN","area":"\u534e\u4e1c","area_id":"300000",
		//"region":"\u6d59\u6c5f\u7701","region_id":"330000","city":"\u676d\u5dde\u5e02","city_id":"330100","county":"","county_id":"-1","isp":"","isp_id":"-1","ip":"110.75.225.254"}}
	}	
}
