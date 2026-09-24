package OMCE.OMCE.Avaliacao.service;

import java.util.List;

public abstract class AvaliacaoTemplateService<D, A> {

    // Template Method
    public final void criar(D dto) {

        A avaliacao = criarAvaliacao(dto);

        salvarAvaliacao(avaliacao);
    }

    // Template Method para cálculo da média
    public final double calcularMedia(Long id) {

        List<Integer> notas = buscarNotas(id);

        if (notas == null || notas.isEmpty()) {
            return 0.0;
        }

        return notas.stream()
                .mapToDouble(Integer::doubleValue)
                .average()
                .orElse(0.0);
    }

    // Passos específicos de cada tipo de avaliação
    protected abstract A criarAvaliacao(D dto);

    protected abstract void salvarAvaliacao(A avaliacao);

    protected abstract List<Integer> buscarNotas(Long id);
}