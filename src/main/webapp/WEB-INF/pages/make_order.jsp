<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Order page</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/general.css'/>">
    <link rel="stylesheet" href="<c:url value='/resources/css/input_form.css'/>">
</head>
<body>
<div id="content_box">
    <form id="input-form" action="/marketplace/store/process_order" method="post">
        <input type="hidden" id="productId" name="productId" value="${product.id}">

        <label for="productName">Product:</label><br>
        <input type="text" id="productName" name="productName" value="${product.name}" readonly class="form-input"><br>

        <label for="price">Price:</label><br>
        <input type="text" id="price" name="price" value="${product.price}" readonly class="form-input"><br>

        <label for="quantity">Quantity:</label><br>
        <input type="number" id="quantity" name="quantity" value="${quantity}" readonly class="form-input"><br>

        <label for="total">Total:</label><br>
        <input type="text" id="total" name="total" value="${product.price * quantity}" readonly class="form-input"><br>

        <label for="email">Email:</label><br>
        <input type="email" id="email" name="email" required class="form-input"><br>

        <label for="payment">Payment Method:</label><br>
        <select id="payment" name="payment">
            <option value="cash">Cash</option>
            <option value="credit_card">Credit Card</option>
        </select><br>

        <label for="delivery">Delivery Method:</label><br>
        <select id="delivery" name="delivery">
            <option value="pickup">Pickup</option>
            <option value="courier">Courier</option>
            <option value="post">Post</option>
        </select><br>

        <input type="submit" value="Submit" class="form-submit">
    </form>

    <a href="/marketplace/main_menu/store">Back to Store</a>
</div>
</body>
</html>
