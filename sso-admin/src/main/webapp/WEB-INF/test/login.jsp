<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>sso单点登录</title>
</head>
<body>
    <shiro:authenticated>
        <%
            String redirectUrl = (String) request.getAttribute("redirectUrl");
            if (redirectUrl != null && redirectUrl != "") {
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect(request.getContextPath() + "/test/index");
            }
        %>
    </shiro:authenticated>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    <form action="${ctx}/login" method="post">
        用户名：<input type="text" name="username" />
        密码：<input type="text" name="password" />
        <input type="hidden" name="redirectUrl" value="${redirectUrl}" />
        <input type="submit" value="登录">
    </form>
</body>
</html>
