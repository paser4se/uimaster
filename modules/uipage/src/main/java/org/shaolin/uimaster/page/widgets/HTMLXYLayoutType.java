package org.shaolin.uimaster.page.widgets;

import java.io.IOException;

import org.shaolin.uimaster.page.HTMLSnapshotContext;
import org.shaolin.uimaster.page.cache.UIFormObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLXYLayoutType extends HTMLLayoutType
{
	private static final Logger logger = LoggerFactory.getLogger(HTMLXYLayoutType.class);

    public HTMLXYLayoutType()
    {
    }

    public HTMLXYLayoutType(HTMLSnapshotContext context)
    {
        super(context);
    }

	public HTMLXYLayoutType(HTMLSnapshotContext context, String id)
	{
	    super(context, id);
	}

    public void generateBeginHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth)
    {
        HTMLWidgetType parentComponent = (HTMLWidgetType) context.getRequest().
                getAttribute(HTMLSnapshotContext.REQUEST_PARENT_TAG_KEY);
        if (parentComponent != null)
        {
            HTMLLayoutType htmlLayout = parentComponent.getHTMLLayout();
            if (htmlLayout instanceof HTMLXYLayoutType)
            {
                String printX = (String) getAttribute("printX");
                String printY = (String) getAttribute("printY");
                String parentX = (String) htmlLayout.getAttribute("printX");
                String parentY = (String) htmlLayout.getAttribute("printY");
                printX = String.valueOf(Integer.parseInt(printX) + Integer.parseInt(parentX));
                printY = String.valueOf(Integer.parseInt(printY) + Integer.parseInt(parentY));
                addAttribute("printX", printX);
                addAttribute("printY", printY);
            }
        }
    }

    public void generateEndHTML(HTMLSnapshotContext context, UIFormObject ownerEntity, int depth)
    {
    }

	public void generateAttribute(HTMLSnapshotContext context, String attributeName, Object attributeValue) throws IOException
	{
	    String attrValue = (String)attributeValue;
        if ("align".equals(attributeName))
        {
            if(!"full".equals(attrValue.toLowerCase()))
            {
                context.generateHTML(" align=\"");
                context.generateHTML(attrValue);
                context.generateHTML("\"");
            }
        }
        else if ("valign".equals(attributeName))
        {
            if(!"full".equals(attrValue.toLowerCase()))
            {
                if ("center".equals(attrValue.toLowerCase()))
                {
                    attrValue = "middle";
                }
                context.generateHTML(" vAlign=\"");
                context.generateHTML(attrValue);
                context.generateHTML("\"");
            }
        }
        else if ("cellUIStyle".equals(attributeName))
        {
            context.generateHTML(" class=\"");
            context.generateHTML(attrValue);
            context.generateHTML("\"");
        }
        else if ("rowUIStyle".equals(attributeName))
        {
        }
        else
        {
            super.generateAttribute(context, attributeName, attributeValue);
        }
	}

    public static final String ___REVISION___ = "$Revision: 1.7 $";

    private static final long serialVersionUID = -4010697551493720080L;
}
