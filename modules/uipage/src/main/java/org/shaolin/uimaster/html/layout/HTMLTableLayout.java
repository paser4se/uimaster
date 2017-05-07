package org.shaolin.uimaster.html.layout;

import org.shaolin.bmdp.datamodel.page.UIContainerType;
import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.exception.UIPageException;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;

public class HTMLTableLayout extends HTMLLayout
{
    public HTMLTableLayout(UIContainerType container, UIFormObject entity)
    {
        super(entity);
        
        init(container, null);
    }
    
    public HTMLTableLayout(UIContainerType container, UIFormObject entity, String cellUIStyle)
    {
        super(entity);
        
        init(container, cellUIStyle);
    }
    
    public void generateHTML(UserRequestContext context, int depth, Boolean readOnly, 
            IUISkin uiskinObj, HTMLWidgetType parentComponent, String rowUIStyle) throws UIPageException
    {
        for ( int i = 0, n = layoutComstraintList.size(); i < n; i++ )
        {
            AbstractHTMLLayout layout = (AbstractHTMLLayout)layoutComstraintList.get(i);
            layout.generate(context, depth, readOnly, uiskinObj, parentComponent, rowUIStyle);
        }
        if ( layoutComstraintList.size() != 0 )
        {
            HTMLUtil.generateTab(context, depth);
            context.generateHTML("</tr>");
        }
    }

}
