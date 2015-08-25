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

import org.shaolin.uimaster.page.HTMLSnapshotContext;
import org.shaolin.uimaster.page.ajax.Chart;
import org.shaolin.uimaster.page.ajax.Layout;
import org.shaolin.uimaster.page.ajax.Widget;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLChartDoughnut extends HTMLChartSuper {
	
	private static final long serialVersionUID = -5232602952223828765L;
	private static Logger logger = LoggerFactory.getLogger(HTMLChartDoughnut.class);

	public HTMLChartDoughnut() {
	}

	public HTMLChartDoughnut(HTMLSnapshotContext context) {
		super(context);
	}

	public HTMLChartDoughnut(HTMLSnapshotContext context, String id) {
		super(context, id);
	}

	@Override
	public void generateBeginHTML(HTMLSnapshotContext context,
			UIFormObject ownerEntity, int depth) {

	}

	@Override
	public void generateEndHTML(HTMLSnapshotContext context,
			UIFormObject ownerEntity, int depth) {
	}

	public Widget createAjaxWidget(VariableEvaluator ee) {
		Chart chart = new Chart(getName(), Layout.NULL, HTMLChartDoughnut.class);

		chart.setReadOnly(getReadOnly());
		chart.setUIEntityName(getUIEntityName());

		chart.setListened(true);
		chart.setFrameInfo(getFrameInfo());

		return chart;
	}

}
