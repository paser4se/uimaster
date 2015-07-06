//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.19 at 12:56:16 PM CST 
//


package org.shaolin.bmdp.datamodel.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Flow is a workflow consists of source and
 * 				destination
 * 				node pairs. A specific node is a bean defined in bean
 * 				configuration file.
 * 			
 * 
 * <p>Java class for flowType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="flowType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="conf" type="{http://bmdp.shaolin.org/datamodel/Workflow}flowConfType" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="node" type="{http://bmdp.shaolin.org/datamodel/Workflow}generalNodeType"/>
 *           &lt;element name="condition" type="{http://bmdp.shaolin.org/datamodel/Workflow}conditionNodeType"/>
 *           &lt;element name="split" type="{http://bmdp.shaolin.org/datamodel/Workflow}splitNodeType"/>
 *           &lt;element name="join" type="{http://bmdp.shaolin.org/datamodel/Workflow}joinNodeType"/>
 *           &lt;element name="child-flow" type="{http://bmdp.shaolin.org/datamodel/Workflow}childFlowNodeType"/>
 *           &lt;element name="start-node" type="{http://bmdp.shaolin.org/datamodel/Workflow}startNodeType"/>
 *           &lt;element name="intermediate-node" type="{http://bmdp.shaolin.org/datamodel/Workflow}intermediateNodeType"/>
 *           &lt;element name="end-node" type="{http://bmdp.shaolin.org/datamodel/Workflow}endNodeType"/>
 *           &lt;element name="timeout-node" type="{http://bmdp.shaolin.org/datamodel/Workflow}timeoutNodeType"/>
 *           &lt;element name="cancel-timeout-node" type="{http://bmdp.shaolin.org/datamodel/Workflow}cancelTimeoutNodeType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="eventConsumer" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "flowType", propOrder = {
    "description",
    "conf",
    "nodesAndConditionsAndSplits"
})
public class FlowType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected String description;
    protected FlowConfType conf;
    @XmlElements({
        @XmlElement(name = "node", type = GeneralNodeType.class),
        @XmlElement(name = "condition", type = ConditionNodeType.class),
        @XmlElement(name = "split", type = SplitNodeType.class),
        @XmlElement(name = "join", type = JoinNodeType.class),
        @XmlElement(name = "child-flow", type = ChildFlowNodeType.class),
        @XmlElement(name = "start-node", type = StartNodeType.class),
        @XmlElement(name = "intermediate-node", type = IntermediateNodeType.class),
        @XmlElement(name = "end-node", type = EndNodeType.class),
        @XmlElement(name = "timeout-node", type = TimeoutNodeType.class),
        @XmlElement(name = "cancel-timeout-node", type = CancelTimeoutNodeType.class)
    })
    protected List<NodeType> nodesAndConditionsAndSplits;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "eventConsumer", required = true)
    protected String eventConsumer;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the conf property.
     * 
     * @return
     *     possible object is
     *     {@link FlowConfType }
     *     
     */
    public FlowConfType getConf() {
        return conf;
    }

    /**
     * Sets the value of the conf property.
     * 
     * @param value
     *     allowed object is
     *     {@link FlowConfType }
     *     
     */
    public void setConf(FlowConfType value) {
        this.conf = value;
    }

    /**
     * Gets the value of the nodesAndConditionsAndSplits property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nodesAndConditionsAndSplits property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNodesAndConditionsAndSplits().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GeneralNodeType }
     * {@link ConditionNodeType }
     * {@link SplitNodeType }
     * {@link JoinNodeType }
     * {@link ChildFlowNodeType }
     * {@link StartNodeType }
     * {@link IntermediateNodeType }
     * {@link EndNodeType }
     * {@link TimeoutNodeType }
     * {@link CancelTimeoutNodeType }
     * 
     * 
     */
    public List<NodeType> getNodesAndConditionsAndSplits() {
        if (nodesAndConditionsAndSplits == null) {
            nodesAndConditionsAndSplits = new ArrayList<NodeType>();
        }
        return this.nodesAndConditionsAndSplits;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the eventConsumer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventConsumer() {
        return eventConsumer;
    }

    /**
     * Sets the value of the eventConsumer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventConsumer(String value) {
        this.eventConsumer = value;
    }

}
