create database omce;
create table user(

    id bigint not null auto_increment,
    nome varchar(100) not null,
    email varchar(100) not null unique,
    cpf varchar(100) not null,
    telefone varchar(100) not null,
    sexo varchar(100) not null,
    data_nasc varchar(100) not null,
    nome_user varchar(100) not null unique,
    senha varchar(100) not null,
    logradouro varchar(100) not null,
    pais varchar(100) not null,
    estado varchar(100) not null,
    cidade varchar(100) not null,

    primary key(id)

);
create table produto(
    id bigint not null auto_increment,
    nome varchar(100) not null,
    preco double not null,
    detalhes TEXT not null,
    id_usuario bigint NOT NULL,

    FOREIGN KEY (id_usuario) REFERENCES user(id),

    primary key(id)
);
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
create table pagamento(
    id bigint not null auto_increment,
    pedido_id bigint not null,
    metodo_pagamento varchar(100) not null,
    valor double not null,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    primary key(id)
);
