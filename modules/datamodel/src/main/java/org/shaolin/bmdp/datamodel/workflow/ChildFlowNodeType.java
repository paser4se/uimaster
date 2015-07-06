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
import org.shaolin.bmdp.datamodel.common.NameExpressionType;


/**
 * 
 * 				A child-flow is a logic flow inside its parent. The
 * 				start attribute specifies which node in the child-flow
 * 				will be
 * 				invoked when the IEvent is passed to.The flow
 * 				attribute refers the
 * 				flow definition.
 * 			
 * 
 * <p>Java class for childFlowNodeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="childFlowNodeType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://bmdp.shaolin.org/datamodel/Workflow}generalNodeType">
 *       &lt;sequence>
 *         &lt;element name="app" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inputMapping" type="{http://bmdp.shaolin.org/datamodel/Common}NameExpressionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ouputMapping" type="{http://bmdp.shaolin.org/datamodel/Common}NameExpressionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="postProcess" type="{http://bmdp.shaolin.org/datamodel/Workflow}handlerType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="start" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="flow" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "childFlowNodeType", propOrder = {
    "app",
    "inputMappings",
    "ouputMappings",
    "postProcess"
})
public class ChildFlowNodeType
    extends GeneralNodeType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected String app;
    @XmlElement(name = "inputMapping")
    protected List<NameExpressionType> inputMappings;
    @XmlElement(name = "ouputMapping")
    protected List<NameExpressionType> ouputMappings;
    protected HandlerType postProcess;
    @XmlAttribute(name = "start", required = true)
    protected String start;
    @XmlAttribute(name = "flow", required = true)
    protected String flow;

    /**
     * Gets the value of the app property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApp() {
        return app;
    }

    /**
     * Sets the value of the app property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApp(String value) {
        this.app = value;
    }

    /**
     * Gets the value of the inputMappings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inputMappings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInputMappings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NameExpressionType }
     * 
     * 
     */
    public List<NameExpressionType> getInputMappings() {
        if (inputMappings == null) {
            inputMappings = new ArrayList<NameExpressionType>();
        }
        return this.inputMappings;
    }

    /**
     * Gets the value of the ouputMappings property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ouputMappings property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOuputMappings().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NameExpressionType }
     * 
     * 
     */
    public List<NameExpressionType> getOuputMappings() {
        if (ouputMappings == null) {
            ouputMappings = new ArrayList<NameExpressionType>();
        }
        return this.ouputMappings;
    }

    /**
     * Gets the value of the postProcess property.
     * 
     * @return
     *     possible object is
     *     {@link HandlerType }
     *     
     */
    public HandlerType getPostProcess() {
        return postProcess;
    }

    /**
     * Sets the value of the postProcess property.
     * 
     * @param value
     *     allowed object is
     *     {@link HandlerType }
     *     
     */
    public void setPostProcess(HandlerType value) {
        this.postProcess = value;
    }

    /**
     * Gets the value of the start property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStart(String value) {
        this.start = value;
    }

    /**
     * Gets the value of the flow property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlow() {
        return flow;
    }

    /**
     * Sets the value of the flow property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlow(String value) {
        this.flow = value;
    }

}
