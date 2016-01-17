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
package org.shaolin.bmdp.exceptions;

import org.shaolin.bmdp.i18n.Localizer;

public abstract class BaseRuntimeException
    extends RuntimeException
{

    /**
     * Constructs a BaseRuntimeException with a given exception reason
     * 
     * @param   aReason     the key to the localized reason string.
     */
    protected BaseRuntimeException(String aReason)
    {
        this(aReason, null, null);
    }


    /**
     * Constructs a BaseRuntimeException with a given exception reason, an
     * argument array
     * 
     * @param   aReason     the key to the localized reason string
     * @param   args        the argument list
     */
    protected BaseRuntimeException(String aReason, Object[] args)
    {
        this(aReason, null, args);
    }


    /**
     * Constructs a BaseRuntimeException given a exception reason, a nested 
     * Throwable object.
     * 
     * @param   aReason     the key to the localized reason string
     * @param   aThrowable  the argument list
     */
    protected BaseRuntimeException(String aReason, Throwable aThrowable)
    {
        this(aReason, aThrowable, null);
    }


    /**
     * Constructs an exception with a given exception reason, a nested 
     * Throwable object, and an argument array.
     * 
     * @param   aReason     the key to the localized reason string
     * @param   aThrowable  nested throwable
     * @param   args        the argument list
     */
    protected BaseRuntimeException(String aReason, 
                                   Throwable aThrowable, 
                                   Object[] args)
    {
        this(aReason, aThrowable, args, null);
    }


    /**
     * Constructs an exception with a given exception reason, a nested 
     * Throwable object, an argument array, and a localizer.
     * 
     * @param   aReason     the key to the localized reason string
     * @param   aThrowable  the argument list
     * @param   args        the argument list
     * @param   aLocalizer  the localizer used for the error messages
     */
    protected BaseRuntimeException(String aReason, 
                                   Throwable aThrowable, 
                                   Object[] args, Localizer aLocalizer)
    {
        super(null, aThrowable);
        data = new ExceptionData(aReason, args, aLocalizer);
    }

    public String getReason()
    {
        return data.getReason();
    }


    public String getMessage()
    {
        return data.getMessage();
    }

    public String getMessage(String localeConfig)
    {
        return data.getMessage(localeConfig);
    }

    public String getMessage(Localizer localizer)
    {
        return data.getMessage(localizer);
    }
    
    public String getMessage(Localizer localizer, String localeConfig)
    {
        return data.getMessage(localizer, localeConfig);
    }

    public Throwable getNestedThrowable()
    {
        return getCause();
    }

    /**
     * Returns the root cause of this exception.
     * If the cause is nonexistent, returns this exception itself.
     */
    public Throwable getRootCause()
    {
        return ExceptionData.getRootCause(this);
    }

    public int getArgumentNumber()
    {
        return data.getArgumentNumber();
    }


    public Object getArgument(int argPosition)
    {
        return data.getArgument(argPosition);
    }

    /**
     * an ExceptionData object which actually implements all the extra 
     * functionalities that BraveMinds exception and error handling framework 
     * promises to provide.
     */
    private ExceptionData data;

    private static final long serialVersionUID = -7992921811279744097L;
}//BaseRuntimeException
