package sumdu.edu.ua.database.pgsql;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import sumdu.edu.ua.database.DatabaseConnector;
import sumdu.edu.ua.models.Product;

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
            if (e.getSQLState().equals("23505")) {
                return false;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<Product> getProducts(String username, String name, String filter, String sort, String order, boolean wishlisted) {
        try {
            String query = "SELECT *\n" +
                    "FROM final_project_products_for_sale pfs\n" +
                    "JOIN final_project_storage s on pfs.product_id = s.product_id\n" +
                    "LEFT JOIN final_project_wishlist w on pfs.product_id = w.product_id\n" +
                    "WHERE (user_id = (SELECT user_id FROM final_project_users WHERE username = ?)\n";

            if (wishlisted) {
                query = query + ")\n";
            } else {
                query = query + "OR w.user_id IS NULL)\n";
            }

            if (!name.isEmpty()) {
                query = query + "AND (product_name LIKE '%" + name + "%')\n";
            }

            if (!filter.equals("All")) {
                query = query + "AND (product_type = '" + filter + "')\n";
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
            query = query + ";";

            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getString("product_type"),
                        resultSet.getDouble("product_price"),
                        "none",
                        "none",
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
    public List<String> getProductTypes() {
        try {
            String query = "SELECT DISTINCT product_type FROM final_project_storage;";
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<String> types = new ArrayList<>();
            while (resultSet.next()) {
                types.add(resultSet.getString("product_type"));
            }

            closeConnection();
            return types;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product getProductById(int productId) {
        try {
            String query = "SELECT * FROM final_project_products_for_sale pfs\n" +
                    "JOIN final_project_storage s on pfs.product_id = s.product_id\n" +
                    "WHERE pfs.product_id = ?;";
            PreparedStatement statement = getConnection().prepareStatement(query);

            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            Product product = new Product(
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getString("product_type"),
                    resultSet.getDouble("product_price"),
                    "none",
                    "none",
                    resultSet.getInt("product_quantity"),
                    false
            );

            closeConnection();
            return product;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCurrentQuantity(int productId) {
        try {
            String query = "SELECT product_quantity FROM final_project_storage WHERE product_id = ?;";
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
    public void insertOrder(String username, int productId, double price, int quantity, String email, String payment, String delivery) {
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

            query = "UPDATE final_project_storage " +
                    "SET product_quantity = product_quantity - ? " +
                    "WHERE product_id = ?;";
            statement = getConnection().prepareStatement(query);

            statement.setInt(1, quantity);
            statement.setInt(2, productId);
            statement.executeUpdate();

            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
