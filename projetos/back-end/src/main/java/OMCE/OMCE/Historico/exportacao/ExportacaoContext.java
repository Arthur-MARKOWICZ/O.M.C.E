package OMCE.OMCE.Historico.exportacao;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Context do padrao Strategy: guarda as estrategias disponiveis e devolve a
 * adequada ao formato pedido. O Spring injeta todas as implementacoes de
 * {@link ExportacaoStrategy}, entao um formato novo e so uma classe nova.
 */
@Component
public class ExportacaoContext {

    private final Map<FormatoExportacao, ExportacaoStrategy> estrategias;

    public ExportacaoContext(List<ExportacaoStrategy> estrategias) {
        this.estrategias = estrategias.stream()
                .collect(Collectors.toMap(ExportacaoStrategy::getFormato, Function.identity()));
    }

    public ExportacaoStrategy escolher(FormatoExportacao formato) {
        ExportacaoStrategy estrategia = estrategias.get(formato);
        if (estrategia == null) {
            throw new IllegalArgumentException("Formato de exportacao nao suportado: " + formato);
        }
        return estrategia;
    }
}
