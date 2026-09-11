package OMCE.OMCE.Historico.exportacao.dto;

/** Arquivo pronto para download: nome, content type e os bytes gerados pela estrategia. */
public record ArquivoExportado(String nomeArquivo, String contentType, byte[] conteudo) {
}
