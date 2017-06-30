package org.shaolin.uimaster.page.javacc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.shaolin.bmdp.datamodel.common.VariableCategoryType;
import org.shaolin.bmdp.datamodel.common.VariableType;
import org.shaolin.bmdp.datamodel.page.ExpressionPropertyType;
import org.shaolin.bmdp.datamodel.page.PropertyValueType;
import org.shaolin.bmdp.datamodel.page.ResourceBundlePropertyType;
import org.shaolin.bmdp.datamodel.page.StringPropertyType;
import org.shaolin.bmdp.i18n.ExceptionConstants;
import org.shaolin.bmdp.i18n.LocaleContext;
import org.shaolin.bmdp.i18n.ResourceUtil;
import org.shaolin.bmdp.json.JSONArray;
import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.persistence.BEEntityDaoObject;
import org.shaolin.bmdp.runtime.be.BEUtil;
import org.shaolin.bmdp.runtime.be.IBusinessEntity;
import org.shaolin.bmdp.runtime.ce.CEUtil;
import org.shaolin.bmdp.runtime.ce.IConstantEntity;
import org.shaolin.bmdp.runtime.entity.EntityNotFoundException;
import org.shaolin.javacc.context.DefaultEvaluationContext;
import org.shaolin.javacc.context.DefaultParsingContext;
import org.shaolin.uimaster.page.widgets.HTMLReferenceEntityType;

public final class UIVariableUtil {
	
	public static Object createObject(String entityName, VariableCategoryType categoryType)
			throws EntityNotFoundException {
		if (VariableCategoryType.BUSINESS_ENTITY == categoryType) {
			return BEUtil.createBEObject(entityName);
		} else if (VariableCategoryType.CONSTANT_ENTITY == categoryType) {
			return CEUtil.getConstantEntity(entityName);
		//} else if (VariableCategoryType.UI_ENTITY == categoryType) {
			//return new HTMLReferenceEntityType(entityName);
		} else if (VariableCategoryType.JAVA_CLASS == categoryType 
				|| VariableCategoryType.JAVA_PRIMITIVE == categoryType) {
			try {
				Class<?> objectClass = Class.forName(entityName);
				if (objectClass.isInterface()) {
					return null;
				}

				if (objectClass == boolean.class) {
					return Boolean.FALSE;
				} else if (objectClass == byte.class) {
					return new Byte((byte) 0);
				} else if (objectClass == short.class) {
					return new Short((short) 0);
				} else if (objectClass == int.class) {
					return new Integer(0);
				} else if (objectClass == long.class) {
					return new Long(0);
				} else if (objectClass == float.class) {
					return new Float(0);
				} else if (objectClass == double.class) {
					return new Double(0);
				} else if (objectClass == char.class) {
					return new Character((char) 0);
				} else {
					return objectClass.newInstance();
				}
			} catch (Exception ex) {
				throw new EntityNotFoundException(
						ExceptionConstants.UIMASTER_COMMON_003, ex,
						new Object[] { entityName });
			}
		} else {
			throw new EntityNotFoundException(
					ExceptionConstants.UIMASTER_COMMON_008, new Object[] { categoryType.value(),
							entityName });
		}
	}

	public static String getVariableClassName(VariableType variable)
			throws EntityNotFoundException {
		VariableCategoryType categoryType = variable.getCategory();
		String variableType = variable.getType().getEntityName();

		String variableClassName = null;

		if (categoryType == null) {
			categoryType = VariableCategoryType.JAVA_CLASS;
		}
		if (categoryType == VariableCategoryType.BUSINESS_ENTITY) {
//			String packageName = variableType.substring(0, variableType.length() - variableType.lastIndexOf('.') - 1);
//			String name = variableType.substring(variableType.length() - variableType.lastIndexOf('.'));
			variableClassName = variableType + "Impl";
		} else if (categoryType == VariableCategoryType.CONSTANT_ENTITY) {
			variableClassName = variableType;
		} else if (categoryType == VariableCategoryType.UI_ENTITY) {
			variableClassName = HTMLReferenceEntityType.class.getName();
		} else if (categoryType == VariableCategoryType.JAVA_CLASS
				|| VariableCategoryType.JAVA_PRIMITIVE == categoryType) {
			variableClassName = variableType;
		} 

		return variableClassName;
	}
	
