package OMCE.OMCE.Pagamento.strategy;

import OMCE.OMCE.Pagamento.Pagamento;
import OMCE.OMCE.Pagamento.enums.MetodoPagamento;
import OMCE.OMCE.Pagamento.enums.StatusPagamento;
import OMCE.OMCE.Pedido.Pedido;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CartaoCreditoPagamento implements PagamentoStrategy {

    @Override
    public Pagamento processar(Pedido pedido, double valor) {
        Pagamento pagamento = new Pagamento(pedido, MetodoPagamento.CARTAO_CREDITO, valor);
        pagamento.setProtocolo("CC-" + UUID.randomUUID());
        pagamento.setStatus(StatusPagamento.APROVADO);
        pagamento.setDataPagamento(LocalDateTime.now());
        return pagamento;
    }

    @Override
    public MetodoPagamento getMetodo() {
        return MetodoPagamento.CARTAO_CREDITO;
    }
}