package org.shaolin.uimaster.html.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.shaolin.bmdp.datamodel.common.ExpressionType;
import org.shaolin.bmdp.datamodel.page.TableLayoutConstraintType;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.exception.UIComponentNotFoundException;
import org.shaolin.uimaster.page.exception.UIPageException;
import org.shaolin.uimaster.page.widgets.HTMLCellLayoutType;
import org.shaolin.uimaster.page.widgets.HTMLLayoutType;
import org.shaolin.uimaster.page.widgets.HTMLReferenceEntityType;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTML Layout is not sharable for all users. Each user has it's owned layouts. 
 * 
 * @author wushaol
 *
 */
public class HTMLComponentLayout extends AbstractHTMLLayout
{
    private static Logger logger = LoggerFactory.getLogger(HTMLComponentLayout.class);
    
    private int colSpan;
    
    private int rowSpan;
    
    protected String UIID = null;
    
    private Map<String, Object> attributeMap;
    
    protected List componentList;

    protected Map<String, ExpressionType> expMap;
    
    private boolean isContainer = false;
    
    public HTMLComponentLayout(String UIID, UIFormObject entity)
    {
        super(entity);
        String tempId = (UIID.lastIndexOf('.') != -1)? UIID.substring(UIID.lastIndexOf('.')+1): UIID;
        this.UIID = tempId;
        expMap = ownerEntity.getComponentExpression(tempId);
    }
    
    public String getUIID()
    {
        return UIID;
    }
    
    public void setIsContainer(boolean isContainer)
    {
        this.isContainer = isContainer;
    }
    
    /*
     * Do layout constraint transfer for each component, just copy value
     * to corresponding attribute.
     *
     */
    public void setConstraints(TableLayoutConstraintType constraint)
    {
        attributeMap = new HashMap<String, Object>();
        
        this.cellX = constraint.getX();
        this.cellY = constraint.getY();
        this.colSpan = constraint.getWidth();
        if ( colSpan > 1 )
        {
            attributeMap.put("colSpan", String.valueOf(colSpan));
        }
        this.rowSpan = constraint.getHeight();
        if ( rowSpan > 1 )
        {
            attributeMap.put("rowSpan", String.valueOf(rowSpan));
        }
        if ( constraint.getAlign() != null )
        {
            String align = constraint.getAlign().toString();
            if ( align != null && !align.equals("full"))
            {
                attributeMap.put("align", align);
            }
        }
        if ( constraint.getValign() != null )
        {
            String valign = constraint.getValign().toString();
            if ( valign != null && valign.equals("center") )
            {
                valign = "middle";
            }
            if ( valign != null && !valign.equals("full"))
            {
                attributeMap.put("valign", valign);
            }
        }
        String cellUIStyle = constraint.getCellUIStyle();
        if ( cellUIStyle != null && !cellUIStyle.equals("") )
        {
            attributeMap.put("cellUIStyle", cellUIStyle);
        }
        else
        {
            attributeMap.put("cellUIStyle", "");
        }
        String cellUIClass = constraint.getCellUIClass();
        if ( cellUIClass != null && !cellUIClass.equals(""))
        {
            attributeMap.put("cellUIClass", cellUIClass);
        }
        String bgColor = constraint.getBgColor();
        if ( bgColor != null && !bgColor.equals(""))
        {
            attributeMap.put("bgColor", bgColor);
        }
    }

    public void addComponent(HTMLComponentLayout component)
    {
        if ( componentList == null )
        {
            componentList = new ArrayList();
        }
        componentList.add(component);
        if ( component.isContainer )
        {
            this.isContainer = true;
        }
    }
    
    public void removeUsed(Map componentMap)
    {
        componentMap.remove(UIID);
        if ( componentList != null )
        {
            for ( int i = 0, length = componentList.size(); i < length; i++ )
            {
                HTMLComponentLayout comp = (HTMLComponentLayout)componentList.get(i);
                comp.removeUsed(componentMap);
            }
        }
    }

    public int getColSpan()
    {
        return colSpan;
    }

    public int getRowSpan()
    {
        return rowSpan;
    }
    
    public void setTDAttribute(String name, String value)
    {
        attributeMap.put(name, value);
    }
    
