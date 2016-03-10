/*
 *
 * This file is automatically generated on Fri Mar 11 00:00:41 CST 2016
 */

    
package org.shaolin.bmdp.workflow.be;
        
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

import org.shaolin.bmdp.workflow.ce.*;


/**
 * null
 * 
 * This code is generated automatically, any change will be replaced after rebuild.
 * 
 *
 */

public interface ICreateNode 
    extends IBusinessEntity, IExtensibleEntity
{
    public final static String ENTITY_NAME = "org.shaolin.bmdp.workflow.be.CreateNode";
    
 
    /**
     *  get name
     *
     *  @return name
     */
    public java.lang.String getName();

    /**
     *  get description
     *
     *  @return description
     */
    public java.lang.String getDescription();

    /**
     *  get expression
     *
     *  @return expression
     */
    public java.lang.String getExpression();

    /**
     *  get expiredDays
     *
     *  @return expiredDays
     */
    public int getExpiredDays();

    /**
     *  get expiredHours
     *
     *  @return expiredHours
     */
    public int getExpiredHours();

    /**
     *  get partyType
     *
     *  @return partyType
     */
    public java.lang.String getPartyType();

    /**
     *  get actionPage
     *
     *  @return actionPage
     */
    public java.lang.String getActionPage();

    /**
     *  get actionPosition
     *
     *  @return actionPosition
     */
    public java.lang.String getActionPosition();

    /**
     *  get type
     *
     *  @return type
     */
    public NodeType getType();

    /**
     *  set name
     */
    public void setName(java.lang.String name);

    /**
     *  set description
     */
    public void setDescription(java.lang.String description);

    /**
     *  set expression
     */
    public void setExpression(java.lang.String expression);

    /**
     *  set expiredDays
     */
    public void setExpiredDays(int expiredDays);

    /**
     *  set expiredHours
     */
    public void setExpiredHours(int expiredHours);

    /**
     *  set partyType
     */
    public void setPartyType(java.lang.String partyType);

    /**
     *  set actionPage
     */
    public void setActionPage(java.lang.String actionPage);

    /**
     *  set actionPosition
     */
    public void setActionPosition(java.lang.String actionPosition);

    /**
     *  set type
     */
    public void setType(NodeType type);


}

        