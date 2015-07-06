/*
 * Copyright 2000-2003 by BMI Asia, Inc.,
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of BMI Asia, Inc.("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with BMI Asia.
 */

//package
package org.shaolin.bmdp.persistence.query.mapping;

//imports
import java.lang.reflect.Field;

public interface IFieldOwnerDescriptor
{
    public Field getField(String fieldName);
    
    public String getConfigPath();

    public static final String ___REVISION___ = "$Revision: 1.3 $";
}
