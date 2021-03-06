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
package org.shaolin.bmdp.i18n;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * Localizer retrieves language specific text.<p>
 * 
 * It gets the resource bundle based on the name specified.
 * If resource path is not specified, the default path(bmiasia.ebos.resources)
 * is assumed.
 * 
 * @see java.util.ResourceBundle
 */
public class Localizer implements Serializable
{
    /**
     * name of the resource bundle.
     */
    private String  name;

    /**
     * resource path. it must be a package pattern if you put the resource file under the specific package, 
     * for exam: "org.shaolin.common.resources".
     */
    private String  resourcePath;
    
    /**
     * locale config
     */
    private String  localeConfig;

    /**
     * Constructs a localizer for the specified resource
     * bundle name and default path and default locale
     * 
     * @param   aName   name of the resource bundle.
     */
    public  Localizer(String aName)
    {
        this(aName, null);
    }
    
    /**
     * Constructs a localizer for the specified resource
     * bundle name and path and default locale.
     * 
     * @param   aName           name of the resource bundle.
     * @param   aResourcePath   location of the resource bundle.
     */
    public  Localizer(String aName, String aResourcePath)
    {
        this(aName, aResourcePath, null);
    }


    /**
     * Constructs a localizer for the specified resource 
     * bundle name, path and locale
     * 
     * @param   aName           name of the resource bundle.
     * @param   aResourcePath   location of the resource bundle.
     * @param   aLocale         locale of the resource bundle.
     */
    public  Localizer(String aName, String aResourcePath, String aLocaleConfig)
    {
        name = aName;
        resourcePath = aResourcePath;
        localeConfig = aLocaleConfig;
    }
    
    /**
     * @return   String name of the subsystem.
     */
    public  String getName()
    {
        return name;
    }
    
    /**
     * Gets the localized text for the specified key.<p>
     * 
     * @param   aKey    key to get the localized text for.
     * @return  text specific to current locale.
     */
    public  String  getString(String aKey)
    {
        return getString(aKey, (String)null);
    }

    /**
     * Gets the localized text for the specified key.<p>
     * 
     * @param   aKey            key to get the localized text for.
     * @param   localeConfig    if not specified, use this localizer's locale config
     * @return  text specific to the localeConfig.
     */
    public  String  getString(String aKey, String localeConfig)
    {
    	String aResourceUri = name;
    	if (resourcePath != null && !"".equals(resourcePath)) {
    		aResourceUri = resourcePath + "." + name;
    	}
        String lConfig = getLocaleConfig(localeConfig, this.localeConfig);
        String value = ResourceUtil.getResourceFromProperties(lConfig, aResourceUri, aKey);
		if (value == null) {
			value = aKey;
		}

        return value;
    }

    /**
     * Gets the localized text for the specified status code.<p>
     * 
     * @param   aKey    key to get the localized text for.
     * @param   anArgs  params to format message
     * @return  text specific to current locale.
     */
    public  String  getString(String aKey, Object[] anArgs)
    {
        return getString(aKey, anArgs, null);
    }

    /**
     * Gets the localized text for the specified status code.<p>
     * 
     * @param   aKey    key to get the localized text for.
     * @param   anArgs  params to format message
     * @param   localeConfig    if not specified, use this localizer's locale config
     * @return  text specific to current locale.
     */
    public  String  getString(String aKey, Object[] anArgs, String localeConfig)
    {
        String aMsg = getString(aKey, localeConfig);
        if (null != anArgs && null != aMsg)
        {
            aMsg = MessageFormat.format(aMsg, anArgs);
        }
        return aMsg;
    }
    
    private String getLocaleConfig(String lConfig1, String lConfig2)
    {
        if (lConfig1 != null)
        {
            return lConfig1;
        }
        if (lConfig2 != null)
        {
            return lConfig2;
        }
        return LocaleContext.getUserLocale();
    }

    public static final Localizer EBOS_ERRORS = new Localizer("Errors");
    
    private static final long serialVersionUID = 7723964074630817129L;
}
