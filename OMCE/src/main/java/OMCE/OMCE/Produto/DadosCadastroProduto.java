package OMCE.OMCE.Produto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosCadastroProduto(String nome, double preco, String detalhes , @JsonAlias("id_usuario") long id_user) {
}
