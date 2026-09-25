package OMCE.OMCE.Pagamento.strategy;

import OMCE.OMCE.Pagamento.Pagamento;
import OMCE.OMCE.Pagamento.enums.MetodoPagamento;
import OMCE.OMCE.Pedido.Pedido;

public interface PagamentoStrategy {

    Pagamento processar(Pedido pedido, double valor);

    MetodoPagamento getMetodo();
}