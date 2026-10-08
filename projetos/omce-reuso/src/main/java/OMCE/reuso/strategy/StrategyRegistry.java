package OMCE.reuso.strategy;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;


public class StrategyRegistry<K, S> {

    private final Map<K, S> estrategias;
    private final String descricao;

    public StrategyRegistry(Collection<? extends S> estrategias,
                            Function<? super S, ? extends K> extratorChave,
                            String descricao) {
        Objects.requireNonNull(estrategias, "estrategias");
        Objects.requireNonNull(extratorChave, "extratorChave");
        this.descricao = Objects.requireNonNull(descricao, "descricao");

        Map<K, S> mapa = new LinkedHashMap<>();
        for (S estrategia : estrategias) {
            K chave = extratorChave.apply(estrategia);
            S anterior = mapa.putIfAbsent(chave, estrategia);
            if (anterior != null) {
                throw new IllegalStateException(
                        "Estrategia duplicada para a chave " + chave + " em " + descricao);
            }
        }
        this.estrategias = Collections.unmodifiableMap(mapa);
    }

    public S escolher(K chave) {
        S estrategia = estrategias.get(chave);
        if (estrategia == null) {
            throw new IllegalArgumentException(descricao + " nao suportado: " + chave);
        }
        return estrategia;
    }

    public boolean suporta(K chave) {
        return estrategias.containsKey(chave);
    }

    public List<S> todas() {
        return List.copyOf(estrategias.values());
    }
}
