//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.09 at 12:20:13 PM CST 
//


package org.shaolin.bmdp.datamodel.page;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UIFileType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UIFileType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://bmdp.shaolin.org/datamodel/Page}UIComponentType">
 *       &lt;sequence>
 *         &lt;element name="text" type="{http://bmdp.shaolin.org/datamodel/Page}PropertyValueType" minOccurs="0"/>
 *         &lt;element name="isMultiple" type="{http://bmdp.shaolin.org/datamodel/Page}BooleanPropertyType" minOccurs="0"/>
 *         &lt;element name="suffix" type="{http://bmdp.shaolin.org/datamodel/Page}StringPropertyType" minOccurs="0"/>
 *         &lt;element name="storedPath" type="{http://bmdp.shaolin.org/datamodel/Page}PropertyValueType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UIFileType", propOrder = {
    "text",
    "isMultiple",
    "suffix",
    "storedPath"
})
public class UIFileType
    extends UIComponentType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected PropertyValueType text;
    protected BooleanPropertyType isMultiple;
    protected StringPropertyType suffix;
    protected PropertyValueType storedPath;

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link PropertyValueType }
     *     
     */
    public PropertyValueType getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertyValueType }
     *     
     */
    public void setText(PropertyValueType value) {
        this.text = value;
    }

    /**
     * Gets the value of the isMultiple property.
     * 
     * @return
     *     possible object is
     *     {@link BooleanPropertyType }
     *     
     */
    public BooleanPropertyType getIsMultiple() {
        return isMultiple;
    }

    /**
     * Sets the value of the isMultiple property.
     * 
     * @param value
     *     allowed object is
     *     {@link BooleanPropertyType }
     *     
     */
    public void setIsMultiple(BooleanPropertyType value) {
        this.isMultiple = value;
    }

    /**
     * Gets the value of the suffix property.
     * 
     * @return
     *     possible object is
     *     {@link StringPropertyType }
     *     
     */
    public StringPropertyType getSuffix() {
        return suffix;
    }

    /**
     * Sets the value of the suffix property.
     * 
     * @param value
     *     allowed object is
     *     {@link StringPropertyType }
     *     
     */
    public void setSuffix(StringPropertyType value) {
        this.suffix = value;
    }

    /**
     * Gets the value of the storedPath property.
     * 
     * @return
     *     possible object is
     *     {@link PropertyValueType }
     *     
     */
    public PropertyValueType getStoredPath() {
        return storedPath;
    }

    /**
     * Sets the value of the storedPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertyValueType }
     *     
     */
    public void setStoredPath(PropertyValueType value) {
        this.storedPath = value;
    }

}
