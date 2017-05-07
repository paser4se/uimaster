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

import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.WebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HTMLChoiceType extends HTMLWidgetType
{
    private static final Logger logger = LoggerFactory.getLogger(HTMLChoiceType.class);

    public static final String OPTIONVALUE_KEY = "optionValue";

    public static final String OPTIONDISPLAYVALUE_KEY = "optionDisplayValue";

    public HTMLChoiceType(String id)
    {
        super(id);
    }

    public List<String> getOptionValues()
    {
    	List<String> optionValues = (List<String>)getAttribute(OPTIONVALUE_KEY);
        if (UserRequestContext.UserContext.get().isValueMask() && optionValues != null)
        {
            int length = optionValues.size();
            List<String> maskedOptionValues = new ArrayList<String>();
            for (int i = 0; i < length; i++)
            {
                maskedOptionValues.add(WebConfig.getHiddenValueMask());
            }
            return maskedOptionValues;
        }
        else
        {
            return optionValues;
        }
    }

    public List<String> getOptionDisplayValues()
    {
    	List<String> optionDisplayValues = (List<String>)getAttribute(OPTIONDISPLAYVALUE_KEY);
        if (UserRequestContext.UserContext.get().isValueMask() && optionDisplayValues != null)
        {
            int length = optionDisplayValues.size();
            List<String> maskedOptionDisplayValues = new ArrayList<String>();
            for (int i = 0; i < length; i++)
            {
                maskedOptionDisplayValues.add(WebConfig.getHiddenValueMask());
            }
            return maskedOptionDisplayValues;
        }
        else
        {
            return optionDisplayValues;
        }
    }

    public void generateAttribute(UserRequestContext context, String attributeName,
            Object attributeValue) throws IOException
    {
        if (OPTIONVALUE_KEY.equals(attributeName) || OPTIONDISPLAYVALUE_KEY.equals(attributeName))
        {
        }
        else
        {
            super.generateAttribute(context, attributeName, attributeValue);
        }
    }

    public void setOptionValues(List<String> optionValues) // should be list of string
    {
        if (optionValues == null)
        {
            return;
        }
        addAttribute(OPTIONVALUE_KEY, optionValues);
    }

    public void setOptionDisplayValues(List<String> optionDisplayValues) // should be list of string
    {
        if (optionDisplayValues == null)
        {
            return;
        }
        addAttribute(OPTIONDISPLAYVALUE_KEY, optionDisplayValues);
    }

}
