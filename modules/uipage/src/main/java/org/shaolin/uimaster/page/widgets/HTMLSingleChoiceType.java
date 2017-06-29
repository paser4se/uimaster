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

import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HTMLSingleChoiceType extends HTMLChoiceType
{
    private static final Logger logger = LoggerFactory.getLogger(HTMLSingleChoiceType.class);

    private Class realValueDataType = String.class;
    
    private static final long serialVersionUID = 9069902870270456324L;
    
	public HTMLSingleChoiceType(String id)
	{
	    super(id);
	}
	
    public void generateAttribute(UserRequestContext context, String attributeName, Object attributeValue) throws IOException
	{
	    if("value".equals(attributeName))
        {
        }
        else
        {
            super.generateAttribute(context, attributeName, attributeValue);
        }
	}
    
    public String getValue()
	{
	    String value = (String) getAttribute("value");
        return value == null ? "":value;
	}

	public void setValue(String value)
	{
	    addAttribute("value", value);
	}
	
	public void setRealValueDataType(Class realValueDataType) {
		this.realValueDataType = realValueDataType;
	}
	
	public Class getRealValueDataType() {
		return this.realValueDataType;
	}
	
	public JSONObject createJsonModel(VariableEvaluator ee) throws JSONException 
    {
		JSONObject json = super.createJsonModel(ee);
		json.put("realVType", realValueDataType.getName());
		return json;
    }

}
