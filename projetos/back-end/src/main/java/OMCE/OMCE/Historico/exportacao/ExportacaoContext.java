package OMCE.OMCE.Historico.exportacao;

import OMCE.reuso.strategy.StrategyRegistry;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Context do padrao Strategy da exportacao: o Spring injeta todas as implementacoes de
 * {@link ExportacaoStrategy}, entao um formato novo e so uma classe nova. O registro e a
 * escolha vem do {@link StrategyRegistry} (modulo omce-reuso).
 */
@Component
public class ExportacaoContext extends StrategyRegistry<FormatoExportacao, ExportacaoStrategy> {

    public ExportacaoContext(List<ExportacaoStrategy> estrategias) {
        super(estrategias, ExportacaoStrategy::getFormato, "Formato de exportacao");
    }
}
