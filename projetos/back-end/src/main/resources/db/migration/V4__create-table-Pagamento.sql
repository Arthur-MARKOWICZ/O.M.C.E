create table pagamento(
    id bigint not null auto_increment,
    metodo_pagamento varchar(100) not null,
    valor double not null,
    primary key(id)
);