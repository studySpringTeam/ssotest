<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
    <title>sso单点登录</title>
</head>
<body>
    <c:set var="ctx" value="${pageContext.request.contextPath}" />
    <form action="${ctx}/login" method="post">
        用户名：<input type="text" name="username" />
        密码：<input type="text" name="password" />
        <input type="submit" value="登录">
    </form>
</body>
</html>
