<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/template.css">
</head>

<body>
    <div id="main_container">
        <header>
            <a href="/mainpage/logout">Exit</a>
        </header>

        <div id="container">
            <nav id="menu">
                <h2>Menu</h2>
                <a href="/marketplace/main_menu/store">Store</a>
                <a href="/marketplace/main_menu/cart">Cart</a>
                <a href="/marketplace/main_menu/orders">Orders</a>
            </nav>

            <div id="main">
                <c:choose>
                    <c:when test="${not empty action}">
                        <jsp:include page="${action}.jsp" />
                    </c:when>
                    <c:otherwise>
                        <h1>Hello, ${sessionScope.username}</h1>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <footer>
            Copyright 2000-2024
        </footer>
    </div>
</body>
</html>
