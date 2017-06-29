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
import java.util.ArrayList;
import java.util.List;

import org.shaolin.bmdp.json.JSONException;
import org.shaolin.bmdp.json.JSONObject;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.javacc.VariableEvaluator;

public abstract class HTMLMultiChoiceType extends HTMLChoiceType 
{
	private static final long serialVersionUID = 8008004105287717253L;
    
	public HTMLMultiChoiceType(String id)
	{
	    super(id);
	}
	 
    public void generateAttribute(UserRequestContext context, String attributeName, Object attributeValue) throws IOException
	{
	    if ( "value".equals(attributeName) )
        {
        }
        else
        {
            super.generateAttribute(context, attributeName, attributeValue);
        }
	}
    
	protected boolean equal(String str)
	{
		List<String> value = getValues();
		for (int i = 0; i<value.size(); i++)
		{
			if (value.get(i).toString().equalsIgnoreCase(str))
			{
				return true;
			}
		}
		return false;
	}

	public List getValues() {
		List values = (List) getAttribute("values");
		return values == null ? new ArrayList() : values;
	}


	public void setValues(List<String> value)
	{
        addAttribute("values", value);
	}

    public JSONObject createJsonModel(VariableEvaluator ee) throws JSONException 
	{
    	return super.createJsonModel(ee);
	}
}
