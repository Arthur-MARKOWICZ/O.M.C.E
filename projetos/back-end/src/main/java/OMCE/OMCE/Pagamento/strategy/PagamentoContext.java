package OMCE.OMCE.Pagamento.strategy;

import OMCE.OMCE.Pagamento.enums.MetodoPagamento;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PagamentoContext {

    private final Map<MetodoPagamento, PagamentoStrategy> estrategias;

    public PagamentoContext(List<PagamentoStrategy> estrategias) {
        this.estrategias = estrategias.stream()
                .collect(Collectors.toMap(PagamentoStrategy::getMetodo, Function.identity()));
    }

    public PagamentoStrategy escolher(MetodoPagamento metodo) {
        PagamentoStrategy estrategia = estrategias.get(metodo);
        if (estrategia == null) {
            throw new IllegalArgumentException("Metodo de pagamento nao suportado: " + metodo);
        }
        return estrategia;
    }
}