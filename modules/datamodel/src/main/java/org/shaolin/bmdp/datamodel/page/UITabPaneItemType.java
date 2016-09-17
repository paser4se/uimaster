//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.17 at 10:00:58 PM CST 
//


package org.shaolin.bmdp.datamodel.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UITabPaneItemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UITabPaneItemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="title" type="{http://bmdp.shaolin.org/datamodel/Page}PropertyValueType"/>
 *         &lt;element name="refEntity" type="{http://bmdp.shaolin.org/datamodel/Page}UIReferenceEntityType"/>
 *         &lt;element name="frame" type="{http://bmdp.shaolin.org/datamodel/Page}UIFrameType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="panel" type="{http://bmdp.shaolin.org/datamodel/Page}UIPanelType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="uiid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UITabPaneItemType", propOrder = {
    "title",
    "refEntity",
    "frames",
    "panel"
})
public class UITabPaneItemType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected PropertyValueType title;
    @XmlElement(required = true)
    protected UIReferenceEntityType refEntity;
    @XmlElement(name = "frame")
    protected List<UIFrameType> frames;
    @XmlElement(required = true)
    protected UIPanelType panel;
    @XmlAttribute(name = "uiid")
    protected String uiid;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link PropertyValueType }
     *     
     */
    public PropertyValueType getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertyValueType }
     *     
     */
    public void setTitle(PropertyValueType value) {
        this.title = value;
    }

    /**
     * Gets the value of the refEntity property.
     * 
     * @return
     *     possible object is
     *     {@link UIReferenceEntityType }
     *     
     */
    public UIReferenceEntityType getRefEntity() {
        return refEntity;
    }

    /**
     * Sets the value of the refEntity property.
     * 
     * @param value
     *     allowed object is
     *     {@link UIReferenceEntityType }
     *     
     */
    public void setRefEntity(UIReferenceEntityType value) {
        this.refEntity = value;
    }

    /**
     * Gets the value of the frames property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the frames property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFrames().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UIFrameType }
     * 
     * 
     */
    public List<UIFrameType> getFrames() {
        if (frames == null) {
            frames = new ArrayList<UIFrameType>();
        }
        return this.frames;
    }

    /**
     * Gets the value of the panel property.
     * 
     * @return
     *     possible object is
     *     {@link UIPanelType }
     *     
     */
    public UIPanelType getPanel() {
        return panel;
    }

    /**
     * Sets the value of the panel property.
     * 
     * @param value
     *     allowed object is
     *     {@link UIPanelType }
     *     
     */
    public void setPanel(UIPanelType value) {
        this.panel = value;
    }

    /**
     * Gets the value of the uiid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUiid() {
        return uiid;
    }

    /**
     * Sets the value of the uiid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUiid(String value) {
        this.uiid = value;
    }

}
