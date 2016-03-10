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



/**
 * null
 * 
 * This code is generated automatically, any change will be replaced after rebuild.
 * 
 *
 */

public class CreateWorkflowImpl  implements org.shaolin.bmdp.workflow.be.ICreateWorkflow
{
    private static final long serialVersionUID = 0x90B1123CE87B50FFL;

    private final transient IConstantService ceService = AppContext.get().getConstantService();

    protected String getBusinessEntityName()
    {
        return "org.shaolin.bmdp.workflow.be.CreateWorkflow";
    }

    public CreateWorkflowImpl()
    {
        
        sessionServcies = new java.util.ArrayList<ISessionVarDefinition>();
            
        _extField = new BEExtensionInfo();
        
    }
    
    
        /**
     * Create Date
     */
    private java.util.Date createDate = null;


    /**
     *  BEExtension _extType
     */
    protected String _extType;

    /**
     *  BEExtension _extField
     */
    protected BEExtensionInfo _extField;
        
   /**
     *  help is not available
     */    
    protected java.lang.String workflowName;
    
   /**
     *  help is not available
     */    
    protected java.lang.String flowName;
    
   /**
     *  help is not available
     */    
    protected java.util.List<ISessionVarDefinition> sessionServcies;
    
        /**
     *  Get createDate
     *
     *  @return java.util.Date
     */
    public java.util.Date getCreateDate() {
        return createDate;
    }
        
    /**
     *  get _extType
     *
     *  @return _extType
     */
    public String get_extType() {
        return _extType;
    }

    /**
     *  get _extField
     *
     *  @return _extField
     */
    public BEExtensionInfo get_extField() {
        return _extField;
    }
        
    /**
     *  get workflowName
     *
     *  @return workflowName
     */
    public java.lang.String getWorkflowName() {
        return workflowName;
    }

    /**
     *  get flowName
     *
     *  @return flowName
     */
    public java.lang.String getFlowName() {
        return flowName;
    }

    /**
     *  get sessionServcies
     *
     *  @return sessionServcies
     */
    public java.util.List<ISessionVarDefinition> getSessionServcies() {
        return sessionServcies;
    }

        /**
     *  set createDate
     *  @parameter true or false.
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

    /**
     *  set _extType
     *  @param _extType which is the extension type of be object.
     */
    public void set_extType(java.lang.String _extType) {
        this._extType = _extType;
    }
        
    /**
     *  set workflowName
     */
    public void setWorkflowName(java.lang.String workflowName) {
        this.workflowName = workflowName;
    }

    /**
     *  set flowName
     */
    public void setFlowName(java.lang.String flowName) {
        this.flowName = flowName;
    }

    /**
     *  set sessionServcies
     */
    public void setSessionServcies(java.util.List<ISessionVarDefinition> sessionServcies) {
        this.sessionServcies = sessionServcies;
    }

    
    /**
     * Check different according to primary key.
     */
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof org.shaolin.bmdp.workflow.be.CreateWorkflowImpl))
            return false;
        org.shaolin.bmdp.workflow.be.CreateWorkflowImpl o = (org.shaolin.bmdp.workflow.be.CreateWorkflowImpl)obj;
        
        boolean result = super.equals(obj);

        boolean eq = true;
        
        return result;
    }

    /**
     * Generate hashCode according to primary key.
     */
    public int hashCode() {
        
        int result = super.hashCode();

        return result;
    }
        
    
     /**
     * Gets the String format of the business entity.
     *
     * @return String the business entity in String format.
     */
    public  String  toString() {
        StringBuffer aBuf = new StringBuffer();
        aBuf.append("org.shaolin.bmdp.workflow.be.CreateWorkflow");
    
        aBuf.append(" : ");
        
        aBuf.append("createDate=").append(createDate).append(", ");
        
        aBuf.append("workflowName");
        aBuf.append("=");
        aBuf.append(workflowName);
        aBuf.append(", ");
        
        aBuf.append("flowName");
        aBuf.append("=");
        aBuf.append(flowName);
        aBuf.append(", ");
        
        return aBuf.toString();
    }
    
    
     /**
     * Gets list of MemberType.
     *
     * @return List     the list of MemberType.
     */
    public List<MemberType> getMemberList() {
        List<MemberType> memberTypeList = new ArrayList<MemberType>();
        
        MemberType member = null;
        
        org.shaolin.bmdp.datamodel.bediagram.StringType workflowNameBEType = new org.shaolin.bmdp.datamodel.bediagram.StringType();
    
        //MemberType Define for workflowName
        member = new MemberType();
        member.setName("workflowName");
        member.setDescription("null");
        member.setType(workflowNameBEType);
        memberTypeList.add(member);
            
        org.shaolin.bmdp.datamodel.bediagram.StringType flowNameBEType = new org.shaolin.bmdp.datamodel.bediagram.StringType();
    
        //MemberType Define for flowName
        member = new MemberType();
        member.setName("flowName");
        member.setDescription("null");
        member.setType(flowNameBEType);
        memberTypeList.add(member);
            
        org.shaolin.bmdp.datamodel.bediagram.BEListType sessionServciesBEType = new org.shaolin.bmdp.datamodel.bediagram.BEListType();
    
        org.shaolin.bmdp.datamodel.bediagram.BEObjRefType sessionServciesElementBEType = new org.shaolin.bmdp.datamodel.bediagram.BEObjRefType();
    
        TargetEntityType sessionServciesElementTargetEntity = new TargetEntityType();
        sessionServciesElementTargetEntity.setEntityName("org.shaolin.bmdp.workflow.be.SessionVarDefinition");
        sessionServciesElementBEType.setTargetEntity(sessionServciesElementTargetEntity);
            
        sessionServciesBEType.setElementType(sessionServciesElementBEType);
            
        //MemberType Define for sessionServcies
        member = new MemberType();
        member.setName("sessionServcies");
        member.setDescription("null");
        member.setType(sessionServciesBEType);
        memberTypeList.add(member);
            
        return memberTypeList;
    }
    
    public ICreateWorkflow createEntity ()
    {
        return new CreateWorkflowImpl();
    }
    
}

        

