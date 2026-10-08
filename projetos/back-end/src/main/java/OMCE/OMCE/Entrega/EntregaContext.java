package OMCE.OMCE.Entrega;

import OMCE.reuso.strategy.StrategyRegistry;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Context do Strategy de entrega, baseado no {@link StrategyRegistry} do omce-reuso.
 * Mantem a regra propria do dominio: sem tipo informado, usa a entrega padrao.
 */
@Component
public class EntregaContext extends StrategyRegistry<TipoEntrega, EntregaStrategy> {

    public EntregaContext(List<EntregaStrategy> estrategias) {
        super(estrategias, EntregaStrategy::getTipo, "Tipo de entrega");
    }

    @Override
    public EntregaStrategy escolher(TipoEntrega tipo) {
        return super.escolher(tipo != null ? tipo : TipoEntrega.PADRAO);
    }
}
