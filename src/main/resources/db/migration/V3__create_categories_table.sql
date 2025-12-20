-- Tabela de categorias com ID incremental
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index no nome para buscas
CREATE INDEX idx_categories_name ON categories(name);

-- Alterar coluna category_id em products para INTEGER e adicionar FK
ALTER TABLE products 
    ALTER COLUMN category_id DROP DEFAULT,
    ALTER COLUMN category_id TYPE INTEGER USING NULL;

ALTER TABLE products
    ADD CONSTRAINT fk_products_category 
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL;
