package org.shaolin.uimaster.html.layout;

import org.shaolin.uimaster.page.HTMLUtil;
import org.shaolin.uimaster.page.UserRequestContext;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.shaolin.uimaster.page.exception.UIPageException;
import org.shaolin.uimaster.page.widgets.HTMLCellLayoutType;
import org.shaolin.uimaster.page.widgets.HTMLLayoutType;
import org.shaolin.uimaster.page.widgets.HTMLWidgetType;

public class HTMLEmptyLayout extends AbstractHTMLLayout {
    
    public HTMLEmptyLayout(UIFormObject entity) {
        super(entity);
    }
    
    public void generate(UserRequestContext context, int depth, Boolean readOnly, 
            IUISkin uiskinObj, HTMLWidgetType parentComponent, String rowUIStyle) throws UIPageException
    {
        HTMLLayoutType layout = HTMLUtil.getHTMLLayoutType("", "CellLayoutType");
        ((HTMLCellLayoutType)layout).setContainer(container);
        
        layout.setParentComponent(parentComponent);
        layout.setTableColumnCount(colCount);
        layout.setTableRowCount(rowCount);
        layout.addAttribute("x", String.valueOf(cellX));
        layout.addAttribute("y", String.valueOf(cellY));
        if ( colWidth != null )
        {
            layout.addAttribute("width", colWidth);
        }
        if ( rowHeight != null )
        {
            layout.addAttribute("height", rowHeight);
        }
        layout.addAttribute("cellUIStyle", "");
        if ( newLine && !firstLine )
        {
            HTMLUtil.generateTab(context, depth);
            context.generateHTML("<div class=\"hardreturn\"></div>");
        }
        
        HTMLUtil.generateTab(context, depth);
        layout.generateBeginHTML(context, this.ownerEntity, depth);

        HTMLUtil.generateTab(context, depth + 1);
        context.generateHTML("&nbsp;");

        HTMLUtil.generateTab(context, depth);
        layout.generateEndHTML(context, this.ownerEntity, depth);
    }

}
