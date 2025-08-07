package OMCE.OMCE.Produto.dto;

import OMCE.OMCE.Produto.enums.Categoria;
import OMCE.OMCE.Produto.enums.Condicao;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroProduto( @NotNull String nome, @NotNull  double preco, @NotNull  String detalhes ,
                                   @NotNull long id_usuario, @NotNull  String imagem,
                                   @NotNull   String imagem_tipo,
                                   Categoria categoria, Condicao condicao) {
}
