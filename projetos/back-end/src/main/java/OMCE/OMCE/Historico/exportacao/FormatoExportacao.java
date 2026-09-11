package OMCE.OMCE.Historico.exportacao;

/**
 * Formatos aceitos pela exportacao do historico. Serve de chave para o
 * {@link ExportacaoContext} escolher a estrategia concreta.
 */
public enum FormatoExportacao {
    CSV,
    XLSX,
    PDF
}
