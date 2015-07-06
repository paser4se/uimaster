//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.07.27 at 02:36:42 PM CST 
//


package org.shaolin.bmdp.datamodel.common;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * entityName may be a constant("Empty") which it indicates undefined. if no typed any words, it would indicate templateRef has been a parent template. when got reference object with returning defined entity of parent.
 * 
 * <p>Java class for TargetEntityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TargetEntityType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://bmdp.shaolin.org/datamodel/Common}ItemRefType">
 *       &lt;sequence>
 *         &lt;element name="implRuleTemplate" type="{http://bmdp.shaolin.org/datamodel/Common}TargetEntityType" minOccurs="0"/>
 *         &lt;element name="MatchedEntity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="entityName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="systemVersion" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetEntityType", propOrder = {
    "implRuleTemplate",
    "matchedEntity"
})
public class TargetEntityType
    extends ItemRefType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected TargetEntityType implRuleTemplate;
    @XmlElement(name = "MatchedEntity")
    protected String matchedEntity;
    @XmlAttribute(name = "entityName", required = true)
    protected String entityName;
    @XmlAttribute(name = "systemVersion")
    protected Integer systemVersion;

    /**
     * Gets the value of the implRuleTemplate property.
     * 
     * @return
     *     possible object is
     *     {@link TargetEntityType }
     *     
     */
    public TargetEntityType getImplRuleTemplate() {
        return implRuleTemplate;
    }

    /**
     * Sets the value of the implRuleTemplate property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetEntityType }
     *     
     */
    public void setImplRuleTemplate(TargetEntityType value) {
        this.implRuleTemplate = value;
    }

    /**
     * Gets the value of the matchedEntity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatchedEntity() {
        return matchedEntity;
    }

    /**
     * Sets the value of the matchedEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatchedEntity(String value) {
        this.matchedEntity = value;
    }

    /**
     * Gets the value of the entityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * Sets the value of the entityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntityName(String value) {
        this.entityName = value;
    }

    /**
     * Gets the value of the systemVersion property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSystemVersion() {
        return systemVersion;
    }

    /**
     * Sets the value of the systemVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSystemVersion(Integer value) {
        this.systemVersion = value;
    }

}
