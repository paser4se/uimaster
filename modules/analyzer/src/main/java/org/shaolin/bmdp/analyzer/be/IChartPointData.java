/*
 *
 * This file is automatically generated on Thu Feb 25 23:12:02 CST 2016
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

public interface IChartPointData 
    extends IBusinessEntity, IExtensibleEntity
{
    public final static String ENTITY_NAME = "org.shaolin.bmdp.analyzer.be.ChartPointData";
    
 
    /**
     *  get label
     *
     *  @return label
     */
    public java.lang.String getLabel();

    /**
     *  get dataset
     *
     *  @return dataset
     */
    public java.lang.String getDataset();

    /**
     *  get dataset1
     *
     *  @return dataset1
     */
    public java.lang.String getDataset1();

    /**
     *  get dataset2
     *
     *  @return dataset2
     */
    public java.lang.String getDataset2();

    /**
     *  get dataset3
     *
     *  @return dataset3
     */
    public java.lang.String getDataset3();

    /**
     *  get dataset4
     *
     *  @return dataset4
     */
    public java.lang.String getDataset4();

    /**
     *  set label
     */
    public void setLabel(java.lang.String label);

    /**
     *  set dataset
     */
    public void setDataset(java.lang.String dataset);

    /**
     *  set dataset1
     */
    public void setDataset1(java.lang.String dataset1);

    /**
     *  set dataset2
     */
    public void setDataset2(java.lang.String dataset2);

    /**
     *  set dataset3
     */
    public void setDataset3(java.lang.String dataset3);

    /**
     *  set dataset4
     */
    public void setDataset4(java.lang.String dataset4);


}

        