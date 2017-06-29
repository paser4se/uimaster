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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.shaolin.bmdp.json.JSONArray;
import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.bmdp.runtime.AppContext;
import org.shaolin.bmdp.runtime.ce.CEUtil;
import org.shaolin.uimaster.page.AjaxContext;
import org.shaolin.uimaster.page.AjaxContextHelper;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.ajax.json.IDataItem;

abstract public class Choice<T> extends Widget<T>
{
    private static final long serialVersionUID = -2155496455366137360L;

    private static final String CLEAR_ALL_ITEMS = "CLEAR_ALL_ITEMS_2155496455366137360L";
    
    protected List<String> optionValues = new ArrayList<String>();

    protected List<String> optionDisplayValues = new ArrayList<String>();
    
    protected String ceName;
    
    protected int expendlevels = 1;
    
	public Choice(String id, Layout layout)
    {
        super(id, layout);
        this._setWidgetLabel(id);
    }

    /**
     * 
     * @param itemName
     * @param itemValue
     */
    public void addOption(String itemName, String itemValue)
    {
        if(this._isReadOnly())
        {
            return;
        }
        if(!optionValues.contains(itemValue))
        {
            optionValues.add(itemValue);
            optionDisplayValues.add(itemName);
            
            if (!this.isListened())
            {
                return;
            }
            AjaxContext ajaxContext = AjaxContextHelper.getAjaxContext();
            if (ajaxContext == null || !ajaxContext.existElement(this))
            {
                return;
            }
            
            String str = "{'item':'true','name':'"+ itemName +"','value':'"+ HTMLUtil.handleEscape(String.valueOf(itemValue)) +"'}";
            IDataItem dataItem = AjaxContextHelper.updateAttrItem(this.getId(), str);
            dataItem.setFrameInfo(getFrameInfo());
            ajaxContext.addDataItem(dataItem);
        }
    }
    
    public String getOptionName(String itemValue) {
    	if(optionValues.contains(itemValue))
        {
    		int index = optionValues.indexOf(itemValue);
    		return optionDisplayValues.get(index);
        }
    	return "";
    }
    
    /**
     * 
     * Be noticed that the parameter is itemValue!
     * 
     * @param itemValue
     */
    public void removeOption(String itemValue)
    {
        if(this._isReadOnly())
        {
            return;
        }
        if(optionValues.contains(itemValue))
        {
            int index = optionValues.indexOf(itemValue);
            optionValues.remove(index);
            optionDisplayValues.remove(index);
            
            _removeAttribute(itemValue);
        }
    }
    
    public void removeAllOptions() {
    	if(this._isReadOnly())
        {
            return;
        }
    	optionValues.clear();
    	optionDisplayValues.clear();
    	
    	updateOptions();
    }

    public void setOptions(List<String> displayValues, List<String> optionValues)
    {
        if(displayValues == null && optionValues == null)
        {
            return;
        }
        if(displayValues == null)
        {
            displayValues = new ArrayList<String>(optionValues);
        }
        if(displayValues.size() != optionValues.size())
        {
            throw new IllegalArgumentException("Value's size and Display's size are mismatched!");
        }
        if (!this.isListened())
        {
            // create ajax object initially.
            this.optionValues = new ArrayList<String>(optionValues);
            //to cut the reference between optionValues and displayValues. If both are the same object, it will be a potential problem.
            this.optionDisplayValues = new ArrayList<String>(displayValues);//
            
            return;
        }
        if(this._isReadOnly())
        {
            return;
        }
        
        this.optionValues = new ArrayList<String>(optionValues);
        this.optionDisplayValues = new ArrayList<String>(displayValues);
        
        updateOptions();
    }
    
    private void updateOptions()
    {
        if(optionDisplayValues.size() != optionValues.size())
        {
            throw new IllegalArgumentException("The size of Value option and the size of Display option are mismatched!");
        }
        if (!this.isListened())
        {
            return;
        }
        AjaxContext ajaxContext = AjaxContextHelper.getAjaxContext();
        if (ajaxContext == null || !ajaxContext.existElement(this))
        {
            return;
        }
        if(this._isReadOnly())
        {
            return;
        }
        
        _removeAttribute(CLEAR_ALL_ITEMS);
        for(int i=0; i<this.optionValues.size(); i++)
        {
            String name = this.optionDisplayValues.get(i).toString();
            String value = this.optionValues.get(i).toString();
            
            String str = "{'item':'true','name':'"+ name +"','value':'"+ HTMLUtil.handleEscape(String.valueOf(value)) +"'}";
            IDataItem dataItem = AjaxContextHelper.updateAttrItem(this.getId(), str);
            dataItem.setFrameInfo(getFrameInfo());
            ajaxContext.addDataItem(dataItem);
        }
        String str = "{'finish':'true'}";
        IDataItem dataItem = AjaxContextHelper.updateAttrItem(this.getId(), str);
        dataItem.setFrameInfo(getFrameInfo());
        ajaxContext.addDataItem(dataItem);
    }
    
    public List<String> getOptionValues()
    {
        return optionValues;
    }

    public List<String> getOptionDisplayValues()
    {
        return optionDisplayValues;
    }
    
    public String getValue()
    {
    	checkConstraint();
    	
        String value = String.valueOf(getAttribute("value"));
        return value == null ? "" : value;
    }
    
    public String getSelectedDisplayValue() {
    	String value = getValue();
    	for (int i=0; i<optionValues.size(); i++) {
    		if (value.equals(optionValues.get(i))) {
    			return optionDisplayValues.get(i);
    		}
    	}
    	return "";
    }
    
    public String getCeName() {
  		return ceName;
  	}

  	public void setCeName(String ceName) {
  		this.ceName = ceName;
  	}
    
  	public int getExpendlevels() {
 		return expendlevels;
 	}

 	public void setExpendlevels(int expendlevels) {
 		this.expendlevels = expendlevels;
 	}
  	
    public JSONObject toJSON() throws JSONException {
		JSONObject json = super.toJSON();
		if (json.has("attrMap") && json.getJSONObject("attrMap").has("optionValue")) {
			json.getJSONObject("attrMap").remove("optionValue");
		}
		if (this.ceName != null && this.ceName.length() > 0) {
			json.put("ce", this.ceName);
			json.put("expLevel", this.expendlevels);
		} else {
			// normal list
			json.put("optValues", new JSONArray(this.getOptionValues()));
			//json.put("optDvalues", new JSONArray(this.getOptionDisplayValues()));
		}
		return json;
	}
	
	@SuppressWarnings("unchecked")
	public void fromJSON(JSONObject json) throws Exception {
		super.fromJSON(json);
		if (json.has("ce")) {
			this.expendlevels = json.getInt("expLevel");
			if (this.expendlevels <= 1) {
				//set the optValues and Display Values from CE
				Map<Integer, String> items = CEUtil.getAllConstants(json.getString("ce"));
				Iterator<Map.Entry<Integer,String>> i = items.entrySet().iterator();
				while (i.hasNext()) {
					Map.Entry<Integer,String> entry = i.next();
					this.optionValues.add(entry.getKey() + "");
					this.optionDisplayValues.add(entry.getValue());
				}
			} else {
				CEUtil.getCEItems(this.expendlevels, this.optionValues, this.optionDisplayValues, 
						AppContext.get().getConstantService().getConstantEntity(json.getString("ce")));
			}
		} else {
			this.optionValues = json.getJSONArray("optValues").toList();
			this.optionDisplayValues = this.optionValues;
		}
	}
}