    public void generate(UserRequestContext context, int depth, Boolean readOnly, 
            IUISkin uiskinObj, HTMLWidgetType parentComponent, String rowUIStyle) throws UIPageException
    {
    	HTMLCellLayoutType layout = (HTMLCellLayoutType)HTMLUtil.getHTMLLayoutType("", "CellLayoutType");
        layout.setContainer(container);
        layout.setIsContainer(isContainer);

        layout.setParentComponent(parentComponent);
        layout.setTableColumnCount(colCount);
        layout.setTableRowCount(rowCount);
        
        attributeMap.put("x", String.valueOf(cellX));
        attributeMap.put("y", String.valueOf(cellY));
        if (colWidth != null)
        {
        	attributeMap.put("width", colWidth);
        }
        if (rowHeight != null)
        {
        	attributeMap.put("height", rowHeight);
        }
        if (uiskinObj != null)
        {
        	attributeMap.putAll(uiskinObj.getAttributeMap(layout));
        }
        layout.setAttribute(attributeMap);
        
        //TODO:
//        String compId = context.getHTMLPrefix() + UIID;
//        String securityVisible = null;
//        ComponentPermission cp = context.getCompPermission(compId);
//        
//        String[] viewPermission = HTMLUtil.getViewPermission(cp, ee, propMap, expMap, (Map)appendMap.get(UIID));
//        
//        if ((viewPermission != null) && (viewPermission.length > 0) && !(HTMLUtil.checkViewPermission(viewPermission)))
//        {
//            //configured view permission and owns no view permission
//            logger.debug("the user doesn't own the permission to view the component: " + compId);
//            securityVisible = "false";
//            layout.addAttribute("visible",securityVisible);
//            context.enterValueMask();
//        }
        if ( newLine && !firstLine )
        {
            HTMLUtil.generateTab(context, depth);
            context.generateHTML("<div class=\"hardreturn\">&nbsp;</div>");
        }
        
        HTMLUtil.generateTab(context, depth);
        layout.generateBeginHTML(context, this.ownerEntity, depth);
        
        generateComponentHTML(context, depth + 1, readOnly, layout);
        
        HTMLUtil.generateTab(context, depth);
        layout.generateEndHTML(context, this.ownerEntity, depth);
        
        //TODO:
//        if(securityVisible != null)
//        {
//            context.leaveValueMask();
//        }
    }
    
    public void generateComponentHTML(UserRequestContext context, int depth,
    		 Boolean readOnly, HTMLLayoutType htmlLayout) throws UIPageException
    {
    	HTMLUtil.generateTab(context, depth);
    	
        Object selfReadOnly = context.getAttribute(this.getUIID(), "readOnly");
        Boolean realReadOnly = null;
		if (selfReadOnly == null || selfReadOnly.equals("parent")) {
			realReadOnly = readOnly;
		} else if (selfReadOnly.equals("self")) {
			realReadOnly = null;
		} else {
			realReadOnly = Boolean.parseBoolean(selfReadOnly.toString());
		}
        if ( logger.isTraceEnabled() ) {
        	String flag = (realReadOnly == null ? "null" : realReadOnly.toString());
        	if (flag.equals("true")) {
	            logger.trace("The readOnly value for component: {} in the uientity: {} is true",
	                    new Object[] {UIID, ownerEntity.getName()});
        	}
        }
        HTMLWidgetType htmlComponent = context.getHtmlWidget(context.getHTMLPrefix() + UIID);
        if (htmlComponent == null) {
        	throw new UIComponentNotFoundException(context.getHTMLPrefix() + UIID);
        }
        htmlComponent.setHTMLLayout(htmlLayout);
        htmlComponent.setReadOnly(realReadOnly);
        
        if (htmlComponent.getClass() == HTMLReferenceEntityType.class) {
            ((HTMLReferenceEntityType)htmlComponent).setReconfiguration(
                    ownerEntity.getReconfigurationMap(UIID), null, null);
        }
        IUISkin uiskinObj = htmlComponent.getUISkin();
        if (uiskinObj != null) {
			try {
				uiskinObj.generatePreCode(htmlComponent);
			} catch (Exception e) {
				logger.error("uiskin error: ", e);
			}
        } 
        
//        Widget newWidget = htmlComponent.createAjaxWidget(ee);
//        if ( newWidget != null ) {
//            context.addAjaxWidget(newWidget.getId(), newWidget);
//            if (htmlComponent.getClass() == HTMLButtonType.class) {
//            	// all express must be re-calculate when click button in every time.
//        		((Button)newWidget).setExpressMap(expMap);
//        	}
//        }
        
		if (uiskinObj != null) {
			try {
				uiskinObj.generatePostCode(htmlComponent);
			} catch (Exception e) {
				logger.error("uiskin error: ", e);
			}
		} else {
			htmlComponent.generateBeginHTML(context, this.ownerEntity, depth);
            htmlComponent.generateEndHTML(context, this.ownerEntity, depth);
		}
        if ( componentList != null )
        {
            for ( int i = 0, n = componentList.size(); i < n; i++ )
            {
                HTMLComponentLayout component=(HTMLComponentLayout)componentList.get(i);                
                component.generateComponentHTML(context, depth, realReadOnly, htmlLayout);
            }
        }
    }

}
