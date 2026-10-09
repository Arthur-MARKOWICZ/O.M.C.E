package OMCE.OMCE.AvaliacaoProduto.dto;

import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProduto;

import java.time.LocalDateTime;

public record AvaliacaoProdutoRespostaDTO(
        Long id,
        int nota,
        String comentario,
        Long produto_id,
        String nome_produto,
        LocalDateTime data
) {

    public AvaliacaoProdutoRespostaDTO(AvaliacaoProduto avaliacao) {

        this(
                avaliacao.getId(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getProduto().getId(),
                avaliacao.getProduto().getNome(),
                avaliacao.getData()
        );
    }
}
