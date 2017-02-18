<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.shaolin.uimaster.page.flow.error.WebflowError" %>
<%@ page import="org.shaolin.uimaster.page.flow.error.WebflowErrors" %>
<%@ page import="org.shaolin.bmdp.exceptions.I18NRuntimeException" %>
<%@ page import="org.shaolin.uimaster.page.flow.WebflowConstants" %>
<%@ page import="org.shaolin.bmdp.i18n.ResourceUtil" %>
<%String webRoot="/uimaster";%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=webRoot%>/css/main.css" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="x-ua-compatible" content="ie=7">
<meta name="viewport" id="WebViewport" content="width=device-width,initial-scale=1.0,minimum-scale=0.5,maximum-scale=1.0,user-scalable=1">
<meta name="apple-mobile-web-app-title" content="UIMaster">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
<meta name="format-detection" content="telephone=no">
<script language="javascript" src="<%=webRoot%>/js/common.js"></script>
<script language="javascript" src="<%=webRoot%>/common_js/common.js"></script>
<link rel="stylesheet" href="<%=webRoot%>/css/main.css" type="text/css">
<link rel="stylesheet" href="<%=webRoot%>/css/controls.css" type="text/css">
<script language="javascript">
function initPage()
{
}
</script>
</head>

<body onload="initPage();">
<% long suffix = System.currentTimeMillis();%>
<DIV id="uIPanel1Root.titlePanel" class=table-tp name="uIPanel1Root">
<DIV class=title-tp>
<DIV style="WIDTH: 20px; Height: 23px; FLOAT: left">&nbsp;</DIV>
<DIV style="FLOAT: left; CLEAR: right">Error Log</DIV>
</DIV>
<DIV class=content-tp>
<DIV id="uIPanel1Root.wrapperPanel">
	<DIV style="width:100%;">
	<DIV style="width:50px;FLOAT: left">
	<img src="<%=webRoot%>/images/Error.png" ></img>
	</DIV>
	<DIV style="width:100%;vertical-align:bottom;padding-top:10px;padding-bottom:10px;">
	<%
	WebflowErrors errorArray = (WebflowErrors)request.getAttribute(WebflowConstants.ERROR_KEY);
	if(errorArray != null && !errorArray.empty())
	{
        for (Iterator keys = errorArray.properties(); keys.hasNext();)
        {
            String prop = (String)keys.next();
            for (Iterator it = errorArray.get(prop); it.hasNext();)
            {
                WebflowError error = (WebflowError)it.next();
                String errorKey = error.getKey();
                //String msg = (errorKey == null) ? null : MessageFormat.format(errorKey, error.getValues());
				out.println(ResourceUtil.getResource(null,"Errors","ERROR_REASON")+prop+": "+errorKey);
                Throwable currThrowable = error.getThrowable();
                if(currThrowable instanceof I18NRuntimeException)
                {
                	String errorCode = ((I18NRuntimeException)currThrowable).getReason();
                	out.println("<br>"+ResourceUtil.getResource(null,"Errors","ERROR_CODE")+errorCode);
                }
            }
        }
	}
	%>
	</DIV> 
	</DIV>

</DIV>
</DIV>
</DIV>

</body>
</html>
