<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Orders Page</title>
</head>
<body>
<c:if test="${not empty param.error}">
    <p style="color: red;">${param.error}</p>
</c:if>
<table>
    <tr>
        <th>Order ID</th>
        <th>User ID</th>
        <th>Product ID</th>
        <th>Quantity</th>
        <th>Product Price</th>
        <th>Total Price</th>
        <th>Email</th>
        <th>Delivery Variant</th>
        <th>Payment Variant</th>
        <th>Status</th>
    </tr>
    <c:forEach var="order" items="${orders}">
        <tr>
            <td>${order.orderId}</td>
            <td>${order.orderUserId}</td>
            <td>${order.orderProductId}</td>
            <td>${order.orderQuantity}</td>
            <td>${order.orderProductPrice}</td>
            <td>${order.orderTotalPrice}</td>
            <td>${order.orderEmail}</td>
            <td>${order.orderDeliveryVariant}</td>
            <td>${order.orderPaymentVariant}</td>
            <td>${order.orderStatus}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
