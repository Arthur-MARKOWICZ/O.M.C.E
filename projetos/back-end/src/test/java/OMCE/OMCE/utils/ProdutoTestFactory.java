package OMCE.OMCE.utils;

import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.Produto.enums.Categoria;
import OMCE.OMCE.Produto.enums.Condicao;

import java.util.Base64;

/**
 * Monta produtos nos testes sem depender dos campos especificos de cada categoria
 * e sem duplicar o Template Method de cadastro.
 */
public class ProdutoTestFactory {

    public static DadosCadastroProduto dados(
            String nome,
            double preco,
            String detalhes,
            long id_usuario,
            String imagem,
            String imagem_tipo,
            Categoria categoria,
            Condicao condicao) {

        return new DadosCadastroProduto(
                nome, preco, detalhes, id_usuario, imagem, imagem_tipo,
                categoria, condicao,
                null, null, null, null, null
        );
    }

    public static Produto produto(DadosCadastroProduto dados) {

        Produto produto = new Produto();

        produto.setNome(dados.nome());
        produto.setPreco(dados.preco());
        produto.setDetalhes(dados.detalhes());
        produto.setVendido(false);
        produto.setCondicao(dados.condicao());
        produto.setCategoria(dados.categoria());

        if (dados.imagem() != null) {
            produto.setImagem(Base64.getDecoder().decode(dados.imagem()));
            produto.setImageTipo(dados.imagem_tipo());
        }

        return produto;
    }
}
