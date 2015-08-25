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
package org.shaolin.uimaster.page.ajax;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Shaolin Wu
 */
public class Chart extends Widget implements Serializable {
	private static final long serialVersionUID = -1744731434666233557L;
	
	private static final Logger logger = LoggerFactory.getLogger(Chart.class);
	
	private final Class type;
	
	public Chart(String id, Layout layout, Class type) {
		super(id, layout);
		this.type = type;
	}

}
