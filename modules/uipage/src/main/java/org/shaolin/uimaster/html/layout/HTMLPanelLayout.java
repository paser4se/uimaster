package org.shaolin.uimaster.html.layout;

import org.apache.log4j.Logger;
import org.shaolin.bmdp.datamodel.page.UIPanelType;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.exception.UIPageException;
import org.shaolin.uimaster.page.widgets.HTMLLayoutType;
import org.shaolin.uimaster.page.widgets.HTMLPanelType;

public class HTMLPanelLayout extends HTMLComponentLayout
{
    private static Logger logger = Logger.getLogger(HTMLPanelLayout.class);

    private HTMLCellLayout layout = null;
    
    /**
     * @param UIID pay attention to this UIID which is pure id defined in the page/form component.
     * @param entity
     */
    public HTMLPanelLayout(String UIID, UIFormObject entity)
    {
        super(UIID, entity);
    }

    public void setBody(UIPanelType panel)
    {
        layout = new HTMLCellLayout(panel, ownerEntity);
        layout.setContainer(UIID + "-");
    }

    public void generateComponentHTML(UserRequestContext context, int depth, 
    		Boolean readOnly, HTMLLayoutType htmlLayout) throws UIPageException
    {
    	Boolean realReadOnly = readOnly;
//        String selfReadOnly = (String)propMap.get("readOnly");
//        if (selfReadOnly == null || selfReadOnly.equals("parent")){
//            realReadOnly = readOnly;
//        } else {
//            realReadOnly = Boolean.valueOf(ee.evaluateReadOnly(selfReadOnly));
//        }
        if (logger.isDebugEnabled()) {
            logger.debug("<---HTMLPanelLayout.generateComponentHTML--->The readOnly value for component: "
                    + UIID
                    + " in the uientity: "
                    + ownerEntity.getName()
                    + " is "
                    + (realReadOnly == null ? "null" : realReadOnly.toString()));
        }

        HTMLPanelType uiPanel = (HTMLPanelType)context.getHtmlWidget(context.getHTMLPrefix() + UIID);
        uiPanel.setReadOnly(realReadOnly);
        uiPanel.setHTMLLayout(htmlLayout);
        
        HTMLUtil.generateTab(context, depth);
        IUISkin uiskinObj = uiPanel.getUISkin();
		if (uiskinObj != null) {
			try {
				uiskinObj.generatePreCode(uiPanel);
			} catch (Exception e) {
				logger.error("uiskin error: ", e);
			}
		} else {
			uiPanel.generateBeginHTML(context, this.ownerEntity, depth);
		}

//        Widget newWidget = uiPanel.createAjaxWidget(ee);
//        if (newWidget != null) {
//        	context.addAjaxWidget(newWidget.getId(), newWidget);
//        }

        layout.generateHTML(context, depth + 1, realReadOnly, uiskinObj,
                uiPanel);

        HTMLUtil.generateTab(context, depth);
		if (uiskinObj != null) {
			try {
				uiskinObj.generatePostCode(uiPanel);
			} catch (Exception e) {
				logger.error("uiskin error: ", e);
			}
        } else {
        	uiPanel.generateEndHTML(context, this.ownerEntity, depth);
        }

        if (componentList != null)
        {
            for (int i = 0, n = componentList.size(); i < n; i++)
            {
                ((HTMLComponentLayout)componentList.get(i)).generateComponentHTML(context, depth,
                        realReadOnly, htmlLayout);
            }
        }
    }

}
