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
import java.util.List;

import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;

abstract public class SingleChoice<T> extends Choice<T> implements Serializable
{
    private static final long serialVersionUID = 329245687316770737L;

    private Class realValueDataType = String.class;
    
	public SingleChoice(String id, Layout layout)
    {
        super(id, layout);
    }
	
	public void setRealValueType(Class realValueDataType) {
		this.realValueDataType = realValueDataType;
	}

    public T generateAttribute(String name, Object value, StringBuilder sb)
    {
        if ( !name.equals("value") )
        {
            super.generateAttribute(name, value, sb);
        }
        return (T)this;
    }
    
    public void setOptions(List<String> displayValues, List<String> optionValues)
    {
        super.setOptions(displayValues, optionValues);
        if(optionValues != null && optionValues.size() > 0)
            addAttribute("value", optionValues.get(0), true);//select the first one by default.
    }
    
    public void clearValue()
    {
        addAttribute("value", "");
    }
    
    public void setValue(String value)
    {
        if(this._isReadOnly())
        {
            return;
        }
        if(value == null || this.optionValues == null)
        {
            return;
        }
        if (this.optionValues.isEmpty()) {
        		// allow setting the value if the options is empty as agreed.
        		addAttribute("value", value);
            this.notifyChange();
            return;
        }
        if (checkValueExist(value))
        {
            addAttribute("value", value);
            this.notifyChange();
        }
    }

    boolean checkValueExist(String value)
    {
        for (Object option : optionValues)
        {
            if (String.valueOf(option).equals(value))
                return true;
        }

        return false;
    }
    
    public T checkConstraint() {
    	Object value = getAttribute("value");
    	if(this.hasConstraint("selectedValueConstraint")) {
    		String selectedValue = (String)this.getConstraint("selectedValueConstraint");
    		if (value == null || !selectedValue.equals(value)) {
    			throw new IllegalStateException("UI Constraint fails in: " 
    							+ this.getConstraint("selectedValueConstraintText")
    							 + ",UIID: " + this.getId());
    		}
    	}
    	return (T)this;
	}

    public Object getRealValue() {
    	String value = getValue();
    	if (this.realValueDataType == Long.class || this.realValueDataType == long.class) {
			return Long.valueOf(value.equals("") ? "0" : value);
    	} else if (this.realValueDataType == Integer.class || this.realValueDataType == int.class) {
    		return Integer.valueOf(value.equals("") ? "0" : value);
    	} else if (this.realValueDataType == Double.class || this.realValueDataType == double.class) {
    		return Double.valueOf(value.equals("") ? "0" : value);
    	} else if (this.realValueDataType == Float.class || this.realValueDataType == float.class) {
    		return Float.valueOf(value.equals("") ? "0" : value);
    	} else {
    		return value;
    	}
    }
    
    public static Object getRealValue(JSONObject json, String clazz0) throws JSONException, ClassNotFoundException {
    	String value = json.getString("value");
    	Class clazz = Class.forName(clazz0);
    	if (clazz == Long.class || clazz == long.class) {
			return Long.valueOf(value.equals("") ? "0" : value);
    	} else if (clazz == Integer.class || clazz == int.class) {
    		return Integer.valueOf(value.equals("") ? "0" : value);
    	} else if (clazz == Double.class || clazz == double.class) {
    		return Double.valueOf(value.equals("") ? "0" : value);
    	} else if (clazz == Float.class || clazz == float.class) {
    		return Float.valueOf(value.equals("") ? "0" : value);
    	} else {
    		return value;
    	}
    }
    
    public JSONObject toJSON() throws JSONException {
		JSONObject json = super.toJSON();
		json.getJSONObject("attrMap").put("realVType", realValueDataType.getName());
		json.getJSONObject("attrMap").put("value", this.getValue());
		return json;
	}
	
	public void fromJSON(JSONObject json) throws Exception {
		super.fromJSON(json);
		if (json.getJSONObject("attrMap").has("realVType")) {
			this.realValueDataType = Class.forName(json.getJSONObject("attrMap").getString("realVType"));
		} else {
			this.realValueDataType = String.class;
		}
		
		if (json.getJSONObject("attrMap").has("value")) {
			addAttribute("value", json.getJSONObject("attrMap").getString("value"));
		}
	}

}
