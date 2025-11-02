CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role INTEGER NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT true
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_active ON users(active);
