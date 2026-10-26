ALTER TABLE produto DROP FOREIGN KEY produto_ibfk_1;
ALTER TABLE produto MODIFY id_usuario BIGINT NULL;
ALTER TABLE produto ADD CONSTRAINT fk_produto_usuario
    FOREIGN KEY (id_usuario) REFERENCES user(id) ON DELETE SET NULL;

ALTER TABLE pedido DROP FOREIGN KEY pedido_ibfk_1;
ALTER TABLE pedido MODIFY comprador_id BIGINT NULL;
ALTER TABLE pedido ADD CONSTRAINT fk_pedido_comprador
    FOREIGN KEY (comprador_id) REFERENCES user(id) ON DELETE SET NULL;

ALTER TABLE avaliacao_produto DROP FOREIGN KEY fk_avaliacao_produto_avaliador;
ALTER TABLE avaliacao_produto ADD CONSTRAINT fk_avaliacao_produto_avaliador
    FOREIGN KEY (avaliador_id) REFERENCES user(id) ON DELETE SET NULL;

ALTER TABLE avaliacao_vendedor DROP FOREIGN KEY fk_avaliacao_vendedor_avaliador;
ALTER TABLE avaliacao_vendedor ADD CONSTRAINT fk_avaliacao_vendedor_avaliador
    FOREIGN KEY (avaliador_id) REFERENCES user(id) ON DELETE SET NULL;