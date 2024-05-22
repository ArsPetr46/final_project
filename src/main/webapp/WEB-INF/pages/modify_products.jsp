<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title><c:choose><c:when test="${not empty product}">Edit</c:when><c:otherwise>Add</c:otherwise></c:choose> Product</title>
</head>
<body>
<form id="product_form" action="/marketplace/store/<c:choose><c:when test="${not empty product}">process_edit</c:when><c:otherwise>process_add</c:otherwise></c:choose>" method="post" onsubmit="validateForm(event)">
    <c:if test="${not empty product}">
        <input type="hidden" name="productId" value="${product.id}">
    </c:if>

    <label for="productName">Product Name:</label><br>
    <input type="text" id="productName" name="productName" value="${not empty product ? product.name : ''}" required><br>

    <label for="productDescription">Product Description:</label><br>
    <textarea id="productDescription" name="productDescription" required>${not empty product ? product.description : ''}</textarea><br>

    <label for="productTypeId">Product Type:</label><br>
    <select id="productTypeId" name="productTypeId">
        <c:forEach var="type" items="${types}">
            <option value="${type.id}" ${not empty product and type.name eq product.type ? 'selected' : ''}>${type.name}</option>
        </c:forEach>
    </select><br>

    <label for="productPrice">Product Price:</label><br>
    <input type="number" id="productPrice" name="productPrice" value="${not empty product ? product.price : ''}" required><br>

    <label for="productQuantity">Product Quantity:</label><br>
    <input type="number" id="productQuantity" name="productQuantity" value="${not empty product ? product.quantity : ''}" required><br>

    <input type="submit" value="<c:choose><c:when test="${not empty product}">Save</c:when><c:otherwise>Add</c:otherwise></c:choose>">
</form>
<script>
    async function validateForm(event) {
        event.preventDefault();

        var productName = document.getElementById('productName').value;

        var response = await fetch('/marketplace/store/check_product_name/' + productName);
        var data = await response.json();

        if (data.exists) {
            alert("Product name already exists");
        } else {
            document.getElementById('product_form').submit();
        }
    }
</script>
</body>
</html>
