package sumdu.edu.ua.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.database.DatabaseConnector;
import sumdu.edu.ua.models.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    DatabaseConnector database;

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = database.getAllProducts();
        if (products == null || products.isEmpty()) {
            return new ResponseEntity<>(new ApiError("No products found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(products, HttpStatus.OK);
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Integer id) {
        Product product = database.getProductById(id);
        if (product == null) {
            return new ResponseEntity<>(new ApiError("Product not found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {;
        if (database.insertProduct(product.getName(), product.getDescription(), product.getType(), product.getPrice(), product.getQuantity())) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ApiError("Product could not be created"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Integer id, @RequestBody Product product) {
        if (database.updateProduct(id, product.getName(), product.getDescription(), product.getType(), product.getPrice(), product.getQuantity())){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiError("Product could not be updated"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Integer id) {
        if (database.deleteProduct(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiError("Product could not be deleted"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/types")
    public ResponseEntity<?> getAllTypes() {
        List<Type> types = database.getAllTypes();
        if (types == null || types.isEmpty()) {
            return new ResponseEntity<>(new ApiError("No types found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(types, HttpStatus.OK);
        }
    }

    @GetMapping("/types/{id}")
    public ResponseEntity<?> getTypeById(@PathVariable("id") Integer id) {
        Type type = database.getTypeById(id);
        if (type == null) {
            return new ResponseEntity<>(new ApiError("Type not found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(type, HttpStatus.OK);
        }
    }

    @PostMapping("/types")
    public ResponseEntity<?> createType(@RequestBody Type type) {
        if (database.insertType(type.getName(), type.getDescription())) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ApiError("Type could not be created"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/types/{id}")
    public ResponseEntity<?> updateType(@PathVariable("id") Integer id, @RequestBody Type type) {
        if (database.updateType(id, type.getName(), type.getDescription())) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiError("Type could not be updated"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/types/{id}")
    public ResponseEntity<?> deleteType(@PathVariable("id") Integer id) {
        if (database.deleteType(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiError("Type could not be deleted"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders() {
        List<Order> orders = database.getAllOrders();
        if (orders == null || orders.isEmpty()) {
            return new ResponseEntity<>(new ApiError("No orders found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") Integer id) {
        Order order = database.getOrderById(id);
        if (order == null) {
            return new ResponseEntity<>(new ApiError("Order not found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
    }

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        if (database.insertOrder(order.getOrderUserId(), order.getOrderProductId(), order.getOrderProductPrice(), order.getOrderQuantity(), order.getOrderEmail(), order.getOrderPaymentVariant(), order.getOrderDeliveryVariant())) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ApiError("Order could not be created"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable("id") Integer id, @RequestBody Order order) {
        if (database.updateOrder(id, order.getOrderUserId(), order.getOrderProductId(), order.getOrderProductPrice(), order.getOrderQuantity(), order.getOrderEmail(), order.getOrderPaymentVariant(), order.getOrderDeliveryVariant())) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiError("Order could not be updated"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Integer id) {
        if (database.deleteOrder(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiError("Order could not be deleted"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = database.getAllUsers();
        if (users == null || users.isEmpty()) {
            return new ResponseEntity<>(new ApiError("No users found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Integer id) {
        User user = database.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(new ApiError("User not found"), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        System.out.println(user.getUsername() + " " + user.getPassword());

        if (database.insertUser(user.getUsername(), user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new ApiError("User could not be created"), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Integer id, @RequestBody User user) {
        if (database.updateUser(id, user.getUsername(), user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiError("User could not be updated"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        if (database.deleteUser(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiError("User could not be deleted"), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
        String paramName = ex.getParameterName();
        return new ResponseEntity<>(new ApiError("Parameter " + paramName + " is missing"), HttpStatus.BAD_REQUEST);
    }
}
