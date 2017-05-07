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

import java.io.IOException;
import java.io.Serializable;

import org.shaolin.uimaster.page.UserRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HTMLLayoutType extends HTMLWidgetType implements Serializable {
	
	private int tableRowCount;
	private int tableColumnCount;

	private static final Logger logger = LoggerFactory.getLogger(HTMLLayoutType.class);

	public HTMLLayoutType(String id) {
		super(id);
	}

	public void setTableRowCount(int tableRowCount) {
		this.tableRowCount = tableRowCount;
	}

	public void setTableColumnCount(int tableColumnCount) {
		this.tableColumnCount = tableColumnCount;
	}

	public int getTableRowCount() {
		return tableRowCount;
	}

	public int getTableColumnCount() {
		return tableColumnCount;
	}

	public int getY() {
		int row = 0;
		try {
			row = Integer.parseInt((String) getAttribute("y"));
		} catch (NumberFormatException e) {
			logger.error("error. in entity: " + UserRequestContext.UserContext.get().getFormName(), e);
		}
		return row;
	}

	public int getX() {
		return Integer.parseInt((String) getAttribute("x"));
	}

	public int getRowSpan() {
		int rowSpan = 1;
		if (getAttribute("rowSpan") != null) {
			try {
				rowSpan = Integer.parseInt((String) getAttribute("rowSpan"));
			} catch (NumberFormatException e) {
				logger.error("error. in entity: " + UserRequestContext.UserContext.get().getFormName(), e);
			}
		}
		return rowSpan;
	}

	public int getColSpan() {
		int colSpan = 1;
		if (getAttribute("colSpan") != null) {
			try {
				colSpan = Integer.parseInt((String) getAttribute("colSpan"));
			} catch (NumberFormatException e) {
				logger.error("error. in entity: " + UserRequestContext.UserContext.get().getFormName(), e);
			}
		}
		return colSpan;
	}

	public void generateAttribute(UserRequestContext context,
			String attributeName, Object attributeValue) throws IOException {
		super.generateAttribute(context, attributeName, attributeValue);
	}

}
