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
        <th>Payment Variant</th>
        <th>Delivery Variant</th>
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
            <c:choose>
                <c:when test="${sessionScope.role eq 'admin'}">
                    <td>
                        <select id="status_${order.orderId}" onchange="updateOrderStatus(${order.orderId})">
                            <option value="in_progress" <c:if test="${order.orderStatus eq 'in_progress'}">selected</c:if>>in_progress</option>
                            <option value="delivered" <c:if test="${order.orderStatus eq 'delivered'}">selected</c:if>>delivered</option>
                            <option value="acquiered" <c:if test="${order.orderStatus eq 'acquiered'}">selected</c:if>>acquiered</option>
                        </select>
                    </td>
                </c:when>
                <c:otherwise>
                    <td>${order.orderStatus}</td>
                </c:otherwise>
            </c:choose>
        </tr>
    </c:forEach>
</table>
<script>
    function updateOrderStatus(orderId) {
        var selectElement = document.getElementById('status_' + orderId);
        var selectedStatus = selectElement.options[selectElement.selectedIndex].value;
        console.log(selectedStatus);
        console.log(orderId);
        window.location.href = '/marketplace/orders/update/' + orderId + '/' + selectedStatus;
    }
</script>
</body>
</html>
