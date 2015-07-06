//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.20 at 04:07:50 PM CST 
//


package org.shaolin.bmdp.datamodel.page;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FontPropertyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FontPropertyType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://bmdp.shaolin.org/datamodel/Page}PropertyValueType">
 *       &lt;sequence>
 *         &lt;element name="fontName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fontSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="fontStyle" type="{http://bmdp.shaolin.org/datamodel/Page}FontStyleType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FontPropertyType", propOrder = {
    "fontName",
    "fontSize",
    "fontStyle"
})
public class FontPropertyType
    extends PropertyValueType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String fontName;
    protected int fontSize;
    @XmlElement(required = true)
    protected FontStyleType fontStyle;

    /**
     * Gets the value of the fontName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * Sets the value of the fontName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFontName(String value) {
        this.fontName = value;
    }

    /**
     * Gets the value of the fontSize property.
     * 
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Sets the value of the fontSize property.
     * 
     */
    public void setFontSize(int value) {
        this.fontSize = value;
    }

    /**
     * Gets the value of the fontStyle property.
     * 
     * @return
     *     possible object is
     *     {@link FontStyleType }
     *     
     */
    public FontStyleType getFontStyle() {
        return fontStyle;
    }

    /**
     * Sets the value of the fontStyle property.
     * 
     * @param value
     *     allowed object is
     *     {@link FontStyleType }
     *     
     */
    public void setFontStyle(FontStyleType value) {
        this.fontStyle = value;
    }

}
