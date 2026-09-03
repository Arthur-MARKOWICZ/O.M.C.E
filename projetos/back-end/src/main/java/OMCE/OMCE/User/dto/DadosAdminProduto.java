package OMCE.OMCE.Produto.dto;

import OMCE.OMCE.Produto.enums.Categoria;

public record DadosAdminProduto(
        Long id,
        String nome,
        Double preco,
        Categoria categoria,
        boolean vendido
) {
}