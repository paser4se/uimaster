/*
 *
 * This file is automatically generated on Sun Mar 13 11:14:55 CST 2016
 */

    
package org.shaolin.bmdp.analyzer.be;
        
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import org.shaolin.bmdp.datamodel.bediagram.*;
import org.shaolin.bmdp.datamodel.common.*;
import org.shaolin.bmdp.runtime.be.IBusinessEntity;
import org.shaolin.bmdp.runtime.be.IExtensibleEntity;
import org.shaolin.bmdp.runtime.be.IPersistentEntity;
import org.shaolin.bmdp.runtime.be.IHistoryEntity;
import org.shaolin.bmdp.runtime.be.ITaskEntity;
import org.shaolin.bmdp.runtime.be.BEExtensionInfo;

import org.shaolin.bmdp.runtime.spi.IConstantService;

import org.shaolin.bmdp.runtime.AppContext;

import org.shaolin.bmdp.runtime.ce.CEUtil;



/**
 * null
 * 
 * This code is generated automatically, any change will be replaced after rebuild.
 * 
 *
 */

public interface IClientDBInfo 
    extends IPersistentEntity, IExtensibleEntity
{
    public final static String ENTITY_NAME = "org.shaolin.bmdp.analyzer.be.ClientDBInfo";
    
 
    /**
     *  get id
     *
     *  @return id
     */
    public long getId();

    /**
     *  get jdbcClass
     *
     *  @return jdbcClass
     */
    public java.lang.String getJdbcClass();

    /**
     *  get url
     *
     *  @return url
     */
    public java.lang.String getUrl();

    /**
     *  get userName
     *
     *  @return userName
     */
    public java.lang.String getUserName();

    /**
     *  get password
     *
     *  @return password
     */
    public java.lang.String getPassword();

    /**
     *  get webroot
     *
     *  @return webroot
     */
    public java.lang.String getWebroot();

    /**
     *  set id
     */
    public void setId(long id);

    /**
     *  set jdbcClass
     */
    public void setJdbcClass(java.lang.String jdbcClass);

    /**
     *  set url
     */
    public void setUrl(java.lang.String url);

    /**
     *  set userName
     */
    public void setUserName(java.lang.String userName);

    /**
     *  set password
     */
    public void setPassword(java.lang.String password);

    /**
     *  set webroot
     */
    public void setWebroot(java.lang.String webroot);


}

        