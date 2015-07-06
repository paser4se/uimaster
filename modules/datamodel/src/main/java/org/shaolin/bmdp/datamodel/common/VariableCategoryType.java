//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.07.27 at 02:36:42 PM CST 
//


package org.shaolin.bmdp.datamodel.common;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VariableCategoryType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VariableCategoryType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="JavaPrimitive"/>
 *     &lt;enumeration value="JavaClass"/>
 *     &lt;enumeration value="BusinessEntity"/>
 *     &lt;enumeration value="ConstantEntity"/>
 *     &lt;enumeration value="UIEntity"/>
 *     &lt;enumeration value="JoinTable"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VariableCategoryType")
@XmlEnum
public enum VariableCategoryType {

    @XmlEnumValue("JavaPrimitive")
    JAVA_PRIMITIVE("JavaPrimitive"),
    @XmlEnumValue("JavaClass")
    JAVA_CLASS("JavaClass"),
    @XmlEnumValue("BusinessEntity")
    BUSINESS_ENTITY("BusinessEntity"),
    @XmlEnumValue("ConstantEntity")
    CONSTANT_ENTITY("ConstantEntity"),
    @XmlEnumValue("UIEntity")
    UI_ENTITY("UIEntity"),
    @XmlEnumValue("JoinTable")
    JOIN_TABLE("JoinTable");
    private final String value;

    VariableCategoryType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VariableCategoryType fromValue(String v) {
        for (VariableCategoryType c: VariableCategoryType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
