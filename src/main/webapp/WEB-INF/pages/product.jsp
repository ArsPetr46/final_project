<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Product page</title>
</head>
<body>
    <h1>${product.name}</h1>
    <p>Description: ${product.description}</p>
    <p>Type: ${product.type}</p>
    <p>Type Description: ${product.typeDescription}</p>
    <p>Price: ${product.price}</p>
    <p>Quantity: ${product.quantity}</p>
    <a href="/markerplace/store">Return to store</a>
</body>
</html>
