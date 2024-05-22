<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Main menu template</title>
    <link rel="stylesheet" href="css/template.css">
</head>

<body>
    <div id="main_container">
        <header>
            <a href="/marketplace/logout">Exit</a>
        </header>

        <div id="container">
            <nav id="menu">
                <h2>Menu</h2>
                <a href="/marketplace/main_menu/store">Store</a>
                <c:if test="${sessionScope.role eq 'admin'}">
                    <a href="/marketplace/main_menu/types">Types</a>
                    <a href="/marketplace/main_menu/all_orders">All Orders</a>
                </c:if>
                <c:if test="${sessionScope.role ne 'admin'}">
                    <a href="/marketplace/main_menu/user_orders">My Orders</a>
                </c:if>

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
