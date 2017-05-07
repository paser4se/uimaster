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
package org.shaolin.uimaster.page.od.mappings;

import org.shaolin.bmdp.datamodel.page.DirectComponentMappingType;
import org.shaolin.javacc.context.OOEEContext;
import org.shaolin.javacc.exception.ParsingException;
import org.shaolin.uimaster.page.cache.ODObject;
import org.shaolin.uimaster.page.exception.ODException;
import org.shaolin.uimaster.page.od.ODContext;

public class DirectComponentMapping extends ComponentMapping {

	public DirectComponentMapping(DirectComponentMappingType type) {
		super(type);
		throw new UnsupportedOperationException("");
	}

	@Override
	public void parse(OOEEContext context, ODObject odObject)
			throws ParsingException {
	}

	@Override
	public void execute(ODContext odContext) throws ODException {
	}

	public void execute(ODContext odContext, boolean isSingleColumnMapping,
			String columnName) throws ODException {
	}

	public void convertDataToUI(ODContext odContext) throws ODException {
	}

	public void convertUIToData(ODContext odContext) {
	}

}