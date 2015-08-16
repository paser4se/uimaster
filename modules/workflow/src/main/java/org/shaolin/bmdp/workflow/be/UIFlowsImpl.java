/*
 *
 * This file is automatically generated on Sun Aug 16 23:27:45 CST 2015
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

public class UIFlowsImpl  implements org.shaolin.bmdp.workflow.be.IUIFlows
{
    private static final long serialVersionUID = 0x90B1123CE87B50FFL;

    private final IConstantService ceService = AppContext.get().getConstantService();

    protected String getBusinessEntityName()
    {
        return "org.shaolin.bmdp.workflow.be.UIFlows";
    }

    public UIFlowsImpl()
    {
        
        _extField = new BEExtensionInfo();
        
    }
    
    
        /**
     * Enable record
     */
    private boolean _enable = true;


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
    protected java.lang.String name;
    
   /**
     *  help is not available
     */    
    protected java.lang.String flow;
    
   /**
     *  help is not available
     */    
    protected long moduleItemId;
    
   /**
     *  help is not available
     */    
    protected ModuleType moduleType = ModuleType.NOT_SPECIFIED;
    
    protected int moduleTypeInt = ModuleType.NOT_SPECIFIED.getIntValue();
    
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
     *  get name
     *
     *  @return name
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     *  get flow
     *
     *  @return flow
     */
    public java.lang.String getFlow() {
        return flow;
    }

    /**
     *  get moduleItemId
     *
     *  @return moduleItemId
     */
    public long getModuleItemId() {
        return moduleItemId;
    }

    /**
     *  get moduleType
     *
     *  @return moduleType
     */
    public ModuleType getModuleType() {
        return moduleType;
    }

    /**
     *  get moduleTypeInt
     *
     *  @return moduleTypeInt
     */
    private int getModuleTypeInt() {
        return moduleTypeInt;
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
     *  set name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     *  set flow
     */
    public void setFlow(java.lang.String flow) {
        this.flow = flow;
    }

    /**
     *  set moduleItemId
     */
    public void setModuleItemId(long moduleItemId) {
        this.moduleItemId = moduleItemId;
    }

    /**
     *  set moduleType
     */
    public void setModuleType(ModuleType moduleType) {
        this.moduleType = moduleType;
    if (moduleTypeInt != moduleType.getIntValue()) {
            moduleTypeInt = moduleType.getIntValue();
        }
    }

    /**
     *  set int moduleType
     */
    private void setModuleTypeInt(int intValue) {
        this.moduleTypeInt = intValue;
        if (moduleTypeInt != moduleType.getIntValue()) {
            moduleType = (ModuleType)ceService.getConstantEntity(ModuleType.ENTITY_NAME).getByIntValue(moduleTypeInt);
        }
    }

    
    /**
     * Check different according to primary key.
     */
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof org.shaolin.bmdp.workflow.be.UIFlowsImpl))
            return false;
        org.shaolin.bmdp.workflow.be.UIFlowsImpl o = (org.shaolin.bmdp.workflow.be.UIFlowsImpl)obj;
        
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
        aBuf.append("org.shaolin.bmdp.workflow.be.UIFlows");
    
        aBuf.append(" : ");
        
        aBuf.append("enable=").append(_enable).append(", ");
        
        aBuf.append("id");
        aBuf.append("=");
        aBuf.append(id);
        aBuf.append(", ");
        
        aBuf.append("name");
        aBuf.append("=");
        aBuf.append(name);
        aBuf.append(", ");
        
        aBuf.append("flow");
        aBuf.append("=");
        aBuf.append(flow);
        aBuf.append(", ");
        
        aBuf.append("moduleItemId");
        aBuf.append("=");
        aBuf.append(moduleItemId);
        aBuf.append(", ");
        
        aBuf.append("moduleType");
        aBuf.append("=");
        aBuf.append(moduleType);
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
            
        org.shaolin.bmdp.datamodel.bediagram.StringType nameBEType = new org.shaolin.bmdp.datamodel.bediagram.StringType();
    
        //MemberType Define for name
        member = new MemberType();
        member.setName("name");
        member.setDescription("null");
        member.setType(nameBEType);
        memberTypeList.add(member);
            
        org.shaolin.bmdp.datamodel.bediagram.StringType flowBEType = new org.shaolin.bmdp.datamodel.bediagram.StringType();
    
        //MemberType Define for flow
        member = new MemberType();
        member.setName("flow");
        member.setDescription("null");
        member.setType(flowBEType);
        memberTypeList.add(member);
            
        org.shaolin.bmdp.datamodel.bediagram.LongType moduleItemIdBEType = new org.shaolin.bmdp.datamodel.bediagram.LongType();
    
        //MemberType Define for moduleItemId
        member = new MemberType();
        member.setName("moduleItemId");
        member.setDescription("null");
        member.setType(moduleItemIdBEType);
        memberTypeList.add(member);
            
        org.shaolin.bmdp.datamodel.bediagram.CEObjRefType moduleTypeBEType = new org.shaolin.bmdp.datamodel.bediagram.CEObjRefType();
    
        TargetEntityType moduleTypeTargetEntity = new TargetEntityType();
        moduleTypeBEType.setTargetEntity(moduleTypeTargetEntity);
        moduleTypeTargetEntity.setEntityName("org.shaolin.bmdp.workflow.ce.ModuleType");
            
        //MemberType Define for moduleType
        member = new MemberType();
        member.setName("moduleType");
        member.setDescription("null");
        member.setType(moduleTypeBEType);
        memberTypeList.add(member);
            
        return memberTypeList;
    }
    
    public IUIFlows createEntity ()
    {
        return new UIFlowsImpl();
    }
    
}

        

