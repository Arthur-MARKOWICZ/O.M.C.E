package OMCE.OMCE.AvaliacaoProduto.dto;

import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProduto;

import java.time.LocalDateTime;

public record AvaliacaoProdutoRespostaDTO(
        int nota,
        String comentario,
        Long produto_id,
        LocalDateTime data
) {

    public AvaliacaoProdutoRespostaDTO(AvaliacaoProduto avaliacao) {

        this(
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getProduto().getId(),
                avaliacao.getData()
        );
    }
}