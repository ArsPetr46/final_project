package sumdu.edu.ua.database;

import sumdu.edu.ua.models.Product;
import sumdu.edu.ua.models.Type;

import java.sql.Connection;
import java.util.List;

public abstract class DatabaseConnector {
    protected Connection connection;
    protected abstract Connection getConnection();
    protected abstract void closeConnection();
    public abstract boolean checkUser(String username, String password);
    public abstract String getUserRole(String username);
    public abstract boolean insertUser(String username, String password);
    public abstract List<Product> getProducts(String username, String name, String filter, String sort, String order, boolean wishlisted);
    public abstract void addToWishlist(int productId, String username);
    public abstract void removeFromWishlist(int productId, String username);
    public abstract List<String> getTypeNames();
    public abstract List<Type> getTypes();
    public abstract Product getProductById(int productId);
    public abstract int getCurrentQuantity(int productId);
    public abstract void insertOrder(String username, int productId, double price, int quantity, String email, String payment, String delivery);
    public abstract boolean checkProductName(String productName);
    public abstract boolean insertProduct(String productName, String productDescription, int productTypeId, double productPrice, int productQuantity);
    public abstract Type getTypeById(int typeId);
    public abstract boolean checkTypeName(String typeName);
    public abstract boolean insertType(String typeName, String typeDescription);
    public abstract boolean updateType(int typeId, String typeName, String typeDescription);
    public abstract boolean deleteType(int typeId);
    public abstract boolean deleteProduct(int productId);
    public abstract boolean updateProduct(int productId, String productName, String productDescription, int productTypeId, double productPrice, int productQuantity);
    public abstract boolean productExists(int productId);
}
