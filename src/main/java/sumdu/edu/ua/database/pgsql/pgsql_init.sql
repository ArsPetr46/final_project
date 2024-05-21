DO $$ BEGIN
    CREATE TYPE user_role AS ENUM ('user', 'admin');
    CREATE TYPE delivery_variants AS ENUM ('courier', 'post', 'pickup');
    CREATE TYPE payment_variants AS ENUM ('cash', 'card', 'paypal');
    CREATE TYPE order_status AS ENUM ('in_progress', 'completed', 'canceled');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

CREATE TABLE IF NOT EXISTS final_project_users (
                                                   user_id SERIAL PRIMARY KEY,
                                                   username VARCHAR(50) UNIQUE NOT NULL,
                                                   password VARCHAR(50) NOT NULL,
                                                   role user_role NOT NULL DEFAULT 'user'
);

CREATE TABLE IF NOT EXISTS final_project_storage (
                                                     product_id SERIAL PRIMARY KEY,
                                                     product_base_price DECIMAL(10, 2) NOT NULL,
                                                     product_quantity INT UNIQUE NOT NULL
);

DROP TABLE final_project_products_for_sale;

CREATE TABLE IF NOT EXISTS final_project_products_for_sale (
                                                               product_id INT PRIMARY KEY REFERENCES final_project_storage(product_id),
                                                               product_price DECIMAL(10, 2) NOT NULL,
                                                               product_payment_variant payment_variants NOT NULL,
                                                               product_delivery_variant delivery_variants NOT NULL
);

CREATE TABLE IF NOT EXISTS final_project_orders (
                                                    order_id SERIAL PRIMARY KEY,
                                                    order_part_id INT REFERENCES final_project_products_for_sale(product_id),
                                                    order_part_quantity INT NOT NULL,
                                                    order_part_price DECIMAL(10, 2) NOT NULL,
                                                    user_id INT REFERENCES final_project_users(user_id),
                                                    order_status order_status NOT NULL
);

CREATE TABLE IF NOT EXISTS final_project_wishlist (
                                                      product_id INT REFERENCES final_project_storage(product_id),
                                                      user_id INT REFERENCES final_project_users(user_id),
                                                      PRIMARY KEY (product_id, user_id)
);