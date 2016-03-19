/*
 * This code is generated automatically, any change will be replaced after rebuild.
 * Generated on Sat Mar 19 15:29:19 CST 2016
 */

package org.shaolin.bmdp.analyzer.ce;
import java.util.*;
import org.shaolin.bmdp.runtime.ce.IConstantEntity;
import org.shaolin.bmdp.runtime.ce.AbstractConstant;

/**
 * 
 * entityName: org.shaolin.bmdp.analyzer.ce.JavaCCJobStatusType
 *
 */
public final class JavaCCJobStatusType extends AbstractConstant
{
    public static final String ENTITY_NAME = "org.shaolin.bmdp.analyzer.ce.JavaCCJobStatusType";
    
    protected static final long serialVersionUID = 0x811b9115811b9115L;
    private static String i18nBundle = "org_shaolin_bmdp_analyzer_i18n";

    //User-defined constant define

    public static final JavaCCJobStatusType NOT_SPECIFIED = new JavaCCJobStatusType(CONSTANT_DEFAULT_VALUE, -1, null, null, null, null, false);

    public static final JavaCCJobStatusType STOP = new JavaCCJobStatusType("Stop", 0, "org.shaolin.bmdp.analyzer.ce.JavaCCJobStatusType.STOP", "Stop", null, null,false);

    public static final JavaCCJobStatusType START = new JavaCCJobStatusType("Start", 1, "org.shaolin.bmdp.analyzer.ce.JavaCCJobStatusType.START", "Start", null, null,false);

    //End of constant define

    //Common constant define
    public JavaCCJobStatusType()
    {
        constantList.add(NOT_SPECIFIED);
        constantList.add(STOP);
        constantList.add(START);
    }

    private JavaCCJobStatusType(long id, String value, int intValue, String i18nKey, String description)
    {
        super(id, value, intValue, i18nKey, description);
    }
    
    private JavaCCJobStatusType(String value, int intValue, String i18nKey,
        String description, Date effTime, Date expTime)
    {
        super(value, intValue, i18nKey, description, effTime, expTime);
    }

    private JavaCCJobStatusType(String value, int intValue, String i18nKey,
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
        return new JavaCCJobStatusType(value, intValue, i18nKey,
            description, effTime, expTime);
    }

    protected String getTypeEntityName()
    {
        return ENTITY_NAME;
    }

}

