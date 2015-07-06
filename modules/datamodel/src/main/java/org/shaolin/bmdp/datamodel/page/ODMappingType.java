//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.20 at 04:07:50 PM CST 
//


package org.shaolin.bmdp.datamodel.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.shaolin.bmdp.datamodel.common.EntityType;
import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.datamodel.common.ParamType;


/**
 * <p>Java class for ODMappingType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ODMappingType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://bmdp.shaolin.org/datamodel/Common}EntityType">
 *       &lt;sequence>
 *         &lt;element name="UIEntity" type="{http://bmdp.shaolin.org/datamodel/Common}ParamType" maxOccurs="unbounded"/>
 *         &lt;element name="DataEntity" type="{http://bmdp.shaolin.org/datamodel/Common}ParamType" maxOccurs="unbounded"/>
 *         &lt;element name="ComponentMapping" type="{http://bmdp.shaolin.org/datamodel/Page}ComponentMappingType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DataToUIMappingOperation" type="{http://bmdp.shaolin.org/datamodel/Common}ExpressionType"/>
 *         &lt;element name="UIToDataMappingOperation" type="{http://bmdp.shaolin.org/datamodel/Common}ExpressionType"/>
 *         &lt;element name="DataLocale" type="{http://bmdp.shaolin.org/datamodel/Common}ExpressionType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ODMappingType", propOrder = {
    "uiEntities",
    "dataEntities",
    "componentMappings",
    "dataToUIMappingOperation",
    "uiToDataMappingOperation",
    "dataLocale"
})
public class ODMappingType
    extends EntityType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "UIEntity", required = true)
    protected List<ParamType> uiEntities;
    @XmlElement(name = "DataEntity", required = true)
    protected List<ParamType> dataEntities;
    @XmlElement(name = "ComponentMapping")
    protected List<ComponentMappingType> componentMappings;
    @XmlElement(name = "DataToUIMappingOperation", required = true)
    protected ExpressionType dataToUIMappingOperation;
    @XmlElement(name = "UIToDataMappingOperation", required = true)
    protected ExpressionType uiToDataMappingOperation;
    @XmlElement(name = "DataLocale")
    protected ExpressionType dataLocale;

    /**
     * Gets the value of the uiEntities property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uiEntities property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUIEntities().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParamType }
     * 
     * 
     */
    public List<ParamType> getUIEntities() {
        if (uiEntities == null) {
            uiEntities = new ArrayList<ParamType>();
        }
        return this.uiEntities;
    }

    /**
     * Gets the value of the dataEntities property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataEntities property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataEntities().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParamType }
     * 
     * 
     */
    public List<ParamType> getDataEntities() {
        if (dataEntities == null) {
            dataEntities = new ArrayList<ParamType>();
        }
        return this.dataEntities;
    }

    /**
     * Gets the value of the componentMappings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the componentMappings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComponentMappings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComponentMappingType }
     * 
     * 
     */
    public List<ComponentMappingType> getComponentMappings() {
        if (componentMappings == null) {
            componentMappings = new ArrayList<ComponentMappingType>();
        }
        return this.componentMappings;
    }

    /**
     * Gets the value of the dataToUIMappingOperation property.
     * 
     * @return
     *     possible object is
     *     {@link ExpressionType }
     *     
     */
    public ExpressionType getDataToUIMappingOperation() {
        return dataToUIMappingOperation;
    }

    /**
     * Sets the value of the dataToUIMappingOperation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExpressionType }
     *     
     */
    public void setDataToUIMappingOperation(ExpressionType value) {
        this.dataToUIMappingOperation = value;
    }

    /**
     * Gets the value of the uiToDataMappingOperation property.
     * 
     * @return
     *     possible object is
     *     {@link ExpressionType }
     *     
     */
    public ExpressionType getUIToDataMappingOperation() {
        return uiToDataMappingOperation;
    }

    /**
     * Sets the value of the uiToDataMappingOperation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExpressionType }
     *     
     */
    public void setUIToDataMappingOperation(ExpressionType value) {
        this.uiToDataMappingOperation = value;
    }

    /**
     * Gets the value of the dataLocale property.
     * 
     * @return
     *     possible object is
     *     {@link ExpressionType }
     *     
     */
    public ExpressionType getDataLocale() {
        return dataLocale;
    }

    /**
     * Sets the value of the dataLocale property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExpressionType }
     *     
     */
    public void setDataLocale(ExpressionType value) {
        this.dataLocale = value;
    }

}
