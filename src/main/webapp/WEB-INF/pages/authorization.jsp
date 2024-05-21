<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/general.css'/>">
    <link rel="stylesheet" href="<c:url value='/resources/css/login.css'/>">
    <link rel="stylesheet" href="<c:url value='/resources/css/input_form.css'/>">
</head>
<body>
    <main>
        <div id="content_box">
            <h2>Please login or register</h2>
            <c:if test="${not empty param.error}">
                <p>${param.error}</p>
            </c:if>
            <form name="authorization" action="/marketplace/auth" method="post" id="input-form">
                <input id="username" type="text" name="username" value="" placeholder="Name" maxlength="30" required class="form-input"/>
                <input id="password" type="password" name="password" value="" placeholder="Password" maxlength="30" required class="form-input"/>

                <div id="button-group">
                    <input type="submit" name="action" value="Login" class="form-submit">
                    <input type="submit" name="action" value="Signup" class="form-submit">
                </div>
            </form>
        </div>
    </main>
</body>
</html>