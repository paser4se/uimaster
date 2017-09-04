<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="java.text.MessageFormat" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.shaolin.uimaster.page.flow.error.WebflowError" %>
<%@ page import="org.shaolin.uimaster.page.flow.error.WebflowErrors" %>
<%@ page import="org.shaolin.bmdp.exceptions.BaseRuntimeException" %>
<%@ page import="org.shaolin.uimaster.page.flow.WebflowConstants" %>
<%@ page import="org.shaolin.bmdp.i18n.ResourceUtil" %>
<%
    // clear timestamp set
    session.removeAttribute("_timestamp");

WebflowErrors errors = (WebflowErrors)request.getAttribute(WebflowConstants.ERROR_KEY);
if(errors != null && !errors.empty())
{
        for (Iterator keys = errors.properties(); keys.hasNext();)
        {
            String prop = (String)keys.next();
            for (Iterator it = errors.get(prop); it.hasNext();)
            {
                WebflowError error = (WebflowError)it.next();
                Throwable t = error.getThrowable();
                if (t != null)
                {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    t.printStackTrace(pw);
                    pw.flush();

                    out.println("<pre> Exception stackTrace: "
                            + sw.toString() + "</pre>");
                }
            }
        }
}
%>