create table produto(
 id bigint not null auto_increment,
    nome varchar(100) not null,
    preco double not null,
    detalhes TEXT not null,

    primary key(id)
);