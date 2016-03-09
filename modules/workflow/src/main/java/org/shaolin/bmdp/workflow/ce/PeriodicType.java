/*
 * This code is generated automatically, any change will be replaced after rebuild.
 * Generated on Wed Mar 09 23:50:02 CST 2016
 */

package org.shaolin.bmdp.workflow.ce;
import java.util.*;
import org.shaolin.bmdp.runtime.ce.IConstantEntity;
import org.shaolin.bmdp.runtime.ce.AbstractConstant;

/**
 * 
 * entityName: org.shaolin.bmdp.workflow.ce.PeriodicType
 *
 */
public final class PeriodicType extends AbstractConstant
{
    public static final String ENTITY_NAME = "org.shaolin.bmdp.workflow.ce.PeriodicType";
    
    protected static final long serialVersionUID = 0x811b9115811b9115L;
    private static String i18nBundle = "org_shaolin_bmdp_workflow_i18n";

    //User-defined constant define

    public static final PeriodicType NOT_SPECIFIED = new PeriodicType(CONSTANT_DEFAULT_VALUE, -1, null, null, null, null, false);

    public static final PeriodicType DAILY = new PeriodicType("Daily", 0, "org.shaolin.bmdp.workflow.ce.PeriodicType.DAILY", "Daily", null, null,false);

    public static final PeriodicType WEEKLY = new PeriodicType("Weekly", 1, "org.shaolin.bmdp.workflow.ce.PeriodicType.WEEKLY", "Weekly", null, null,false);

    public static final PeriodicType MONTHLY = new PeriodicType("Monthly", 2, "org.shaolin.bmdp.workflow.ce.PeriodicType.MONTHLY", "Monthly", null, null,false);

    //End of constant define

    //Common constant define
    public PeriodicType()
    {
        constantList.add(NOT_SPECIFIED);
        constantList.add(DAILY);
        constantList.add(WEEKLY);
        constantList.add(MONTHLY);
    }

    private PeriodicType(long id, String value, int intValue, String i18nKey, String description)
    {
        super(id, value, intValue, i18nKey, description);
    }
    
    private PeriodicType(String value, int intValue, String i18nKey,
        String description, Date effTime, Date expTime)
    {
        super(value, intValue, i18nKey, description, effTime, expTime);
    }

    private PeriodicType(String value, int intValue, String i18nKey,
            String description, Date effTime, Date expTime, boolean isPassivated)
    {
        super(value, intValue, i18nKey, description, effTime, expTime, isPassivated);
    }
    
    public String getI18nBundle()
    {
        return i18nBundle;
    }

    public void setI18nBundle(String bundle)
    {
        i18nBundle = bundle;
    }

    protected AbstractConstant __create(String value, int intValue, String i18nKey,
        String description, Date effTime, Date expTime)
    {
        return new PeriodicType(value, intValue, i18nKey,
            description, effTime, expTime);
    }

    protected String getTypeEntityName()
    {
        return ENTITY_NAME;
    }

}

