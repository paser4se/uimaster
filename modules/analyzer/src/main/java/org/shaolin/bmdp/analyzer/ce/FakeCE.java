/*
 * This code is generated automatically, any change will be replaced after rebuild.
 * Generated on Tue Dec 29 19:46:22 CST 2015
 */

package org.shaolin.bmdp.analyzer.ce;
import java.util.*;
import org.shaolin.bmdp.runtime.ce.IConstantEntity;
import org.shaolin.bmdp.runtime.ce.AbstractConstant;

/**
 * 
 * entityName: org.shaolin.bmdp.analyzer.ce.FakeCE
 *
 */
public final class FakeCE extends AbstractConstant
{
    public static final String ENTITY_NAME = "org.shaolin.bmdp.analyzer.ce.FakeCE";
    
    protected static final long serialVersionUID = 0x811b9115811b9115L;
    private static String i18nBundle = "org_shaolin_bmdp_analyzer_i18n";

    //User-defined constant define

    public static final FakeCE NOT_SPECIFIED = new FakeCE(CONSTANT_DEFAULT_VALUE, -1, null, null, null, null, false);

    //End of constant define

    //Common constant define
    public FakeCE()
    {
        constantList.add(NOT_SPECIFIED);
    }

    private FakeCE(long id, String value, int intValue, String i18nKey, String description)
    {
        super(id, value, intValue, i18nKey, description);
    }
    
    private FakeCE(String value, int intValue, String i18nKey,
        String description, Date effTime, Date expTime)
    {
        super(value, intValue, i18nKey, description, effTime, expTime);
    }

    private FakeCE(String value, int intValue, String i18nKey,
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
        return new FakeCE(value, intValue, i18nKey,
            description, effTime, expTime);
    }

    protected String getTypeEntityName()
    {
        return ENTITY_NAME;
    }

}

