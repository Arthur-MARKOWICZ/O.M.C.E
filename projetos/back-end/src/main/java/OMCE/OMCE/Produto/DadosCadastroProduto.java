package OMCE.OMCE.Produto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroProduto(String subistituir,@NotNull String nome,@NotNull  double preco,@NotNull  String detalhes ,
                                   @NotNull long id_usuario,@NotNull  String imagem,
                                   @NotNull   String imagem_tipo,
                                   Categoria categoria, Condicao condicao) {
}
