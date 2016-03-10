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

public class FlowEntityImpl  implements org.shaolin.bmdp.workflow.be.IFlowEntity
{
    private static final long serialVersionUID = 0x90B1123CE87B50FFL;

    private final transient IConstantService ceService = AppContext.get().getConstantService();

    protected String getBusinessEntityName()
    {
        return "org.shaolin.bmdp.workflow.be.FlowEntity";
    }

    public FlowEntityImpl()
    {
        
        _extField = new BEExtensionInfo();
        
    }
    
    
        /**
     * Create Date
     */
    private java.util.Date createDate = null;

    /**
     * Enable record
     */
    private boolean _enable = true;


    /**
     * History version
     */
    protected int _version = 0;

    /**
     * History starttime
     */
    private java.util.Date _starttime = new java.util.Date();

    /**
     * History endtime
     */
    private java.util.Date _endtime = null;

    /**
     * History _optuserid
     */
    private long _optuserid = 0L;


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
    protected long id;
    
   /**
     *  help is not available
     */    
    protected java.lang.String entityName;
    
   /**
     *  help is not available
     */    
    protected java.lang.String type;
    
   /**
     *  help is not available
     */    
    protected java.lang.String content;
    
        /**
     *  Get createDate
     *
     *  @return java.util.Date
     */
    public java.util.Date getCreateDate() {
        return createDate;
    }
            /**
     *  Is enable
     *
     *  @return boolean
     */
    public boolean isEnabled() {
        return _enable;
    }
            /**
     *  Is enable
     *
     *  @return boolean
     */
    private boolean get_enable() {
        return _enable;
    }
        
    /**
     *  get version
     *
     *  @return version
     */
    public int getVersion() {
        return _version;
    }

    private int get_version() {
        return _version;
    }

    /**
     *  get operator user id
     *
     *  @return user id
     */
    public long getOptUserId() {
        return _optuserid;
    }

    /**
     *  get operator user id
     *
     *  @return user id
     */
    private long get_optuserid() {
        return _optuserid;
    }

    /**
     *  get start time
     *
     *  @return start time
     */
    public java.util.Date getStarttime() {
        return _starttime;
    }

    private java.util.Date get_starttime() {
        return _starttime;
    }

    /**
     *  get endtime
     *
     *  @return endtime
     */
    public java.util.Date getEndtime() {
        return _endtime;
    }

    private java.util.Date get_endtime() {
        return _endtime;
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
     *  get id
     *
     *  @return id
     */
    public long getId() {
        return id;
    }

    /**
     *  get entityName
     *
     *  @return entityName
     */
    public java.lang.String getEntityName() {
        return entityName;
    }

    /**
     *  get type
     *
     *  @return type
     */
    public java.lang.String getType() {
        return type;
    }

    /**
     *  get content
     *
     *  @return content
     */
    public java.lang.String getContent() {
        return content;
    }

        /**
     *  set createDate
     *  @parameter true or false.
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

        /**
     *  set enable
     *  @parameter true or false.
     */
    public void setEnabled(boolean enable) {
        _enable = enable;
    }

        /**
     *  set enable
     *  @parameter true or false.
     */
    private void set_enable(boolean enable) {
        _enable = enable;
    }

    
    /**
     *  set version
     */
    public void setVersion(int version) {
        _version = version;
    }

    private void set_version(int version) {
        _version = version;
    }

    /**
     *  set operator user id
     *  @parameter user id.
     */
    public void setOptUserId(long optUserId) {
        _optuserid = optUserId;
    }

    /**
     *  set operator user id
     *  @parameter user id.
     */
    private void set_optuserid(long optUserId) {
        _optuserid = optUserId;
    }

    /**
     *  set start time
     *  @parameter start time which is the start time of object.
     */
    public void setStarttime(java.util.Date starttime) {
        _starttime = starttime;
    }

    private void set_starttime(java.util.Date starttime) {
        _starttime = starttime;
    }

    /**
     *  set endtime
     *  @parameter endtime which is the end time of object.
     */
    public void setEndtime(java.util.Date endtime) {
        _endtime = endtime;
    }

    private void set_endtime(java.util.Date endtime) {
        _endtime = endtime;
    }

/**
     *  set _extType
     *  @param _extType which is the extension type of be object.
     */
    public void set_extType(java.lang.String _extType) {
        this._extType = _extType;
    }
        
    /**
     *  set id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *  set entityName
     */
    public void setEntityName(java.lang.String entityName) {
        this.entityName = entityName;
    }

    /**
     *  set type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }

    /**
     *  set content
     */
    public void setContent(java.lang.String content) {
        this.content = content;
    }

    
    /**
     * Check different according to primary key.
     */
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof org.shaolin.bmdp.workflow.be.FlowEntityImpl))
            return false;
        org.shaolin.bmdp.workflow.be.FlowEntityImpl o = (org.shaolin.bmdp.workflow.be.FlowEntityImpl)obj;
        
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
        aBuf.append("org.shaolin.bmdp.workflow.be.FlowEntity");
    
        aBuf.append(" : ");
        
        aBuf.append("createDate=").append(createDate).append(", ");
        
        aBuf.append("enable=").append(_enable).append(", ");
        
        aBuf.append("id");
        aBuf.append("=");
        aBuf.append(id);
        aBuf.append(", ");
        
        aBuf.append("entityName");
        aBuf.append("=");
        aBuf.append(entityName);
        aBuf.append(", ");
        
        aBuf.append("type");
        aBuf.append("=");
        aBuf.append(type);
        aBuf.append(", ");
        
        aBuf.append("content");
        aBuf.append("=");
        aBuf.append(content);
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
        
        org.shaolin.bmdp.datamodel.bediagram.LongType idBEType = new org.shaolin.bmdp.datamodel.bediagram.LongType();
    
        //MemberType Define for id
        member = new MemberType();
        member.setName("id");
        member.setDescription("null");
        member.setType(idBEType);
        memberTypeList.add(member);
            
        org.shaolin.bmdp.datamodel.bediagram.StringType entityNameBEType = new org.shaolin.bmdp.datamodel.bediagram.StringType();
    
        //MemberType Define for entityName
        member = new MemberType();
        member.setName("entityName");
        member.setDescription("null");
        member.setType(entityNameBEType);
        memberTypeList.add(member);
            
        org.shaolin.bmdp.datamodel.bediagram.StringType typeBEType = new org.shaolin.bmdp.datamodel.bediagram.StringType();
    
        //MemberType Define for type
        member = new MemberType();
        member.setName("type");
        member.setDescription("null");
        member.setType(typeBEType);
        memberTypeList.add(member);
            
        org.shaolin.bmdp.datamodel.bediagram.StringType contentBEType = new org.shaolin.bmdp.datamodel.bediagram.StringType();
    
        //MemberType Define for content
        member = new MemberType();
        member.setName("content");
        member.setDescription("null");
        member.setType(contentBEType);
        memberTypeList.add(member);
            
        return memberTypeList;
    }
    
    public IFlowEntity createEntity ()
    {
        return new FlowEntityImpl();
    }
    
}

        

