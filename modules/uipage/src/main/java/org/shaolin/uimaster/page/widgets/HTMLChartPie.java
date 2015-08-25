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
package org.shaolin.uimaster.page.widgets;

import java.util.List;

import org.shaolin.bmdp.datamodel.page.UITableColumnType;
import org.shaolin.uimaster.page.HTMLSnapshotContext;

public class HTMLChartPie extends HTMLChartSuper {
	private static final long serialVersionUID = -5232602952223828765L;

	public HTMLChartPie() {
	}

	public HTMLChartPie(HTMLSnapshotContext context) {
		super(context);
	}

	public HTMLChartPie(HTMLSnapshotContext context, String id) {
		super(context, id);
	}

	@Override
	public void generateData(List<UITableColumnType> columns,
			HTMLSnapshotContext context, int depth) throws Exception {
		List<Integer> listData = (List<Integer>)this.removeAttribute("query");
		if (!listData.isEmpty()) {
			StringBuffer sb = new StringBuffer("[");
			for (Integer v : listData) {
				sb.append("{").append("value: ").append(v).append("},");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("]");
			context.generateHTML(sb.toString());
		}
	}
}
