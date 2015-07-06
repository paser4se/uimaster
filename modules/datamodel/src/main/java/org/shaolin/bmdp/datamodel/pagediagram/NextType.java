//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.08 at 02:15:27 PM CST 
//


package org.shaolin.bmdp.datamodel.pagediagram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.shaolin.bmdp.datamodel.common.NameExpressionType;


/**
 * specified the next node by chunk name, the  node name and OutDataMapping. in OutDataMapping, expression can reference GlobalVariables of current chunk using prefix tag "@",  reference the OutData/variables of the current node using prefix tag "$"
 * 
 * <p>Java class for NextType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NextType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="destNode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="outDataMappingToNode" type="{http://bmdp.shaolin.org/datamodel/Common}NameExpressionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="connectionPoint" type="{http://bmdp.shaolin.org/datamodel/PageDiagram}ConnectionPointType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NextType", propOrder = {
    "destNode",
    "outDataMappingToNodes",
    "connectionPoints"
})
public class NextType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String destNode;
    @XmlElement(name = "outDataMappingToNode")
    protected List<NameExpressionType> outDataMappingToNodes;
    @XmlElement(name = "connectionPoint")
    protected List<ConnectionPointType> connectionPoints;
    @XmlAttribute(name = "description")
    protected String description;

    /**
     * Gets the value of the destNode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestNode() {
        return destNode;
    }

    /**
     * Sets the value of the destNode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestNode(String value) {
        this.destNode = value;
    }

    /**
     * Gets the value of the outDataMappingToNodes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the outDataMappingToNodes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOutDataMappingToNodes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NameExpressionType }
     * 
     * 
     */
    public List<NameExpressionType> getOutDataMappingToNodes() {
        if (outDataMappingToNodes == null) {
            outDataMappingToNodes = new ArrayList<NameExpressionType>();
        }
        return this.outDataMappingToNodes;
    }

    /**
     * Gets the value of the connectionPoints property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the connectionPoints property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConnectionPoints().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConnectionPointType }
     * 
     * 
     */
    public List<ConnectionPointType> getConnectionPoints() {
        if (connectionPoints == null) {
            connectionPoints = new ArrayList<ConnectionPointType>();
        }
        return this.connectionPoints;
    }

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

}
