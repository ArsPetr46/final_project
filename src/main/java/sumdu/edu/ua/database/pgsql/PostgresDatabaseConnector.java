package sumdu.edu.ua.database.pgsql;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import sumdu.edu.ua.database.DatabaseConnector;
import sumdu.edu.ua.models.Order;
import sumdu.edu.ua.models.Product;
import sumdu.edu.ua.models.Type;
import sumdu.edu.ua.models.User;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

@Component
@Primary
public class PostgresDatabaseConnector extends DatabaseConnector {
    @Override
    protected Connection getConnection() {
        try {
            if (connection == null) {
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://localhost:5432/postgres";
                String username = "postgres";
                String password = "postgres";
                connection = DriverManager.getConnection(url, username, password);
            }

            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connection = null;
            }
        }
    }

    @Override
    public boolean checkUser(String username, String password) {
        try {
            String query = "SELECT * FROM final_project_users " +
                            "WHERE username = ? AND password = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            closeConnection();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getUserRole(String username) {
        try {
            String query = "SELECT role FROM final_project_users " +
                            "WHERE username = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            String role = resultSet.getString("role");

            closeConnection();
            return role;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> getProductsForUser(String username, String name, String filter, String sort, String order, boolean wishlisted) {
        try {
            String query = "SELECT *\n" +
                    "FROM final_project_products p\n" +
                    "JOIN final_project_product_types pt ON p.product_type_id = pt.type_id\n" +
                    "LEFT JOIN final_project_wishlist w ON p.product_id = w.product_id\n" +
                    "AND w.user_id = (SELECT user_id FROM final_project_users WHERE username = '" + username + "')\n";

            if (wishlisted || !name.isEmpty() || !filter.equals("All")) {
                query = query + "WHERE true\n";
            }
            if (wishlisted) {
                query = query + "AND (user_id = (SELECT user_id FROM final_project_users WHERE username = ?))\n";
            }

            if (!name.isEmpty()) {
                query = query + "AND (product_name LIKE '%" + name + "%')\n";
            }

            if (!filter.equals("All")) {
                query = query + "AND (type_name = '" + filter + "')\n";
            }


            if (sort != null) {
                switch (sort) {
                    case "name":
                        query = query + "ORDER BY product_name " + order;
                        break;
                    case "price":
                        query = query + "ORDER BY product_price " + order;
                        break;
                    case "quantity":
                        query = query + "ORDER BY product_quantity " + order;
                        break;
                }
            }

            System.out.println(query);

            PreparedStatement statement = getConnection().prepareStatement(query);
            if (wishlisted) {
                statement.setString(1, username);
            }

            ResultSet resultSet = statement.executeQuery();

            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getString("product_description"),
                        resultSet.getString("type_name"),
                        resultSet.getString("type_description"),
                        resultSet.getDouble("product_price"),
                        resultSet.getInt("product_quantity"),
                        resultSet.getInt("user_id") != 0
                );

                products.add(product);
            }

            closeConnection();
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getOrdersForUser(String username) {
        try {
            String query = "SELECT * FROM final_project_orders o\n" +
                    "JOIN final_project_products p ON o.order_product_id = p.product_id\n" +
                    "JOIN final_project_product_types pt ON p.product_type_id = pt.type_id\n" +
                    "WHERE o.order_user_id = (SELECT user_id FROM final_project_users WHERE username = ?);";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getInt("order_id"),
                        resultSet.getInt("order_user_id"),
                        resultSet.getInt("order_product_id"),
                        resultSet.getInt("order_quantity"),
                        resultSet.getDouble("order_product_price"),
                        resultSet.getDouble("order_total_price"),
                        resultSet.getString("order_email"),
                        resultSet.getString("order_payment_variant"),
                        resultSet.getString("order_delivery_variant"),
                        resultSet.getString("order_status")
                );

                orders.add(order);
            }

            closeConnection();
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addToWishlist(int productId, String username) {
        try {
            String query = "INSERT INTO final_project_wishlist (user_id, product_id) " +
                    "VALUES ((SELECT user_id FROM final_project_users WHERE username = ?), ?);";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, username);
            statement.setInt(2, productId);
            statement.executeUpdate();

            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeFromWishlist(int productId, String username) {
        try {
            String query = "DELETE FROM final_project_wishlist " +
                    "WHERE user_id = (SELECT user_id FROM final_project_users WHERE username = ?) " +
                    "AND product_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, username);
            statement.setInt(2, productId);
            statement.executeUpdate();

            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getTypeNames() {
        try {
            String query = "SELECT DISTINCT type_name FROM final_project_product_types;";
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<String> types = new ArrayList<>();
            while (resultSet.next()) {
                types.add(resultSet.getString("type_name"));
            }

            closeConnection();
            return types;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCurrentQuantity(int productId) {
        try {
            String query = "SELECT product_quantity FROM final_project_products WHERE product_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            int quantity = resultSet.getInt("product_quantity");

            closeConnection();
            return quantity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkProductName(String productName) {
        try {
            String query = "SELECT * FROM final_project_products WHERE product_name = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, productName);
            ResultSet resultSet = statement.executeQuery();

            closeConnection();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkTypeName(String typeName) {
        try {
            String query = "SELECT * FROM final_project_product_types WHERE type_name = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, typeName);
            ResultSet resultSet = statement.executeQuery();

            closeConnection();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean productExists(int productId) {
        try {
            String query = "SELECT * FROM final_project_products WHERE product_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            closeConnection();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public List<Product> getAllProducts() {
        try {
            String query = "SELECT * FROM final_project_products p\n" +
                    "JOIN final_project_product_types pt ON p.product_type_id = pt.type_id;";
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getString("product_description"),
                        resultSet.getString("type_name"),
                        resultSet.getString("type_description"),
                        resultSet.getDouble("product_price"),
                        resultSet.getInt("product_quantity"),
                        false
                );

                products.add(product);
            }

            closeConnection();
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product getProductById(int productId) {
        try {
            String query = "SELECT * FROM final_project_products p\n" +
                    "JOIN final_project_product_types t on p.product_type_id = t.type_id\n" +
                    "WHERE p.product_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();

            Product product = null;
            if (resultSet.next()) {
                product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getString("product_description"),
                        resultSet.getString("type_name"),
                        resultSet.getString("type_description"),
                        resultSet.getDouble("product_price"),
                        resultSet.getInt("product_quantity"),
                        false
                );
            }

            closeConnection();
            return product;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean insertProduct(String productName, String productDescription, int productTypeId, double productPrice, int productQuantity) {
        try {
            String query = "INSERT INTO final_project_products (product_name, product_description, product_type_id, product_price, product_quantity) " +
                    "VALUES (?, ?, ?, ?, ?);";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, productName);
            statement.setString(2, productDescription);
            statement.setInt(3, productTypeId);
            statement.setDouble(4, productPrice);
            statement.setInt(5, productQuantity);
            int affectedRows = statement.executeUpdate();

            closeConnection();
            return affectedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean insertProduct(String productName, String productDescription, String typeName, double productPrice, int productQuantity) {
        try {
            String query = "INSERT INTO final_project_products (product_name, product_description, product_type_id, product_price, product_quantity) " +
                    "VALUES (?, ?, (SELECT type_id FROM final_project_product_types WHERE type_name = ?), ?, ?);";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, productName);
            statement.setString(2, productDescription);
            statement.setString(3, typeName);
            statement.setDouble(4, productPrice);
            statement.setInt(5, productQuantity);
            int affectedRows = statement.executeUpdate();

            closeConnection();
            return affectedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean updateProduct(int productId, String productName, String productDescription, int productTypeId, double productPrice, int productQuantity) {
        try {
            String query = "UPDATE final_project_products " +
                    "SET product_name = ?, product_description = ?, product_type_id = ?, product_price = ?, product_quantity = ? " +
                    "WHERE product_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, productName);
            statement.setString(2, productDescription);
            statement.setInt(3, productTypeId);
            statement.setDouble(4, productPrice);
            statement.setInt(5, productQuantity);
            statement.setInt(6, productId);
            int affectedRows = statement.executeUpdate();

            closeConnection();
            return affectedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean updateProduct(int productId, String productName, String productDescription, String typeName, double productPrice, int productQuantity) {
        try {
            String query = "UPDATE final_project_products " +
                    "SET product_name = ?, product_description = ?, product_type_id = (SELECT type_id FROM final_project_product_types WHERE type_name = ?), product_price = ?, product_quantity = ? " +
                    "WHERE product_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, productName);
            statement.setString(2, productDescription);
            statement.setString(3, typeName);
            statement.setDouble(4, productPrice);
            statement.setInt(5, productQuantity);
            statement.setInt(6, productId);
            int affectedRows = statement.executeUpdate();

            closeConnection();
            return affectedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean deleteProduct(int productId) {
        try {
            getConnection().setAutoCommit(false);

            String query = "DELETE FROM final_project_wishlist WHERE product_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, productId);
            statement.executeUpdate();

            query = "DELETE FROM final_project_products WHERE product_id = ?;";
            statement = getConnection().prepareStatement(query);
            statement.setInt(1, productId);
            int affectedRows = statement.executeUpdate();

            getConnection().commit();

            getConnection().setAutoCommit(true);

            closeConnection();
            return affectedRows > 0;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    closeConnection();
                }
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Type> getAllTypes() {
        try {
            String query = "SELECT * FROM final_project_product_types;";
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Type> types = new ArrayList<>();
            while (resultSet.next()) {
                Type type = new Type(
                        resultSet.getInt("type_id"),
                        resultSet.getString("type_name"),
                        resultSet.getString("type_description")
                );

                types.add(type);
            }

            closeConnection();
            return types;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getTypeById(int typeId) {
        try {
            String query = "SELECT * FROM final_project_product_types WHERE type_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setInt(1, typeId);
            ResultSet resultSet = statement.executeQuery();

            Type type = null;
            if (resultSet.next()) {
                type = new Type(
                        resultSet.getInt("type_id"),
                        resultSet.getString("type_name"),
                        resultSet.getString("type_description")
                );
            }

            closeConnection();
            return type;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean insertType(String typeName, String typeDescription) {
        try {
            String query = "INSERT INTO final_project_product_types (type_name, type_description) " +
                    "VALUES (?, ?);";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, typeName);
            statement.setString(2, typeDescription);
            int affectedRows = statement.executeUpdate();

            closeConnection();
            return affectedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean updateType(int typeId, String typeName, String typeDescription) {
        try {
            String query = "UPDATE final_project_product_types " +
                    "SET type_name = ?, type_description = ? " +
                    "WHERE type_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, typeName);
            statement.setString(2, typeDescription);
            statement.setInt(3, typeId);
            int affectedRows = statement.executeUpdate();

            closeConnection();
            return affectedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean deleteType(int typeId) {
        try {
            getConnection().setAutoCommit(false);

            String query = "DELETE FROM final_project_wishlist WHERE product_id IN (SELECT product_id FROM final_project_products WHERE product_type_id = ?);";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, typeId);
            statement.executeUpdate();

            query = "DELETE FROM final_project_products WHERE product_type_id = ?;";
            statement = getConnection().prepareStatement(query);
            statement.setInt(1, typeId);
            statement.executeUpdate();

            query = "DELETE FROM final_project_product_types WHERE type_id = ?;";
            statement = getConnection().prepareStatement(query);
            statement.setInt(1, typeId);
            int affectedRows = statement.executeUpdate();

            getConnection().commit();

            getConnection().setAutoCommit(true);

            closeConnection();
            return affectedRows > 0;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    closeConnection();
                }
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        try {
            String query = "SELECT * FROM final_project_orders o\n;";
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getInt("order_id"),
                        resultSet.getInt("order_user_id"),
                        resultSet.getInt("order_product_id"),
                        resultSet.getInt("order_quantity"),
                        resultSet.getDouble("order_product_price"),
                        resultSet.getDouble("order_total_price"),
                        resultSet.getString("order_email"),
                        resultSet.getString("order_payment_variant"),
                        resultSet.getString("order_delivery_variant"),
                        resultSet.getString("order_status")
                );

                orders.add(order);
            }

            closeConnection();
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order getOrderById(int orderId) {
        try {
            String query = "SELECT * FROM final_project_orders WHERE order_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();

            Order order = null;
            if (resultSet.next()) {
                order = new Order(
                        resultSet.getInt("order_id"),
                        resultSet.getInt("order_user_id"),
                        resultSet.getInt("order_product_id"),
                        resultSet.getInt("order_quantity"),
                        resultSet.getDouble("order_product_price"),
                        resultSet.getDouble("order_total_price"),
                        resultSet.getString("order_email"),
                        resultSet.getString("order_delivery_variant"),
                        resultSet.getString("order_payment_variant"),
                        resultSet.getString("order_status")
                );
            }

            closeConnection();
            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean insertOrder(String username, int productId, double price, int quantity, String email, String payment, String delivery) {
        try {
            String query = "INSERT INTO final_project_orders (order_user_id, order_product_id, order_product_price, order_quantity, order_email, order_payment_variant, order_delivery_variant) " +
                    "VALUES ((SELECT user_id FROM final_project_users WHERE username = ?), ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, username);
            statement.setInt(2, productId);
            statement.setDouble(3, price);
            statement.setInt(4, quantity);
            statement.setString(5, email);
            statement.setString(6, payment.toLowerCase());
            statement.setString(7, delivery.toLowerCase());
            statement.executeUpdate();

            query = "UPDATE final_project_products\n" +
                    "SET product_quantity = product_quantity - ? " +
                    "WHERE product_id = ?;";
            statement = getConnection().prepareStatement(query);

            statement.setInt(1, quantity);
            statement.setInt(2, productId);
            int affectedRows = statement.executeUpdate();

            closeConnection();
            return affectedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean insertOrder(int userId, int productId, double price, int quantity, String email, String payment, String delivery) {
        try {
            String query = "INSERT INTO final_project_orders (order_user_id, order_product_id, order_product_price, order_quantity, order_email, order_payment_variant, order_delivery_variant) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.setDouble(3, price);
            statement.setInt(4, quantity);
            statement.setString(5, email);
            statement.setString(6, payment.toLowerCase());
            statement.setString(7, delivery.toLowerCase());
            statement.executeUpdate();

            query = "UPDATE final_project_products\n" +
                    "SET product_quantity = product_quantity - ? " +
                    "WHERE product_id = ?;";
            statement = getConnection().prepareStatement(query);

            statement.setInt(1, quantity);
            statement.setInt(2, productId);
            int affectedRows = statement.executeUpdate();

            closeConnection();
            return affectedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean updateOrder(int orderId, int userId, int productId, double price, int quantity, String email, String payment, String delivery) {
        try {
            String query = "UPDATE final_project_orders " +
                    "SET order_user_id = ?, order_product_id = ?, order_product_price = ?, order_quantity = ?, order_email = ?, order_payment_variant = ?, order_delivery_variant = ? " +
                    "WHERE order_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.setDouble(3, price);
            statement.setInt(4, quantity);
            statement.setString(5, email);
            statement.setString(6, payment);
            statement.setString(7, delivery);
            statement.setInt(8, orderId);
            int affectedRows = statement.executeUpdate();

            closeConnection();
            return affectedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean deleteOrder(int orderId) {
        try {
            getConnection().setAutoCommit(false);

            String query = "DELETE FROM final_project_orders WHERE order_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, orderId);
            int affectedRows = statement.executeUpdate();

            getConnection().commit();

            getConnection().setAutoCommit(true);

            closeConnection();
            return affectedRows > 0;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    closeConnection();
                }
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            String query = "SELECT * FROM final_project_users;";
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("role")
                );

                users.add(user);
            }

            closeConnection();
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserById(int userId) {
        try {
            String query = "SELECT * FROM final_project_users WHERE user_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            User user = null;
            if (resultSet.next()) {
                user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("role")
                );
            }


            closeConnection();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean insertUser(String username, String password) {
        try {
            String query = "INSERT INTO final_project_users (username, password) " +
                    "VALUES (?, ?);";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();

            closeConnection();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean updateUser(int userId, String username, String password) {
        try {
            String query = "UPDATE final_project_users " +
                    "SET username = ?, password = ? " +
                    "WHERE user_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setInt(3, userId);
            int affectedRows = statement.executeUpdate();

            closeConnection();
            return affectedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean deleteUser(int userId) {
        try {
            getConnection().setAutoCommit(false);

            String query = "DELETE FROM final_project_wishlist WHERE user_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, userId);
            statement.executeUpdate();

            query = "DELETE FROM final_project_orders WHERE order_user_id = ?;";
            statement = getConnection().prepareStatement(query);
            statement.setInt(1, userId);
            statement.executeUpdate();

            query = "DELETE FROM final_project_users WHERE user_id = ?;";
            statement = getConnection().prepareStatement(query);
            statement.setInt(1, userId);
            int affectedRows = statement.executeUpdate();

            getConnection().commit();

            getConnection().setAutoCommit(true);

            closeConnection();
            return affectedRows > 0;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    closeConnection();
                }
            }
            throw new RuntimeException(e);
        }
    }
}
