package OMCE.OMCE.Entrega;

import OMCE.OMCE.Enderco.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EntregaPadraoStrategy implements EntregaStrategy {

    private static final double VALOR_MINIMO_FRETE_GRATIS = 150.0;
    private static final double TAXA_FIXA = 19.90;
    private static final int PRAZO_DIAS = 7;

    @Override
    public EntregaCalculada calcular(double valorPedido, Endereco endereco) {
        double frete = valorPedido >= VALOR_MINIMO_FRETE_GRATIS ? 0.0 : TAXA_FIXA;
        return new EntregaCalculada(getTipo(), "Entrega padrão", frete, PRAZO_DIAS);
    }

    @Override
    public TipoEntrega getTipo() {
        return TipoEntrega.PADRAO;
    }
}
