CREATE TABLE avaliacao_produto (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nota INT CHECK (nota BETWEEN 1 AND 5),
    comentario TEXT,
    produto_id BIGINT NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_produto FOREIGN KEY (produto_id) REFERENCES produto(id) ON DELETE CASCADE
);
