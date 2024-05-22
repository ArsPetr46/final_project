package sumdu.edu.ua.database;

import sumdu.edu.ua.models.Order;
import sumdu.edu.ua.models.Product;
import sumdu.edu.ua.models.Type;
import sumdu.edu.ua.models.User;

import java.sql.Connection;
import java.util.List;

public abstract class DatabaseConnector {
    protected Connection connection;
    protected abstract Connection getConnection();
    protected abstract void closeConnection();


    public abstract boolean checkUser(String username, String password);
    public abstract String getUserRole(String username);


    public abstract List<Product> getProductsForUser(String username, String name, String filter, String sort, String order, boolean wishlisted);
    public abstract List<Order> getOrdersForUser(String username);
    public abstract void addToWishlist(int productId, String username);
    public abstract void removeFromWishlist(int productId, String username);


    public abstract List<String> getTypeNames();
    public abstract int getCurrentQuantity(int productId);
    public abstract boolean checkProductName(String productName);
    public abstract boolean checkProductName(int id, String productName);
    public abstract boolean checkTypeName(String typeName);
    public abstract boolean checkTypeName(int id, String typeName);
    public abstract boolean productExists(int productId);
    public abstract void updateOrderStatus(int orderId, String status);

    public abstract List<Product> getAllProducts();
    public abstract Product getProductById(int productId);
    public abstract boolean insertProduct(String productName, String productDescription, int productTypeId, double productPrice, int productQuantity);
    public abstract boolean insertProduct(String productName, String productDescription, String typeName, double productPrice, int productQuantity);
    public abstract boolean updateProduct(int productId, String productName, String productDescription, int productTypeId, double productPrice, int productQuantity);
    public abstract boolean updateProduct(int productId, String productName, String productDescription, String typeName, double productPrice, int productQuantity);
    public abstract boolean deleteProduct(int productId);


    public abstract List<Type> getAllTypes();
    public abstract Type getTypeById(int typeId);
    public abstract boolean insertType(String typeName, String typeDescription);
    public abstract boolean updateType(int typeId, String typeName, String typeDescription);
    public abstract boolean deleteType(int typeId);


    public abstract List<Order> getAllOrders();
    public abstract Order getOrderById(int orderId);
    public abstract boolean insertOrder(String username, int productId, double price, int quantity, String email, String payment, String delivery);
    public abstract boolean insertOrder(int userId, int productId, double price, int quantity, String email, String payment, String delivery);
    public abstract boolean updateOrder(int orderId, int userId, int productId, double price, int quantity, String email, String payment, String delivery);
    public abstract boolean deleteOrder(int orderId);


    public abstract List<User> getAllUsers();
    public abstract User getUserById(int userId);
    public abstract boolean insertUser(String username, String password);
    public abstract boolean updateUser(int userId, String username, String password);
    public abstract boolean deleteUser(int userId);
}
