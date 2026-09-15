package OMCE.OMCE.Historico.exportacao.dto;

import java.time.LocalDateTime;

/** Intervalo de datas ja normalizado (inicio no comeco do dia, fim no fim do dia). */
public record PeriodoExportacao(LocalDateTime inicio, LocalDateTime fim) {
}
