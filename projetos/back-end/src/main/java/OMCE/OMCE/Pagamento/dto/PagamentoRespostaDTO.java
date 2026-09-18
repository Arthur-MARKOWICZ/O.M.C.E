package OMCE.OMCE.Pagamento.dto;

import OMCE.OMCE.Pagamento.enums.MetodoPagamento;
import OMCE.OMCE.Pagamento.enums.StatusPagamento;

import java.time.LocalDateTime;

public record PagamentoRespostaDTO(
        Long id,
        Long pedidoId,
        MetodoPagamento metodoPagamento,
        double valor,
        String protocolo,
        StatusPagamento status,
        LocalDateTime dataPagamento
) {
}