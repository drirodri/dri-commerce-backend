CREATE TABLE products (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    thumbnail VARCHAR(500),
    available_quantity INTEGER NOT NULL DEFAULT 0,
    condition VARCHAR(50),
    category_id VARCHAR(100),
    seller_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT fk_products_seller FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_seller_id ON products(seller_id);
CREATE INDEX idx_products_active ON products(active);
CREATE INDEX idx_products_price ON products(price);
