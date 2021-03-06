//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.28 at 04:10:22 PM CST 
//


package org.shaolin.bmdp.designtime.andriod.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for ViewGroup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ViewGroup">
 *   &lt;complexContent>
 *     &lt;extension base="{}View">
 *       &lt;group ref="{}any-view" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;attribute ref="{http://schemas.android.com/apk/res/android}addStatesFromChildren"/>
 *       &lt;attribute ref="{http://schemas.android.com/apk/res/android}alwaysDrawnWithCache"/>
 *       &lt;attribute ref="{http://schemas.android.com/apk/res/android}animateLayoutChanges"/>
 *       &lt;attribute ref="{http://schemas.android.com/apk/res/android}animationCache"/>
 *       &lt;attribute ref="{http://schemas.android.com/apk/res/android}clipChildren"/>
 *       &lt;attribute ref="{http://schemas.android.com/apk/res/android}clipToPadding"/>
 *       &lt;attribute ref="{http://schemas.android.com/apk/res/android}descendantFocusability"/>
 *       &lt;attribute ref="{http://schemas.android.com/apk/res/android}layoutAnimation"/>
 *       &lt;attribute ref="{http://schemas.android.com/apk/res/android}persistentDrawingCache"/>
 *       &lt;attribute ref="{http://schemas.android.com/apk/res/android}splitMotionEvents"/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ViewGroup", propOrder = {
    "glSurfaceViewsAndAdapterViewsAndAdapterViewAnimators"
})
@XmlSeeAlso({
    AdapterView.class,
    SlidingDrawer.class,
    LinearLayout.class,
    GridLayout.class,
    RelativeLayout.class,
    AbsoluteLayout.class,
    FragmentBreadCrumbs.class,
    FrameLayout.class
})
public class ViewGroup
    extends View
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElements({
        @XmlElement(name = "GLSurfaceView", type = GLSurfaceView.class),
        @XmlElement(name = "AdapterView", type = AdapterView.class),
        @XmlElement(name = "AdapterViewAnimator", type = AdapterViewAnimator.class),
        @XmlElement(name = "AutoCompleteTextView", type = AutoCompleteTextView.class),
        @XmlElement(name = "MediaController", type = MediaController.class),
        @XmlElement(name = "TextView", type = TextView.class),
        @XmlElement(name = "FragmentBreadCrumbs", type = FragmentBreadCrumbs.class),
        @XmlElement(name = "AppWidgetHostView", type = AppWidgetHostView.class),
        @XmlElement(name = "GestureOverlayView", type = GestureOverlayView.class),
        @XmlElement(name = "ExtractEditText", type = ExtractEditText.class),
        @XmlElement(name = "KeyboardView", type = KeyboardView.class),
        @XmlElement(name = "RSSurfaceView", type = RSSurfaceView.class),
        @XmlElement(name = "RSTextureView", type = RSTextureView.class),
        @XmlElement(name = "SurfaceView", type = SurfaceView.class),
        @XmlElement(name = "TextureView", type = TextureView.class),
        @XmlElement(name = "ViewGroup", type = ViewGroup.class),
        @XmlElement(name = "ViewStub", type = ViewStub.class),
        @XmlElement(name = "WebView", type = WebView.class),
        @XmlElement(name = "AbsListView", type = AbsListView.class),
        @XmlElement(name = "AbsSeekBar", type = AbsSeekBar.class),
        @XmlElement(name = "AbsSpinner", type = AbsSpinner.class),
        @XmlElement(name = "AbsoluteLayout", type = AbsoluteLayout.class),
        @XmlElement(name = "AdapterViewFlipper", type = AdapterViewFlipper.class),
        @XmlElement(name = "AnalogClock", type = AnalogClock.class),
        @XmlElement(name = "Button", type = Button.class),
        @XmlElement(name = "CalendarView", type = CalendarView.class),
        @XmlElement(name = "CheckBox", type = CheckBox.class),
        @XmlElement(name = "CheckedTextView", type = CheckedTextView.class),
        @XmlElement(name = "Chronometer", type = Chronometer.class),
        @XmlElement(name = "CompoundButton", type = CompoundButton.class),
        @XmlElement(name = "DatePicker", type = DatePicker.class),
        @XmlElement(name = "DialerFilter", type = DialerFilter.class),
        @XmlElement(name = "DigitalClock", type = DigitalClock.class),
        @XmlElement(name = "EditText", type = EditText.class),
        @XmlElement(name = "ExpandableListView", type = ExpandableListView.class),
        @XmlElement(name = "FrameLayout", type = FrameLayout.class),
        @XmlElement(name = "Gallery", type = Gallery.class),
        @XmlElement(name = "GridLayout", type = GridLayout.class),
        @XmlElement(name = "GridView", type = GridView.class),
        @XmlElement(name = "HorizontalScrollView", type = HorizontalScrollView.class),
        @XmlElement(name = "ImageButton", type = ImageButton.class),
        @XmlElement(name = "ImageSwitcher", type = ImageSwitcher.class),
        @XmlElement(name = "ImageView", type = ImageView.class),
        @XmlElement(name = "LinearLayout", type = LinearLayout.class),
        @XmlElement(name = "ListView", type = ListView.class),
        @XmlElement(name = "MultiAutoCompleteTextView", type = MultiAutoCompleteTextView.class),
        @XmlElement(name = "NumberPicker", type = NumberPicker.class),
        @XmlElement(name = "ProgressBar", type = ProgressBar.class),
        @XmlElement(name = "QuickContactBadge", type = QuickContactBadge.class),
        @XmlElement(name = "RadioButton", type = RadioButton.class),
        @XmlElement(name = "RadioGroup", type = RadioGroup.class),
        @XmlElement(name = "RatingBar", type = RatingBar.class),
        @XmlElement(name = "RelativeLayout", type = RelativeLayout.class),
        @XmlElement(name = "ScrollView", type = ScrollView.class),
        @XmlElement(name = "SearchView", type = SearchView.class),
        @XmlElement(name = "SeekBar", type = SeekBar.class),
        @XmlElement(name = "SlidingDrawer", type = SlidingDrawer.class),
        @XmlElement(name = "Space", type = Space.class),
        @XmlElement(name = "Spinner", type = Spinner.class),
        @XmlElement(name = "StackView", type = StackView.class),
        @XmlElement(name = "Switch", type = Switch.class),
        @XmlElement(name = "TabHost", type = TabHost.class),
        @XmlElement(name = "TabWidget", type = TabWidget.class),
        @XmlElement(name = "TableLayout", type = TableLayout.class),
        @XmlElement(name = "TableRow", type = TableRow.class),
        @XmlElement(name = "TextSwitcher", type = TextSwitcher.class),
        @XmlElement(name = "TimePicker", type = TimePicker.class),
        @XmlElement(name = "ToggleButton", type = ToggleButton.class),
        @XmlElement(name = "TwoLineListItem", type = TwoLineListItem.class),
        @XmlElement(name = "VideoView", type = VideoView.class),
        @XmlElement(name = "ViewAnimator", type = ViewAnimator.class),
        @XmlElement(name = "ViewFlipper", type = ViewFlipper.class),
        @XmlElement(name = "ViewSwitcher", type = ViewSwitcher.class),
        @XmlElement(name = "ZoomButton", type = ZoomButton.class),
        @XmlElement(name = "ZoomControls", type = ZoomControls.class)
    })
    protected List<View> glSurfaceViewsAndAdapterViewsAndAdapterViewAnimators;
    @XmlAttribute(name = "addStatesFromChildren", namespace = "http://schemas.android.com/apk/res/android")
    protected String addStatesFromChildren;
    @XmlAttribute(name = "alwaysDrawnWithCache", namespace = "http://schemas.android.com/apk/res/android")
    protected String alwaysDrawnWithCache;
    @XmlAttribute(name = "animateLayoutChanges", namespace = "http://schemas.android.com/apk/res/android")
    protected String animateLayoutChanges;
    @XmlAttribute(name = "animationCache", namespace = "http://schemas.android.com/apk/res/android")
    protected String animationCache;
    @XmlAttribute(name = "clipChildren", namespace = "http://schemas.android.com/apk/res/android")
    protected String clipChildren;
    @XmlAttribute(name = "clipToPadding", namespace = "http://schemas.android.com/apk/res/android")
    protected String clipToPadding;
    @XmlAttribute(name = "descendantFocusability", namespace = "http://schemas.android.com/apk/res/android")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String descendantFocusability;
    @XmlAttribute(name = "layoutAnimation", namespace = "http://schemas.android.com/apk/res/android")
    protected String layoutAnimation;
    @XmlAttribute(name = "persistentDrawingCache", namespace = "http://schemas.android.com/apk/res/android")
    protected String persistentDrawingCache;
    @XmlAttribute(name = "splitMotionEvents", namespace = "http://schemas.android.com/apk/res/android")
    protected String splitMotionEvents;

    /**
     * Gets the value of the glSurfaceViewsAndAdapterViewsAndAdapterViewAnimators property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the glSurfaceViewsAndAdapterViewsAndAdapterViewAnimators property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGLSurfaceViewsAndAdapterViewsAndAdapterViewAnimators().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GLSurfaceView }
     * {@link AdapterView }
     * {@link AdapterViewAnimator }
     * {@link AutoCompleteTextView }
     * {@link MediaController }
     * {@link TextView }
     * {@link FragmentBreadCrumbs }
     * {@link AppWidgetHostView }
     * {@link GestureOverlayView }
     * {@link ExtractEditText }
     * {@link KeyboardView }
     * {@link RSSurfaceView }
     * {@link RSTextureView }
     * {@link SurfaceView }
     * {@link TextureView }
     * {@link ViewGroup }
     * {@link ViewStub }
     * {@link WebView }
     * {@link AbsListView }
     * {@link AbsSeekBar }
     * {@link AbsSpinner }
     * {@link AbsoluteLayout }
     * {@link AdapterViewFlipper }
     * {@link AnalogClock }
     * {@link Button }
     * {@link CalendarView }
     * {@link CheckBox }
     * {@link CheckedTextView }
     * {@link Chronometer }
     * {@link CompoundButton }
     * {@link DatePicker }
     * {@link DialerFilter }
     * {@link DigitalClock }
     * {@link EditText }
     * {@link ExpandableListView }
     * {@link FrameLayout }
     * {@link Gallery }
     * {@link GridLayout }
     * {@link GridView }
     * {@link HorizontalScrollView }
     * {@link ImageButton }
     * {@link ImageSwitcher }
     * {@link ImageView }
     * {@link LinearLayout }
     * {@link ListView }
     * {@link MultiAutoCompleteTextView }
     * {@link NumberPicker }
     * {@link ProgressBar }
     * {@link QuickContactBadge }
     * {@link RadioButton }
     * {@link RadioGroup }
     * {@link RatingBar }
     * {@link RelativeLayout }
     * {@link ScrollView }
     * {@link SearchView }
     * {@link SeekBar }
     * {@link SlidingDrawer }
     * {@link Space }
     * {@link Spinner }
     * {@link StackView }
     * {@link Switch }
     * {@link TabHost }
     * {@link TabWidget }
     * {@link TableLayout }
     * {@link TableRow }
     * {@link TextSwitcher }
     * {@link TimePicker }
     * {@link ToggleButton }
     * {@link TwoLineListItem }
     * {@link VideoView }
     * {@link ViewAnimator }
     * {@link ViewFlipper }
     * {@link ViewSwitcher }
     * {@link ZoomButton }
     * {@link ZoomControls }
     * 
     * 
     */
    public List<View> getGLSurfaceViewsAndAdapterViewsAndAdapterViewAnimators() {
        if (glSurfaceViewsAndAdapterViewsAndAdapterViewAnimators == null) {
            glSurfaceViewsAndAdapterViewsAndAdapterViewAnimators = new ArrayList<View>();
        }
        return this.glSurfaceViewsAndAdapterViewsAndAdapterViewAnimators;
    }

    /**
     * Gets the value of the addStatesFromChildren property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddStatesFromChildren() {
        return addStatesFromChildren;
    }

    /**
     * Sets the value of the addStatesFromChildren property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddStatesFromChildren(String value) {
        this.addStatesFromChildren = value;
    }

    /**
     * Gets the value of the alwaysDrawnWithCache property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlwaysDrawnWithCache() {
        return alwaysDrawnWithCache;
    }

    /**
     * Sets the value of the alwaysDrawnWithCache property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlwaysDrawnWithCache(String value) {
        this.alwaysDrawnWithCache = value;
    }

    /**
     * Gets the value of the animateLayoutChanges property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnimateLayoutChanges() {
        return animateLayoutChanges;
    }

    /**
     * Sets the value of the animateLayoutChanges property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnimateLayoutChanges(String value) {
        this.animateLayoutChanges = value;
    }

    /**
     * Gets the value of the animationCache property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnimationCache() {
        return animationCache;
    }

    /**
     * Sets the value of the animationCache property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnimationCache(String value) {
        this.animationCache = value;
    }

    /**
     * Gets the value of the clipChildren property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClipChildren() {
        return clipChildren;
    }

    /**
     * Sets the value of the clipChildren property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClipChildren(String value) {
        this.clipChildren = value;
    }

    /**
     * Gets the value of the clipToPadding property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClipToPadding() {
        return clipToPadding;
    }

    /**
     * Sets the value of the clipToPadding property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClipToPadding(String value) {
        this.clipToPadding = value;
    }

    /**
     * Gets the value of the descendantFocusability property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescendantFocusability() {
        return descendantFocusability;
    }

    /**
     * Sets the value of the descendantFocusability property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescendantFocusability(String value) {
        this.descendantFocusability = value;
    }

    /**
     * Gets the value of the layoutAnimation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLayoutAnimation() {
        return layoutAnimation;
    }

    /**
     * Sets the value of the layoutAnimation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLayoutAnimation(String value) {
        this.layoutAnimation = value;
    }

    /**
     * Gets the value of the persistentDrawingCache property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersistentDrawingCache() {
        return persistentDrawingCache;
    }

    /**
     * Sets the value of the persistentDrawingCache property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersistentDrawingCache(String value) {
        this.persistentDrawingCache = value;
    }

    /**
     * Gets the value of the splitMotionEvents property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSplitMotionEvents() {
        return splitMotionEvents;
    }

    /**
     * Sets the value of the splitMotionEvents property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSplitMotionEvents(String value) {
        this.splitMotionEvents = value;
    }

}
