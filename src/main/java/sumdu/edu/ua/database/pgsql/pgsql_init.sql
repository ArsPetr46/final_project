CREATE TABLE IF NOT EXISTS final_project_users (
                                                   user_id SERIAL PRIMARY KEY,
                                                   username VARCHAR(50) UNIQUE NOT NULL,
                                                   password VARCHAR(50) NOT NULL,
                                                   role VARCHAR(30) NOT NULL DEFAULT 'user'
);

CREATE TABLE final_project_product_types (
                                             type_id SERIAL PRIMARY KEY,
                                             type_name VARCHAR(50) UNIQUE NOT NULL,
                                             type_description TEXT
);

CREATE TABLE IF NOT EXISTS final_project_products (
                                                      product_id SERIAL PRIMARY KEY,
                                                      product_name VARCHAR(50) UNIQUE NOT NULL,
                                                      product_description TEXT,
                                                      product_type_id INT REFERENCES final_project_product_types(type_id),
                                                      product_price DECIMAL(10, 2) NOT NULL,
                                                      product_quantity INT NOT NULL
);

CREATE TABLE IF NOT EXISTS final_project_orders (
                                                    order_id SERIAL PRIMARY KEY,
                                                    order_user_id INT REFERENCES final_project_users(user_id),
                                                    order_product_id INT REFERENCES final_project_products(product_id),
                                                    order_quantity INT NOT NULL,
                                                    order_product_price DECIMAL(10, 2) NOT NULL,
                                                    order_total_price DECIMAL(10, 2) GENERATED ALWAYS AS (order_product_price * order_quantity) STORED,
                                                    order_email VARCHAR(50) NOT NULL,
                                                    order_delivery_variant VARCHAR(30) NOT NULL,
                                                    order_payment_variant VARCHAR(30) NOT NULL,
                                                    order_status VARCHAR(30) NOT NULL DEFAULT 'in_progress'
);

CREATE TABLE IF NOT EXISTS final_project_wishlist (
                                                      product_id INT REFERENCES final_project_products(product_id),
                                                      user_id INT REFERENCES final_project_users(user_id),
                                                      PRIMARY KEY (product_id, user_id)
);