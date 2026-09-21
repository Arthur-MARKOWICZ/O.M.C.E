package OMCE.OMCE.Entrega;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EntregaContext {

    private final Map<TipoEntrega, EntregaStrategy> estrategias;

    public EntregaContext(List<EntregaStrategy> estrategias) {
        this.estrategias = estrategias.stream()
                .collect(Collectors.toMap(EntregaStrategy::getTipo, Function.identity()));
    }

    public EntregaStrategy escolher(TipoEntrega tipo) {
        TipoEntrega tipoEscolhido = tipo != null ? tipo : TipoEntrega.PADRAO;
        EntregaStrategy estrategia = estrategias.get(tipoEscolhido);
        if (estrategia == null) {
            throw new IllegalArgumentException("Tipo de entrega nao suportado: " + tipoEscolhido);
        }
        return estrategia;
    }

    public List<EntregaStrategy> todas() {
        return List.copyOf(estrategias.values());
    }
}
