<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title><c:choose><c:when test="${not empty product}">Edit</c:when><c:otherwise>Add</c:otherwise></c:choose> Product</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/general.css'/>">
    <link rel="stylesheet" href="<c:url value='/resources/css/input_form.css'/>">
</head>
<body>
<div id="content_box">
    <form id="input-form" action="/marketplace/store/<c:choose><c:when test="${not empty product}">process_edit</c:when><c:otherwise>process_add</c:otherwise></c:choose>" method="post" onsubmit="validateForm(event)">

        <input type="hidden" id="productId" name="productId" value="<c:choose><c:when test="${not empty product}">${product.id}</c:when><c:otherwise>0</c:otherwise></c:choose>">

        <label for="productName">Product Name:</label><br>
        <input type="text" id="productName" name="productName" value="${not empty product ? product.name : ''}" required class="form-input"><br>

        <label for="productDescription">Product Description:</label><br>
        <textarea id="productDescription" name="productDescription" required class="form-input">${not empty product ? product.description : ''}</textarea><br>

        <label for="productTypeId">Product Type:</label><br>
        <select id="productTypeId" name="productTypeId">
            <c:forEach var="type" items="${types}">
                <option value="${type.id}" ${not empty product and type.name eq product.type ? 'selected' : ''}>${type.name}</option>
            </c:forEach>
        </select><br>

        <label for="productPrice">Product Price:</label><br>
        <input type="number" id="productPrice" name="productPrice" value="${not empty product ? product.price : ''}" required class="form-input"><br>

        <label for="productQuantity">Product Quantity:</label><br>
        <input type="number" id="productQuantity" name="productQuantity" value="${not empty product ? product.quantity : ''}" required class="form-input"><br>

        <input type="submit" value="<c:choose><c:when test="${not empty product}">Save</c:when><c:otherwise>Add</c:otherwise></c:choose>" class="form-submit">
    </form>
    <a href="/marketplace/main_menu/store">Return to store</a>
</div>
<script>
    async function validateForm(event) {
        event.preventDefault();

        var productId = document.getElementById('productId').value;
        var productName = document.getElementById('productName').value;
        var response;

        if (productId === '0') {
            response = await fetch('/marketplace/store/validate/productName?name=' + productName);
        }
        else {
            response = await fetch('/marketplace/store/validate/productName?id=' + productId + '&name=' + productName);
        }

        var exists = JSON.parse(await response.text());

        if (exists) {
            alert("Product name already exists");
        } else {
            document.getElementById('input-form').submit();
        }
    }
</script>
</body>
</html>
