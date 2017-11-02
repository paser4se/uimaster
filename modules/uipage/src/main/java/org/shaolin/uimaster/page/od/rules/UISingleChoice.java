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
package org.shaolin.uimaster.page.od.rules;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.ajax.SingleChoice;
import org.shaolin.uimaster.page.exception.UIConvertException;
import org.shaolin.uimaster.page.od.IODMappingConverter;
import org.shaolin.uimaster.page.widgets.HTMLChoiceType;
import org.shaolin.uimaster.page.widgets.HTMLSingleChoiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UISingleChoice implements IODMappingConverter {
	private HTMLSingleChoiceType uisingleChoice;
	private String uiid;
	private Object value;
	private Class realDataType = String.class;
	private List<String> optionValues;
	private List<String> optionDisplayValues;

	public static UISingleChoice createRule() {
		return new UISingleChoice();
	}
	
	public String getRuleName() {
		return this.getClass().getName();
	}

	public HTMLSingleChoiceType getUISingleChoice() {
		return this.uisingleChoice;
	}

	public void setUISingleChoice(HTMLSingleChoiceType UISingleChoice) {
		this.uisingleChoice = UISingleChoice;
	}

	private HTMLSingleChoiceType getUIHTML() {
		return this.uisingleChoice;
	}

	public String getValue() {
		if (this.value != null) {
			return this.value.toString();
		}
		return null;
	}

	public void setValue(Object Value) {
		this.value = Value;
	}

	public List<String> getOptionValues() {
		return this.optionValues;
	}

	public void setOptionValues(List<String> OptionValues) {
		this.optionValues = OptionValues;
	}

	public List<String> getOptionDisplayValues() {
		return this.optionDisplayValues;
	}

	public void setOptionDisplayValues(List<String> OptionDisplayValues) {
		this.optionDisplayValues = OptionDisplayValues;
	}

	public Map<String, Class<?>> getDataEntityClassInfo() {
		HashMap<String, Class<?>> dataClassInfo = new LinkedHashMap<String, Class<?>>();

		dataClassInfo.put("isNumber", Boolean.class);
		dataClassInfo.put("Value", String.class);
		dataClassInfo.put("LongValue", Long.class);
		dataClassInfo.put("IntValue", Long.class);
		dataClassInfo.put("OptionValues", List.class);
		dataClassInfo.put("OptionDisplayValues", List.class);

		return dataClassInfo;
	}

	public Map<String, Class<?>> getUIEntityClassInfo() {
		HashMap<String, Class<?>> uiClassInfo = new HashMap<String, Class<?>>();

		uiClassInfo.put(UI_WIDGET_TYPE, HTMLSingleChoiceType.class);

		return uiClassInfo;
	}
	
	public static Map<String, String> getRequiredUIParameter(String param) {
		HashMap<String, String> dataClassInfo = new LinkedHashMap<String, String>();

		dataClassInfo.put(UI_WIDGET_TYPE, param);

		return dataClassInfo;
	}
	
	public static Map<String, String> getRequiredDataParameters(String value, String optionValues) {
		HashMap<String, String> dataClassInfo = new LinkedHashMap<String, String>();

		dataClassInfo.put("OptionValues", optionValues);
		
		return dataClassInfo;
	}

	public void setInputData(Map<String, Object> paramValue)
			throws UIConvertException {
		try {
			if (paramValue.containsKey(UI_WIDGET_TYPE)) {
				this.uisingleChoice = ((HTMLSingleChoiceType) paramValue
						.get(UI_WIDGET_TYPE));
			}
			if (paramValue.containsKey(UI_WIDGET_ID)) {
				this.uiid = (String) paramValue.get(UI_WIDGET_ID);
			}
			if (paramValue.containsKey("Value")) {
				if (paramValue.get("Value") != null) {
					this.realDataType = paramValue.get("Value").getClass();
					this.value = paramValue.get("Value");
				}
			} else if (paramValue.containsKey("IntValue")) {
				if (paramValue.get("IntValue") != null) {
					this.realDataType = Integer.class;
					this.value = paramValue.get("IntValue");
				}
			} else if (paramValue.containsKey("LongValue")) {
				if (paramValue.get("LongValue") != null) {
					this.realDataType = Long.class;
					this.value = paramValue.get("LongValue");
				}
			}
			if (paramValue.containsKey("OptionValues")) {
				this.optionValues = ((List) paramValue.get("OptionValues"));
			}
			if (paramValue.containsKey("OptionDisplayValues")) {
				this.optionDisplayValues = ((List) paramValue.get("OptionDisplayValues"));
			}
		} catch (Throwable t) {
			if (t instanceof UIConvertException) {
				throw ((UIConvertException) t);
			}
			if (getUIHTML() == null) {
				throw new UIConvertException("EBOS_ODMAPPER_070", t,
						new Object[] { "null"});
			}
			throw new UIConvertException("EBOS_ODMAPPER_070", t,
					new Object[] { getUIHTML().getUIID() });
		}
	}

	public Map<String, Object> getOutputData() throws UIConvertException {
		Map<String, Object> paramValue = new HashMap<String, Object>();
		try {
			paramValue.put(UI_WIDGET_TYPE, this.uisingleChoice);
			paramValue.put("Value", this.value);
			if (this.value != null && this.value instanceof Integer) {
				paramValue.put("IntValue", (Integer)this.value);
			}
			if (this.value != null && this.value instanceof Long) {
				paramValue.put("LongValue", (Long)this.value);
			}
			paramValue.put("OptionValues", this.optionValues);
			paramValue.put("OptionDisplayValues", this.optionDisplayValues);
		} catch (Throwable t) {
			if (t instanceof UIConvertException) {
				throw ((UIConvertException) t);
			}

			throw new UIConvertException("EBOS_ODMAPPER_071", t,
					new Object[] { getUIHTML().getUIID() });
		}

		return paramValue;
	}

	public String[] getImplementInterfaceName() {
		return new String[0];
	}

	public void pushDataToWidget(UserRequestContext htmlContext) throws UIConvertException {
		try {
			if (this.value != null) {
				this.uisingleChoice.setValue(this.value.toString());
				this.uisingleChoice.setRealValueDataType(this.realDataType);
			}
			
			callChoiceOption(true, htmlContext);
		} catch (Throwable t) {
			if (t instanceof UIConvertException) {
				throw ((UIConvertException) t);
			}
			throw new UIConvertException("EBOS_ODMAPPER_072", t,
					new Object[] { getUIHTML().getUIID() });
		}
	}

	public void pullDataFromWidget(UserRequestContext htmlContext) throws UIConvertException {
		try {
//			SingleChoice singleChoice = (SingleChoice) AjaxActionHelper
//					.getCachedAjaxWidget(this.uiid, htmlContext);
			JSONObject selectComp = htmlContext.getAjaxWidget(this.uiid);
			if (selectComp == null) {
				logger.warn(this.uiid + " does not exist for data to ui mapping!");
				return;
			}
			JSONObject attrMap = selectComp.getJSONObject("attrMap");
			if (attrMap.has("realVType")) {
				this.value = SingleChoice.getRealValue(attrMap, attrMap.getString("realVType"));
			} else {
				this.value = SingleChoice.getRealValue(attrMap, String.class.getName());
			}
			if (this.value != null && "null".equals(this.value)) {
				this.value = null;
			}
		} catch (Throwable t) {
			if (t instanceof UIConvertException) {
				throw ((UIConvertException) t);
			}
			throw new UIConvertException("EBOS_ODMAPPER_073", t,
					new Object[] { this.uiid });
		}
	}

	public void callChoiceOption(boolean isDataToUI, UserRequestContext htmlContext) throws UIConvertException {
		try {
			Map<String, Object> converter_in_data = new HashMap<String, Object>();
			converter_in_data.put(UI_WIDGET_TYPE, this.uisingleChoice);
			converter_in_data.put("OptionValues", this.optionValues);
			converter_in_data.put("OptionDisplayValues", this.optionDisplayValues);

			IODMappingConverter converter = new UIChoiceOptionValue();
			converter.setInputData(converter_in_data);
			if (isDataToUI) {
				converter.pushDataToWidget(htmlContext);
			} else {
				converter.pullDataFromWidget(htmlContext);

				Map<String, Object> converter_out_data = converter
						.getOutputData();

				HTMLChoiceType ref_UIChoice = null;
				List ref_OptionValues = null;
				List ref_OptionDisplayValues = null;

				if (converter_out_data.containsKey(UI_WIDGET_TYPE)) {
					ref_UIChoice = (HTMLChoiceType) converter_out_data
							.get(UI_WIDGET_TYPE);
				}
				if (converter_out_data.containsKey("OptionValues")) {
					ref_OptionValues = (List) converter_out_data
							.get("OptionValues");
				}
				if (converter_out_data.containsKey("OptionDisplayValues")) {
					ref_OptionDisplayValues = (List) converter_out_data
							.get("OptionDisplayValues");
				}
				this.optionValues = ref_OptionValues;
				this.optionDisplayValues = ref_OptionDisplayValues;
			}

		} catch (Throwable t) {
			if (t instanceof UIConvertException) {
				throw ((UIConvertException) t);
			}
			throw new UIConvertException("EBOS_ODMAPPER_074", t,
					new Object[] { getUIHTML().getUIID() });
		}
	}
	
	private static final Logger logger = LoggerFactory.getLogger(UIText.class);
}