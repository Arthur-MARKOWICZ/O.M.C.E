package OMCE.OMCE.Produto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosCadastroProduto(String nome, double preco, String detalhes ,long id_usuario,String imagem,
                                   String imagem_tipo,Categoria categoria, Condicao condicao) {
}