	public static String getI18NProperty(PropertyValueType pvalue) {
		if (pvalue instanceof StringPropertyType) {
			return ((StringPropertyType) pvalue).getValue();
		} else if (pvalue instanceof ResourceBundlePropertyType) {
			String bundle = ((ResourceBundlePropertyType) pvalue).getBundle();
			String key = ((ResourceBundlePropertyType) pvalue).getKey();
			return ResourceUtil.getResource(LocaleContext.getUserLocale(), bundle, key);
		} else if (pvalue instanceof ExpressionPropertyType) {
			try {
				ExpressionPropertyType expr = (ExpressionPropertyType) pvalue;
				if (expr != null && expr.getExpression() != null) {
					if (!expr.getExpression().isParsed()) {
						expr.getExpression().parse(new DefaultParsingContext());
					} 
					return expr.getExpression().evaluate(new DefaultEvaluationContext()).toString();
				}
			} catch (Exception e) {
				Logger.getLogger(UIVariableUtil.class).warn(e.getMessage(), e);
			}
		}
		return "";
	}

	public static HashMap<String, Object> convertJsonToVar(JSONObject json) throws JSONException {
    	HashMap<String, Object> inputParams = new HashMap<String, Object>(json.length());
		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String k = keys.next();
			if (json.has(k+"_t")) {
				String type = json.getString(k+"_t");
				if ("be".equals(type)) {
					if (json.has(k+"_c")) {
						try {
							inputParams.put(k, BEEntityDaoObject.DAOOBJECT.get(json.getLong(k), Class.forName(json.getString(k+"_c"))));
						} catch (ClassNotFoundException e) {
							throw new JSONException(e);
						}
					} else {
						inputParams.put(k, json.getBEntity(k));
					}
				} else if ("ce".equals(type)) {
					inputParams.put(k, json.getCEntity(k));
				} else if ("map".equals(type)) {
					inputParams.put(k, json.getJSONObject(k));
				} else if ("list".equals(type)) {
					inputParams.put(k, json.getJSONArray(k));
				} else {
					inputParams.put(k, json.get(k));
				}
			} else {
				inputParams.put(k, json.get(k));
			}
		}
		return inputParams;
    }
    
    public static JSONObject convertVarToJson(HashMap<String, Object> inputParams) throws JSONException {
    	JSONObject json = new JSONObject();
		for (Entry<String, Object> var : inputParams.entrySet()) {
			if (var.getValue() == null) {
				continue;
			}
			if (var.getValue() instanceof IBusinessEntity) {
				IBusinessEntity be = (IBusinessEntity)var.getValue();
				if (be.getId() > 0) {
					// only save key.
					json.put(var.getKey(), be.getId());
					json.put(var.getKey()+"_c", var.getValue().getClass().getName());
				} else {
					// save the whole if it's not persistent.
					json.put(var.getKey(), be);
				}
				json.put(var.getKey()+"_t", "be");
			} else if (var.getValue() instanceof IConstantEntity) {
				json.put(var.getKey(), (IConstantEntity)var.getValue());
				json.put(var.getKey()+"_t", "ce");
			} else if (var.getValue() instanceof Map) {
				json.put(var.getKey(), (Map)var.getValue());
				json.put(var.getKey()+"_t", "map");
			} else if (var.getValue() instanceof List) {
				json.put(var.getKey(), new JSONArray((List)var.getValue()));
				json.put(var.getKey()+"_t", "list");
			} else {
				json.put(var.getKey(), var.getValue());
			}
		}
		json.remove("UIEntity");
		return json;
    }
}
