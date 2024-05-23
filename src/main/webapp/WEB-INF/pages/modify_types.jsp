<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title><c:choose><c:when test="${not empty type}">Edit</c:when><c:otherwise>Add</c:otherwise></c:choose> Type</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/general.css'/>">
    <link rel="stylesheet" href="<c:url value='/resources/css/input_form.css'/>">
</head>
<body>
<div id="content_box">
    <form id="input-form" action="/marketplace/types/<c:choose><c:when test="${not empty type}">process_edit</c:when><c:otherwise>process_add</c:otherwise></c:choose>" method="post" onsubmit="validateForm(event)">

        <input type="hidden" id="typeId" name="typeId" value="<c:choose><c:when test="${not empty type}">${type.id}</c:when><c:otherwise>0</c:otherwise></c:choose>">

        <label for="typeName">Type Name:</label><br>
        <input type="text" id="typeName" name="typeName" value="${not empty type ? type.name : ''}" required class="form-input"><br>

        <label for="typeDescription">Type Description:</label><br>
        <textarea id="typeDescription" name="typeDescription" required class="form-input">${not empty type ? type.description : ''}</textarea><br>

        <input type="submit" value="<c:choose><c:when test="${not empty type}" >Save</c:when><c:otherwise>Add</c:otherwise></c:choose>" class="form-submit">
    </form>
    <a href="/marketplace/main_menu/store">Return to store</a>
</div>
<script>
    async function validateForm(event) {
        event.preventDefault()

        var typeId = document.getElementById('typeId').value;
        var typeName = document.getElementById('typeName').value;
        var response;

        if (typeId === '0') {
            response = await fetch('/marketplace/store/validate/typeName?name=' + typeName);
        }
        else {
            response = await fetch('/marketplace/store/validate/typeName?id=' + typeId + '&name=' + typeName);
        }

        var exists = JSON.parse(await response.text());

        if (exists) {
            alert("Type name already exists");
        } else {
            document.getElementById('input-form').submit();
        }
    }
</script>
</body>
</html>
