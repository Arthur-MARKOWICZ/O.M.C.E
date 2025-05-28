CREATE TABLE avaliacao_vendedor (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nota INT CHECK (nota BETWEEN 1 AND 5),
    comentario TEXT,
    vendedor_id BIGINT NOT NULL,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user FOREIGN KEY (vendedor_id) REFERENCES user(id)
);
