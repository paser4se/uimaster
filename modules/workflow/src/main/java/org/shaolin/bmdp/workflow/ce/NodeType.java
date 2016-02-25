/*
 * This code is generated automatically, any change will be replaced after rebuild.
 * Generated on Mon Feb 22 22:39:37 CST 2016
 */

package org.shaolin.bmdp.workflow.ce;
import java.util.*;
import org.shaolin.bmdp.runtime.ce.IConstantEntity;
import org.shaolin.bmdp.runtime.ce.AbstractConstant;

/**
 * 
 * entityName: org.shaolin.bmdp.workflow.ce.NodeType
 *
 */
public final class NodeType extends AbstractConstant
{
    public static final String ENTITY_NAME = "org.shaolin.bmdp.workflow.ce.NodeType";
    
    protected static final long serialVersionUID = 0x811b9115811b9115L;
    private static String i18nBundle = "org_shaolin_bmdp_workflow_i18n";

    //User-defined constant define

    public static final NodeType NOT_SPECIFIED = new NodeType(CONSTANT_DEFAULT_VALUE, -1, null, null, null, null, false);

    public static final NodeType MISSIONNODE = new NodeType("MissionNode", 0, "org.shaolin.bmdp.workflow.ce.NodeType.MISSIONNODE", "MissionNode", null, null,false);

    public static final NodeType STARTNODE = new NodeType("StartNode", 1, "org.shaolin.bmdp.workflow.ce.NodeType.STARTNODE", "StartNode", null, null,false);

    public static final NodeType CONDITIONNODE = new NodeType("ConditionNode", 2, "org.shaolin.bmdp.workflow.ce.NodeType.CONDITIONNODE", "ConditionNode", null, null,false);

    public static final NodeType SLIPTNODE = new NodeType("SliptNode", 3, "org.shaolin.bmdp.workflow.ce.NodeType.SLIPTNODE", "SliptNode", null, null,false);

    public static final NodeType JOINNODE = new NodeType("JoinNode", 4, "org.shaolin.bmdp.workflow.ce.NodeType.JOINNODE", "JoinNode", null, null,false);

    public static final NodeType ENDNODE = new NodeType("EndNode", 5, "org.shaolin.bmdp.workflow.ce.NodeType.ENDNODE", "EndNode", null, null,false);

    //End of constant define

    //Common constant define
    public NodeType()
    {
        constantList.add(NOT_SPECIFIED);
        constantList.add(MISSIONNODE);
        constantList.add(STARTNODE);
        constantList.add(CONDITIONNODE);
        constantList.add(SLIPTNODE);
        constantList.add(JOINNODE);
        constantList.add(ENDNODE);
    }

    private NodeType(long id, String value, int intValue, String i18nKey, String description)
    {
        super(id, value, intValue, i18nKey, description);
    }
    
    private NodeType(String value, int intValue, String i18nKey,
        String description, Date effTime, Date expTime)
    {
        super(value, intValue, i18nKey, description, effTime, expTime);
    }

    private NodeType(String value, int intValue, String i18nKey,
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
        return new NodeType(value, intValue, i18nKey,
            description, effTime, expTime);
    }

    protected String getTypeEntityName()
    {
        return ENTITY_NAME;
    }

}

