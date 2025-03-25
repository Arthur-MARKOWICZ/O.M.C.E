create table produto(
    id bigint not null auto_increment,
    nome varchar(100) not null,
    preco double not null,
    detalhes TEXT not null,
    id_usuario bigint NOT NULL,

    FOREIGN KEY (id_usuario) REFERENCES user(id),

    primary key(id)
);
