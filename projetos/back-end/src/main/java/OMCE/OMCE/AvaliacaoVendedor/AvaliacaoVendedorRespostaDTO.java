package OMCE.OMCE.AvaliacaoVendedor;

import java.time.LocalDateTime;

public record AvaliacaoVendedorRespostaDTO(int nota, String comentario, Long vendedor_id, LocalDateTime data) {

    public AvaliacaoVendedorRespostaDTO(AvaliacaoVendedor avaliacao) {
        this(avaliacao.getNota(), avaliacao.getComentario(), avaliacao.getVendedor().getId(), avaliacao.getData());
    }
}
