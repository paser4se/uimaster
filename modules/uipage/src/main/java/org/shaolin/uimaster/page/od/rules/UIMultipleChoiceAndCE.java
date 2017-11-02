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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.ce.CEUtil;
import org.shaolin.bmdp.runtime.ce.IConstantEntity;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.exception.UIConvertException;
import org.shaolin.uimaster.page.od.IODMappingConverter;
import org.shaolin.uimaster.page.widgets.HTMLMultiChoiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIMultipleChoiceAndCE implements IODMappingConverter {
	private HTMLMultiChoiceType uiMultipleChoice;
	private String uiid;
	private List<IConstantEntity> ceValues;
	private String ceType;
	private Integer expendlevels = 0; // expending hierarchy levels of a constant object.
	private boolean containsNotSpecified = true;
	private List excludeValue;
	private String notSpecifiedDisplayValue;

	public static UIMultipleChoiceAndCE createRule() {
		return new UIMultipleChoiceAndCE();
	}
	
	public UIMultipleChoiceAndCE() {
	}

	public String getRuleName() {
		return this.getClass().getName();
	}

	public HTMLMultiChoiceType getUIMultipleChoice() {
		return this.uiMultipleChoice;
	}

	public void setUIMultipleChoice(HTMLMultiChoiceType UIMultipleChoice) {
		this.uiMultipleChoice = UIMultipleChoice;
	}

	private HTMLMultiChoiceType getUIHTML() {
		return this.uiMultipleChoice;
	}

	public List<IConstantEntity> getCEValues() {
		return this.ceValues;
	}

	public void setCEValues(List<IConstantEntity> CEValues) {
		this.ceValues = CEValues;
	}

	public String getCEType() {
		return this.ceType;
	}

	public void setCEType(String CEType) {
		this.ceType = CEType;
	}

	public Integer getExpendlevels() {
		return expendlevels;
	}

	public void setExpendlevels(Integer expendlevels) {
		this.expendlevels = expendlevels;
	}
	
	public boolean getContainsNotSpecified() {
		return this.containsNotSpecified;
	}

	public void setContainsNotSpecified(boolean ContainsNotSpecified) {
		this.containsNotSpecified = ContainsNotSpecified;
	}

	public List getExcludeValue() {
		return this.excludeValue;
	}

	public void setExcludeValue(List excludeValue) {
		this.excludeValue = excludeValue;
	}

	public String getNotSpecifiedDisplayValue() {
		return this.notSpecifiedDisplayValue;
	}

	public void setNotSpecifiedDisplayValue(String NotSpecifiedDisplayValue) {
		this.notSpecifiedDisplayValue = NotSpecifiedDisplayValue;
	}

	public Map<String, Class<?>> getDataEntityClassInfo() {
		HashMap<String, Class<?>> dataClassInfo = new LinkedHashMap<String, Class<?>>();

		dataClassInfo.put("CEValues", List.class);
		dataClassInfo.put("CEType", String.class);
		dataClassInfo.put("Expendlevels", Integer.class);
		dataClassInfo.put("ContainsNotSpecified", Boolean.TYPE);
		dataClassInfo.put("ContainsUnknown", Boolean.TYPE);
		dataClassInfo.put("ExcludeValue", List.class);
		dataClassInfo.put("NotSpecifiedDisplayValue", String.class);

		return dataClassInfo;
	}

	public Map<String, Class<?>> getUIEntityClassInfo() {
		HashMap<String, Class<?>> uiClassInfo = new HashMap<String, Class<?>>();

		uiClassInfo.put(UI_WIDGET_TYPE, HTMLMultiChoiceType.class);

		return uiClassInfo;
	}
	
	public static Map<String, String> getRequiredUIParameter(String param) {
		HashMap<String, String> dataClassInfo = new LinkedHashMap<String, String>();

		dataClassInfo.put(UI_WIDGET_TYPE, param);

		return dataClassInfo;
	}
	
	public static Map<String, String> getRequiredDataParameters(String value, String ceType) {
		HashMap<String, String> dataClassInfo = new LinkedHashMap<String, String>();

		dataClassInfo.put("CEValues", value);
		dataClassInfo.put("CEType", ceType);
		
		return dataClassInfo;
	}

	public void setInputData(Map<String, Object> paramValue)
			throws UIConvertException {
		try {
			if (paramValue.containsKey(UI_WIDGET_TYPE)) {
				this.uiMultipleChoice = ((HTMLMultiChoiceType) paramValue
						.get(UI_WIDGET_TYPE));
			}
			if (paramValue.containsKey(UI_WIDGET_ID)) {
				this.uiid = (String) paramValue.get(UI_WIDGET_ID);
			}
			if (paramValue.containsKey("ExcludeValue")) {
				this.excludeValue = ((List) paramValue.get("ExcludeValue"));
			}
			if (paramValue.containsKey("CEType")) {
				this.ceType = ((String) paramValue.get("CEType"));
			}
			if (paramValue.get("CEValues") != null) {
				this.ceValues = ((List) paramValue.get("CEValues"));
			}
			if (paramValue.containsKey("Expendlevels")) {
				this.expendlevels = ((Integer) paramValue.get("Expendlevels"));
			}
			if (paramValue.containsKey("ContainsNotSpecified")) {
				this.containsNotSpecified = ((Boolean) paramValue
						.get("ContainsNotSpecified")).booleanValue();
			}
			if (paramValue.containsKey("NotSpecifiedDisplayValue")) {
				this.notSpecifiedDisplayValue = ((String) paramValue
						.get("NotSpecifiedDisplayValue"));
			}
		} catch (Throwable t) {
			if (t instanceof UIConvertException) {
				throw ((UIConvertException) t);
			}
			throw new UIConvertException("EBOS_ODMAPPER_070", t,
					new Object[] { getUIHTML().getUIID() });
		}
	}

	public Map<String, Object> getOutputData() throws UIConvertException {
		Map<String, Object> paramValue = new HashMap<String, Object>();
		try {
			paramValue.put(UI_WIDGET_TYPE, this.uiMultipleChoice);
			paramValue.put("ExcludeValue", this.excludeValue);
			paramValue.put("CEType", this.ceType);
			paramValue.put("CEValues", this.ceValues);
			paramValue.put("ContainsNotSpecified", Boolean.valueOf(this.containsNotSpecified));
			paramValue.put("NotSpecifiedDisplayValue", this.notSpecifiedDisplayValue);
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
			this.uiMultipleChoice.setCeName(this.ceType);
			if (this.expendlevels < 1) {
				if (this.ceValues != null) {
					List<String> values = new ArrayList<String>();
					for (IConstantEntity item : this.ceValues) {
						values.add(item.getValue());
					}
					this.uiMultipleChoice.setValues(values);
				}
				callChoiceOptionWithCE(true, htmlContext);
			} else {
				if (this.ceValues != null) {
					List<String> values = new ArrayList<String>();
					for (IConstantEntity item : this.ceValues) {
						values.add(item.getEntityName() +","+ item.getValue());
					}
					this.uiMultipleChoice.setValues(values);
				}
				List<String> optionValues = new ArrayList<String>();
				List<String> optionDisplayValues = new ArrayList<String>();
				CEUtil.getCEItems(this.expendlevels, optionValues, optionDisplayValues, 
						AppContext.get().getConstantService().getConstantEntity(this.ceType));
				callChoiceOption(true, htmlContext, optionValues, optionDisplayValues);
			}
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
//			MultiChoice selectComp = (MultiChoice) AjaxActionHelper
//					.getCachedAjaxWidget(this.uiid, htmlContext);
			JSONObject selectComp = htmlContext.getAjaxWidget(this.uiid);
			if (selectComp == null) {
				logger.warn(this.uiid + " does not exist for data to ui mapping!");
				return;
			}
			JSONObject attrMap = selectComp.getJSONObject("attrMap");
			this.ceValues = CEUtil.getConstantEntities(this.ceType,
					(List<String>)attrMap.get("values"));
		} catch (Throwable t) {
			if (t instanceof UIConvertException) {
				throw ((UIConvertException) t);
			}
			throw new UIConvertException("EBOS_ODMAPPER_073", t,
					new Object[] { this.uiid });
		}
	}

	private void callChoiceOptionWithCE(boolean isDataToUI, UserRequestContext htmlContext) throws UIConvertException {
		try {
			Map<String, Object> converter_in_data = new HashMap<String, Object>();

			converter_in_data.put(UI_WIDGET_TYPE, this.uiMultipleChoice);
			converter_in_data.put("CEType", this.ceType);
			converter_in_data.put("ContainsNotSpecified",
					Boolean.valueOf(this.containsNotSpecified));
			converter_in_data.put("ExcludeValue", this.excludeValue);
			converter_in_data.put("NotSpecifiedDisplayValue",
					this.notSpecifiedDisplayValue);

			IODMappingConverter converter = new UIChoiceOptionValueAndCE();
			converter.setInputData(converter_in_data);
			if (isDataToUI) {
				converter.pushDataToWidget(htmlContext);
			} else {
				converter.pullDataFromWidget(htmlContext);

				Map output = converter.getOutputData();

				String ref_CEType = null;
				List ref_excludeValue = null;
				boolean ref_ContainsNotSpecified = false;
				String ref_NotSpecifiedDisplayValue = null;

				if (output.containsKey("CEType")) {
					ref_CEType = (String) output.get("CEType");
				}
				if (output.containsKey("excludeValue")) {
					ref_excludeValue = (List) output
							.get("excludeValue");
				}
				if (output.containsKey("ContainsNotSpecified")) {
					ref_ContainsNotSpecified = ((Boolean) output
							.get("ContainsNotSpecified")).booleanValue();
				}
				if (output.containsKey("NotSpecifiedDisplayValue")) {
					ref_NotSpecifiedDisplayValue = (String) output
							.get("NotSpecifiedDisplayValue");
				}

				this.ceType = ref_CEType;
				this.containsNotSpecified = ref_ContainsNotSpecified;
				this.excludeValue = ref_excludeValue;
				this.notSpecifiedDisplayValue = ref_NotSpecifiedDisplayValue;
			}
		} catch (Throwable t) {
			if (t instanceof UIConvertException) {
				throw ((UIConvertException) t);
			}
			throw new UIConvertException("EBOS_ODMAPPER_074", t,
					new Object[] { getUIHTML().getUIID() });
		}
	}
	
	private void callChoiceOption(boolean isDataToUI, UserRequestContext htmlContext, 
			List<String> optionValues, List<String> optionDisplayValues) throws UIConvertException {
		try {
			Map<String, Object> converter_in_data = new HashMap<String, Object>();
			converter_in_data.put(UI_WIDGET_TYPE, this.uiMultipleChoice);
			converter_in_data.put("OptionValues", optionValues);
			converter_in_data.put("OptionDisplayValues", optionDisplayValues);

			IODMappingConverter converter = new UIChoiceOptionValue();
			converter.setInputData(converter_in_data);
			if (isDataToUI) {
				converter.pushDataToWidget(htmlContext);
			} else {
				converter.pullDataFromWidget(htmlContext);

				List<String> ref_OptionValues = null;
				List<String> ref_OptionDisplayValues = null;
				Map output = converter.getOutputData();
				if (output.containsKey("OptionValues")) {
					ref_OptionValues = (List) output
							.get("OptionValues");
				}
				if (output.containsKey("OptionDisplayValues")) {
					ref_OptionDisplayValues = (List) output
							.get("OptionDisplayValues");
				}
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