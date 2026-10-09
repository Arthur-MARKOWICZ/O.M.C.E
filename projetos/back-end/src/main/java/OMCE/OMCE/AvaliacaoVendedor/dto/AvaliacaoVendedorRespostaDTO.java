package OMCE.OMCE.AvaliacaoVendedor.dto;

import OMCE.OMCE.AvaliacaoVendedor.AvaliacaoVendedor;

import java.time.LocalDateTime;

public record AvaliacaoVendedorRespostaDTO(
        Long id,
        int nota,
        String comentario,
        Long vendedor_id,
        String nome_vendedor,
        LocalDateTime data
) {

    public AvaliacaoVendedorRespostaDTO(AvaliacaoVendedor avaliacao) {
        this(
                avaliacao.getId(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getVendedor().getId(),
                avaliacao.getVendedor().getNome(),
                avaliacao.getData()
        );
    }
}
