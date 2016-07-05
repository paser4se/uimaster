<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page import="java.lang.*,java.io.*" %>
<html>
<head>
<title>IE中自动安装数字证书测试</title>
</head>

<body>
IE中使用XEnroll.InstallPKCS7自动安装根数字证书<br/>
 备注：这里测试的根证书采用Base64编码 X.509格式(CER)<br/>
<%     

StringBuffer server_cert =new StringBuffer();
try {
    String realPath = "/opt/uimaster/apache-tomcat-8.0.9/webapps/uimaster/jsp/vogerp.cer";
    File file = new File(realPath);
    if (!file.exists()) {
        out.println("<HTML><BODY><P>");
        out.println("<h2>根证书文件不存在</h2> <br/>");
        out.println("</P></BODY></HTML>");
        out.flush();
        out.close();
    }else{
        BufferedReader bf=new BufferedReader(new FileReader(file));
        String line=null;
        while((line=bf.readLine())!=null)
            server_cert.append(line);

        bf.close();

    }
}catch(Exception e){
    out.println("<HTML><BODY><P>");
    out.println("<h2>读取证书文件出错</h2> <br/>");
    out.println(e.toString());
    out.println("</P></BODY></HTML>");
    out.flush();
    out.close();
}

String Agent = request.getHeader("User-Agent");
StringTokenizer st = new StringTokenizer(Agent,";");
st.nextToken();
String userBrowser = st.nextToken();
String userOS = st.nextToken();
out.println("你的操作系统为：");
out.println(userOS);
String activexLib="XEnroll";

//检查是否是Windows Vista,Windows 2008,Windows 7,在Vista,Windows 2008，Windows 7上，需要使用 CertEnroll.dll
//Windows 2008 Server, IE7 User-Agent header: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.2;...
//Windows Vista, IE7 User-Agent header: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0;...
//Windows 7,IE8 User-Agent header: Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1;...

if(userOS.equals("Windows NT 6.0") || userOS.equals("Windows NT 6.1")|| userOS.equals("Windows NT 5.2"))
    activexLib="CertEnroll";

String sPKCS7=server_cert.toString();
%>

<% if(activexLib.equals("XEnroll"))
{ %>
    <object id="XEnroll" classid="clsid:127698e4-e730-4e5c-a2b1-21490a70c8a1" codebase="xenroll.dll"></object>
    <SCRIPT language="VBSCRIPT">
        ON ERROR resume next
        sPKCS7 = "<%= sPKCS7 %>"
        //XEnroll.InstallPKCS7用于安装根证书。
        XEnroll.InstallPKCS7(sPKCS7)

        if err.Number <> 0 then
            if err.number = -2146885628 then
                MsgBox "Keyset does not exist"
            else
                MsgBox "证书下载时出错,错误号="&err.description
            end if
        else
            MsgBox "证书已成功装入"
        end if
</script>
<% } else {%>

//方法来源：
//http://blogs.msdn.com/alejacma/archive/2009/01/28/how-to-create-a-certificate-request-with-certenroll-javascript.aspx
//Vista下由于暂时没有测试环境，方法尚待验证

    <object id="objCertEnrollClassFactory" classid="clsid:884e2049-217d-11da-b2a4-000e7bbb2b09"></object>
    <script language="javascript">
    function InstallCert()
    {
        document.write("<br>Installing certificate...");
        try {
            // Variables
            var objEnroll = objCertEnrollClassFactory.CreateObject("X509Enrollment.CX509Enrollment")
            var sPKCS7 = "<%= sPKCS7 %>"
            objEnroll.Initialize(1); // ContextUser
            objEnroll.InstallResponse(0, sPKCS7, 6, ""); // AllowNone = 0, XCN_CRYPT_STRING_BASE64_ANY = 6
        }
        catch (ex) {
            document.write("<br>" + ex.description);
            return false;
        }

    return true;
    }

    InstallCert(); 

    </script>

<% } %>
</body>
</html>
