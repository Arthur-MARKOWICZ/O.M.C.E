CREATE TABLE itens_pedido (
    item_pedido_id BIGINT PRIMARY KEY,
    pedido_id BIGINT,
    produto_id BIGINT,
    quantidade BIGINT,
    FOREIGN KEY (pedido_id) REFERENCES pedido(pedido_id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);