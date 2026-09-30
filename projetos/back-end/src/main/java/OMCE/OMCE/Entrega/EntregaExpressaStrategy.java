package OMCE.OMCE.Entrega;

import OMCE.OMCE.Enderco.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EntregaExpressaStrategy implements EntregaStrategy {

    private static final double TAXA_FIXA = 34.90;
    private static final int PRAZO_DIAS = 2;

    @Override
    public EntregaCalculada calcular(double valorPedido, Endereco endereco) {
        return new EntregaCalculada(getTipo(), "Entrega expressa", TAXA_FIXA, PRAZO_DIAS);
    }

    @Override
    public TipoEntrega getTipo() {
        return TipoEntrega.EXPRESSA;
    }
}
