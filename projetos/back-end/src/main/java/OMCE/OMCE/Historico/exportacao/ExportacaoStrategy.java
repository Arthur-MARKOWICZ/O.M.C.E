package OMCE.OMCE.Historico.exportacao;

import OMCE.OMCE.Historico.exportacao.dto.ArquivoExportado;

import java.time.LocalDate;

/**
 * Strategy da exportacao do historico de compras. Cada formato (CSV, XLSX, PDF)
 * e uma implementacao intercambiavel, escolhida em tempo de execucao pelo
 * {@link ExportacaoContext}.
 */
public interface ExportacaoStrategy {

    ArquivoExportado exportar(Long compradorId, LocalDate dataInicio, LocalDate dataFim);

    FormatoExportacao getFormato();

    String getContentType();

    String getExtensao();
}
