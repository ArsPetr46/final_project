<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Store page</title>
</head>
<body>
    <c:if test="${not empty param.error}">
        <p style="color: red;">${param.error}</p>
    </c:if>
    <form action="/marketplace/main_menu/store" method="get">
        <input type="text" name="name" placeholder="Search for name" value="${empty param.name ? '' : param.name}">
        <select name="filter">
            <option value="All" ${empty param.filter ? 'selected' : ''}>All</option>
            <c:forEach var="type" items="${types}">
                <option value="${type}" ${type eq param.filter ? 'selected' : ''}>${type}</option>
            </c:forEach>
        </select>
        <select name="sort">
            <option value="noSort" ${empty param.sort ? 'selected' : ''}>No sorting</option>
            <option value="name" ${'name' eq param.sort ? 'selected' : ''}>Name</option>
            <option value="price" ${'price' eq param.sort ? 'selected' : ''}>Price</option>
            <option value="quantity" ${'quantity' eq param.sort ? 'selected' : ''}>Quantity</option>
        </select>
        <select name="order">
            <option value="asc" ${'asc' eq param.order ? 'selected' : ''}>Ascending</option>
            <option value="desc" ${'desc' eq param.order ? 'selected' : ''}>Descending</option>
        </select>
        <input type="checkbox" name="wishlisted" value="true" ${'true' eq param.wishlisted ? 'checked' : ''}> Only Wishlisted
        <input type="submit" value="Apply">
    </form>
    <table>
        <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Is Wishlisted</th>
        </tr>
        <c:forEach var="product" items="${products}">
            <tr>
                <td><a href="/marketplace/store/product/${product.id}">${product.name}</a></td>
                <td>${product.type}</td>
                <td>${product.price}</td>
                <td>${product.quantity}</td>
                <td><input type="checkbox" id="wishlist_${product.id}" ${product.isWishlisted ? 'checked' : ''} onclick="toggleWishlist(${product.id})"></td>
                <td>
                    <c:if test="${product.quantity > 0}">
                        <c:choose>
                            <c:when test="${sessionScope.role eq 'admin'}">
                                <form action="/marketplace/store/edit/${product.id}" method="post">
                                    <input type="submit" value="Edit">
                                </form>
                                <form action="/marketplace/store/delete/${product.id}" method="post">
                                    <input type="submit" value="Delete">
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form action="/marketplace/store/order/${product.id}" method="post">
                                    <input type="number" name="quantity" min="1" max="${product.quantity}" value="1" onchange="validateQuantity(this, ${product.quantity})">
                                    <input type="submit" value="Order">
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
    <c:if test="${role eq 'admin'}">
        <form action="/marketplace/store/add" method="post">
            <input type="submit" name="admin_action" value="Add Product">
        </form>
    </c:if>
    <script>
        function toggleWishlist(productId) {
            var isChecked = document.getElementById('wishlist_' + productId).checked;
            var url = '/marketplace/store/' + (isChecked ? 'add_to_wishlist' : 'remove_from_wishlist') + '/' + productId;
            fetch(url, { method: 'POST' });
        }
        function validateQuantity(input, maxQuantity) {
            if (input.value > maxQuantity) {
                alert("Quantity cannot be more than " + maxQuantity);
                input.value = maxQuantity;
            } else if (input.value < 1) {
                alert("Quantity cannot be less than 1");
                input.value = 1;
            }
        }
    </script>
</body>
</html>
