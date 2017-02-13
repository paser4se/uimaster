<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.shaolin.uimaster.page.flow.WebflowConstants" %>
<%@ page import="org.shaolin.bmdp.runtime.security.UserContext" %>
<% String webRoot = "/uimaster";%>
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
	</head>

	<body>
		<% long suffix = System.currentTimeMillis();%>
		<DIV id="uIPanel1Root.titlePanel" class=table-tp name="uIPanel1Root">
			<DIV class=title-tp>
				<DIV style="WIDTH: 20px; Height: 23px; FLOAT: left">&nbsp;
				</DIV>
			</DIV>
			<DIV class=content-tp>
				<DIV id="uIPanel1Root.wrapperPanel">
					<DIV style="width:100%;">
						<DIV style="width:50px;FLOAT: left">
							<img src="<%=webRoot%>/images/Warning.png"></img>
						</DIV>
						<DIV style="width:100%;vertical-align:bottom;padding-top:10px;padding-bottom:10px;">
							对不起，您没有访问此功能的权限!
						</DIV>
						<DIV>
						  <a href="https://www.vogerp.com/uimaster/webflow.do?_chunkname=org.shaolin.bmdp.adminconsole.diagram.WelcomeMainPage&_nodename=Main">返回</a>
						</DIV>
					</DIV>
					<br>

				</DIV>
			</DIV>
		</DIV>
        <%if (session.getAttribute(WebflowConstants.USER_SESSION_KEY) == null) {%>
        <script type="text/javascript">
           window.top.location.href="/uimaster/jsp/index.jsp";
        </script>
        <%}%>
	</body>
</html>
