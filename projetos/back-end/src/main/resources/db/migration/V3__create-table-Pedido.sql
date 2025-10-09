CREATE TABLE pedido (
    pedido_id BIGINT NOT NULL AUTO_INCREMENT,
    comprador_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    valor DOUBLE NOT NULL,
    logradouro VARCHAR(100) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    estado VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    PRIMARY KEY (pedido_id),
    FOREIGN KEY (comprador_id) REFERENCES user(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);