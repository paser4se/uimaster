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

import java.util.ArrayList;
import java.util.List;

import org.shaolin.bmdp.datamodel.page.ExpressionPropertyType;
import org.shaolin.bmdp.datamodel.page.UITableColumnType;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.context.OOEEContextFactory;
import org.shaolin.uimaster.page.HTMLSnapshotContext;
import org.shaolin.uimaster.page.javacc.UIVariableUtil;
import org.shaolin.uimaster.page.od.ODContext;

public class HTMLChartBarType extends HTMLChartSuper {

	private static final long serialVersionUID = -5232602952223828765L;

	public HTMLChartBarType() {
	}

	public HTMLChartBarType(HTMLSnapshotContext context) {
		super(context);
	}

	public HTMLChartBarType(HTMLSnapshotContext context, String id) {
		super(context, id);
	}

	@Override
	public void generateData(List<UITableColumnType> columns,
			HTMLSnapshotContext context, int depth) throws Exception {
		List<String> cssStyles = new ArrayList<String>();
		for (UITableColumnType col: columns) {
			cssStyles.add("label: '" + UIVariableUtil.getI18NProperty(col.getTitle()) + "'," + col.getCssStype());
		}
		
		List<Object> listData = (List<Object>)this.removeAttribute("query");
		if (!listData.isEmpty() && listData.size() > 0) {
			context.generateHTML("datasets: [");
			StringBuilder sb = new StringBuilder();
			// vertical iterator.
			OOEEContext ooeeContext = OOEEContextFactory.createOOEEContext();
			DefaultEvaluationContext evaContext = new DefaultEvaluationContext();
			ooeeContext.setDefaultEvaluationContext(evaContext);
			ooeeContext.setEvaluationContextObject(ODContext.LOCAL_TAG, evaContext);
			for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
				ExpressionPropertyType isVisibleExpr = columns.get(columnIndex).getIsVisible();
				if (isVisibleExpr != null) {
					// it does require the data from the first row.
					evaContext.setVariableValue("rowBE", listData.get(0));
					Boolean isVisible = (Boolean)isVisibleExpr.getExpression().evaluate(ooeeContext);
					if (!isVisible.booleanValue()) {
						continue;
					}
				}
				sb.append("{");
				String css = cssStyles.get(columnIndex);
				if (css != null) {
					sb.append(css).append(",");
				}
				sb.append(" data:[");
				for (int i = 0; i < listData.size(); i++) {
					evaContext.setVariableValue("rowBE", listData.get(i));
					Object value = columns.get(columnIndex).getRowExpression().getExpression().evaluate(
							ooeeContext);
					sb.append(value).append(",");
				}
				sb.deleteCharAt(sb.length()-1);
				sb.append("]},");
			}
			sb.deleteCharAt(sb.length()-1);
			context.generateHTML(sb.toString());
			context.generateHTML("]");
		}
	}

}
