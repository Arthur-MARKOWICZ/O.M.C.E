CREATE TABLE product_review (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nota INT CHECK (score BETWEEN 1 AND 5),
    comentario TEXT,
    produto_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_produto FOREIGN KEY (produto_id) REFERENCES produto(id) ON DELETE CASCADE
);
