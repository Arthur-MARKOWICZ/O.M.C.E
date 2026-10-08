package OMCE.OMCE.Pagamento.strategy;

import OMCE.OMCE.Pagamento.enums.MetodoPagamento;
import OMCE.reuso.strategy.StrategyRegistry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PagamentoContext extends StrategyRegistry<MetodoPagamento, PagamentoStrategy> {

    public PagamentoContext(List<PagamentoStrategy> estrategias) {
        super(estrategias, PagamentoStrategy::getMetodo, "Metodo de pagamento");
    }
}