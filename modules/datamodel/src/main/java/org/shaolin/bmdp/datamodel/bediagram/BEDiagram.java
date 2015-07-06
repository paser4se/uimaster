//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.07.27 at 02:34:46 PM CST 
//


package org.shaolin.bmdp.datamodel.bediagram;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.shaolin.bmdp.datamodel.common.DiagramType;


/**
 * <p>Java class for BEDiagramType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BEDiagramType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://bmdp.shaolin.org/datamodel/Common}DiagramType">
 *       &lt;sequence>
 *         &lt;element name="diagramPackage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="bePackage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cePackage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="beEntity" type="{http://bmdp.shaolin.org/datamodel/BEDiagram}BusinessEntityType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ceEntity" type="{http://bmdp.shaolin.org/datamodel/BEDiagram}ConstantEntityType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BEDiagramType", propOrder = {
    "diagramPackage",
    "bePackage",
    "cePackage",
    "beEntities",
    "ceEntities"
})
@XmlRootElement(name = "BEDiagram")
public class BEDiagram
    extends DiagramType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String diagramPackage;
    @XmlElement(required = true)
    protected String bePackage;
    @XmlElement(required = true)
    protected String cePackage;
    @XmlElement(name = "beEntity")
    protected List<BusinessEntityType> beEntities;
    @XmlElement(name = "ceEntity")
    protected List<ConstantEntityType> ceEntities;

    /**
     * Gets the value of the diagramPackage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiagramPackage() {
        return diagramPackage;
    }

    /**
     * Sets the value of the diagramPackage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiagramPackage(String value) {
        this.diagramPackage = value;
    }

    /**
     * Gets the value of the bePackage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBePackage() {
        return bePackage;
    }

    /**
     * Sets the value of the bePackage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBePackage(String value) {
        this.bePackage = value;
    }

    /**
     * Gets the value of the cePackage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCePackage() {
        return cePackage;
    }

    /**
     * Sets the value of the cePackage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCePackage(String value) {
        this.cePackage = value;
    }

    /**
     * Gets the value of the beEntities property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the beEntities property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBeEntities().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BusinessEntityType }
     * 
     * 
     */
    public List<BusinessEntityType> getBeEntities() {
        if (beEntities == null) {
            beEntities = new ArrayList<BusinessEntityType>();
        }
        return this.beEntities;
    }

    /**
     * Gets the value of the ceEntities property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ceEntities property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCeEntities().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConstantEntityType }
     * 
     * 
     */
    public List<ConstantEntityType> getCeEntities() {
        if (ceEntities == null) {
            ceEntities = new ArrayList<ConstantEntityType>();
        }
        return this.ceEntities;
    }

}
