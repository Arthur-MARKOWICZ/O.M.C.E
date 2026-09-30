package OMCE.OMCE.Entrega;

import OMCE.OMCE.Enderco.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EntregaRetiradaStrategy implements EntregaStrategy {

    private static final int PRAZO_DIAS = 1;

    @Override
    public EntregaCalculada calcular(double valorPedido, Endereco endereco) {
        return new EntregaCalculada(getTipo(), "Retirada na loja", 0.0, PRAZO_DIAS);
    }

    @Override
    public TipoEntrega getTipo() {
        return TipoEntrega.RETIRADA;
    }
}
