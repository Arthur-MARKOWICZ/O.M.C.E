package OMCE.OMCE.Entrega;

import OMCE.OMCE.Enderco.Endereco;

public interface EntregaStrategy {

    EntregaCalculada calcular(double valorPedido, Endereco endereco);

    TipoEntrega getTipo();
}