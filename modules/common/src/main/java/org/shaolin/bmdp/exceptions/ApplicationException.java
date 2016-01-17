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

import org.shaolin.bmdp.exceptions.BaseException;
import org.shaolin.bmdp.i18n.Localizer;

public abstract class ApplicationException extends BaseException
{
    /**
     * Constructs a ApplicationException with a given exception reason
     * 
     * @param   aReason     the key to the localized reason string
     */
    protected ApplicationException(String aReason)
    {
        super(aReason);
    }


    /**
     * Constructs a ApplicationException with a given exception reason, an argument 
     * array.
     * 
     * @param   aReason     the key to the localized reason string
     * @param   args        the argument list
     */
    protected ApplicationException(String aReason, Object[] args)
    {
        super(aReason, args);
    }


    /**
     * Constructs a ApplicationException given a exception reason, a nested Throwable
     * object.
     * 
     * @param   aReason     the key to the localized reason string
     * @param   aThrowable  nested throwable
     */
    protected ApplicationException(String aReason, Throwable aThrowable)
    {
        super(aReason, aThrowable);
    }


    /**
     * Constructs an exception with the given reason, a nested 
     * Throwable object, and an argument array.
     * 
     * @param   aReason     the key to the localized reason string
     * @param   aThrowable  nested throwable
     * @param   args        the argument list
     */
    protected ApplicationException(String aReason, Throwable aThrowable, 
                            Object[] args)
    {
        super(aReason, aThrowable, args);
    }

    /**
     * Constructs an exception with the given reason, a nested 
     * Throwable object, an argument array, and a localizer.
     * 
     * @param   aReason     the key to the localized reason string
     * @param   aThrowable  nested throwable
     * @param   args        the argument list
     * @param   aLocalizer  the localizer used for the error messages
     */
    protected ApplicationException(String aReason, Throwable aThrowable, 
                            Object[] args, Localizer aLocalizer)
    {
        super(aReason, aThrowable, args, aLocalizer);
    }
    
    /**
     * @deprecated
     */
    protected ApplicationException(String aReason, Throwable aThrowable, 
                            Object[] args, Localizer aLocalizer, boolean aNeedI18N)
    {
        super(aReason, aThrowable, args, aLocalizer);
    }

    private static final long serialVersionUID = 3805250691110616185L;
    
}
