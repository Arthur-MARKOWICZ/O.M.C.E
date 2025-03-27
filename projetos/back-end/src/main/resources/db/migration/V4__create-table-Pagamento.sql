create table pagamento(
    id bigint not null auto_increment,
    pedido_id bigint not null,
    metodo_pagamento varchar(100) not null,
    valor double not null,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    primary key(id)
);