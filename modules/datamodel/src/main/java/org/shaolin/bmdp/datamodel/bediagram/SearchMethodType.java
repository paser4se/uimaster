//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.07.27 at 02:34:46 PM CST 
//


package org.shaolin.bmdp.datamodel.bediagram;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.shaolin.bmdp.datamodel.common.TargetEntityType;


/**
 * <p>Java class for SearchMethodType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SearchMethodType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="query" type="{http://bmdp.shaolin.org/datamodel/Common}TargetEntityType"/>
 *         &lt;element name="conditionMappingName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchMethodType", propOrder = {
    "name",
    "query",
    "conditionMappingName"
})
public class SearchMethodType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected TargetEntityType query;
    @XmlElement(required = true)
    protected String conditionMappingName;

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
     * Gets the value of the query property.
     * 
     * @return
     *     possible object is
     *     {@link TargetEntityType }
     *     
     */
    public TargetEntityType getQuery() {
        return query;
    }

    /**
     * Sets the value of the query property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetEntityType }
     *     
     */
    public void setQuery(TargetEntityType value) {
        this.query = value;
    }

    /**
     * Gets the value of the conditionMappingName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConditionMappingName() {
        return conditionMappingName;
    }

    /**
     * Sets the value of the conditionMappingName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConditionMappingName(String value) {
        this.conditionMappingName = value;
    }

}
