CREATE TABLE pagamento (
    id BIGINT NOT NULL AUTO_INCREMENT,
    pedido_id BIGINT NOT NULL,
    metodo_pagamento VARCHAR(100) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (pedido_id) REFERENCES pedido(pedido_id)
);
