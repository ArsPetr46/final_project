<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Types page</title>
</head>
<body>
<c:if test="${not empty param.error}">
    <p style="color: red;">${param.error}</p>
</c:if>
<table>
    <tr>
        <th>Type Name</th>
        <th>Type Description</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="type" items="${types}">
        <tr>
            <td>${type.name}</td>
            <td>${type.description}</td>
            <td>
                <form action="/marketplace/types/edit/${type.id}" method="post">
                    <input type="submit" value="Edit">
                </form>
                <form action="/marketplace/types/delete/${type.id}" method="post">
                    <input type="submit" value="Delete">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<form action="/marketplace/types/add" method="post">
    <input type="submit" value="Add Type">
</form>
</body>
</html>