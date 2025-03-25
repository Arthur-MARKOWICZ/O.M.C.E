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
    role text NOT NULL DEFAULT 'user',
    logradouro varchar(100) not null,
    pais varchar(100) not null,
    estado varchar(100) not null,
    cidade varchar(100) not null,

    primary key(id)

);
