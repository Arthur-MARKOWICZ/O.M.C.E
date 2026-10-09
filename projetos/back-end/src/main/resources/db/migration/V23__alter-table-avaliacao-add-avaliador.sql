ALTER TABLE avaliacao_produto
    ADD COLUMN avaliador_id BIGINT NULL,
    ADD CONSTRAINT fk_avaliacao_produto_avaliador
        FOREIGN KEY (avaliador_id) REFERENCES user(id);

ALTER TABLE avaliacao_vendedor
    ADD COLUMN avaliador_id BIGINT NULL,
    ADD CONSTRAINT fk_avaliacao_vendedor_avaliador
        FOREIGN KEY (avaliador_id) REFERENCES user(id);
