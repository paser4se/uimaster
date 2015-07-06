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
import javax.xml.bind.annotation.XmlType;
import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.datamodel.common.ParamType;


/**
 * 
 * 				dest node with a filter for condition or split.
 * 			
 * 
 * <p>Java class for destWithFilterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="destWithFilterType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://bmdp.shaolin.org/datamodel/Workflow}destType">
 *       &lt;sequence>
 *         &lt;element name="var" type="{http://bmdp.shaolin.org/datamodel/Common}ParamType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="expression" type="{http://bmdp.shaolin.org/datamodel/Common}ExpressionType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="bean" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="var" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "destWithFilterType", propOrder = {
    "vars",
    "expression"
})
public class DestWithFilterType
    extends DestType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "var")
    protected List<ParamType> vars;
    protected ExpressionType expression;
    @XmlAttribute(name = "bean")
    protected String bean;
    @XmlAttribute(name = "var")
    protected String var;

    /**
     * Gets the value of the vars property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vars property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVars().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParamType }
     * 
     * 
     */
    public List<ParamType> getVars() {
        if (vars == null) {
            vars = new ArrayList<ParamType>();
        }
        return this.vars;
    }

    /**
     * Gets the value of the expression property.
     * 
     * @return
     *     possible object is
     *     {@link ExpressionType }
     *     
     */
    public ExpressionType getExpression() {
        return expression;
    }

    /**
     * Sets the value of the expression property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExpressionType }
     *     
     */
    public void setExpression(ExpressionType value) {
        this.expression = value;
    }

    /**
     * Gets the value of the bean property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBean() {
        return bean;
    }

    /**
     * Sets the value of the bean property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBean(String value) {
        this.bean = value;
    }

    /**
     * Gets the value of the var property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVar() {
        return var;
    }

    /**
     * Sets the value of the var property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVar(String value) {
        this.var = value;
    }

}
