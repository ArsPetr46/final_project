<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title><c:choose><c:when test="${not empty type}">Edit</c:when><c:otherwise>Add</c:otherwise></c:choose> Type</title>
</head>
<body>
<form id="type_form" action="/marketplace/types/<c:choose><c:when test="${not empty type}">process_edit</c:when><c:otherwise>process_add</c:otherwise></c:choose>" method="post" onsubmit="validateForm(event)">
    <c:if test="${not empty type}">
        <input type="hidden" name="typeId" value="${type.id}">
    </c:if>

    <label for="typeName">Type Name:</label><br>
    <input type="text" id="typeName" name="typeName" value="${not empty type ? type.name : ''}" required><br>

    <label for="typeDescription">Type Description:</label><br>
    <textarea id="typeDescription" name="typeDescription" required>${not empty type ? type.description : ''}</textarea><br>

    <input type="submit" value="<c:choose><c:when test="${not empty type}">Save</c:when><c:otherwise>Add</c:otherwise></c:choose>">
</form>
<script>
    async function validateForm(event) {
        event.preventDefault();

        var typeName = document.getElementById('typeName').value;

        var response = await fetch('/marketplace/types/check_type_name/' + typeName);
        var data = await response.json();

        if (data.exists) {
            alert("Type name already exists");
        } else {
            document.getElementById('type_form').submit();
        }
    }
</script>
</body>
</html>
