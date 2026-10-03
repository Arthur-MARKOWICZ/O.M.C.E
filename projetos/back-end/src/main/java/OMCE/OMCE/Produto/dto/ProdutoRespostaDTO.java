package OMCE.OMCE.Produto.dto;

import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.enums.Condicao;

public record ProdutoRespostaDTO(Long id, String nome, Double preco, String detalhes,
                                 byte[] imagem, String image_tipo,
                                 String nomeUsuario, Condicao condicao, Long id_vendedor, Double valorFrete) {
    public ProdutoRespostaDTO(Produto produto){
        this(produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                produto.getDetalhes(),
                produto.getImagem(),
                produto.getImageTipo(),
                produto.getUsuario().getNome(),
                produto.getCondicao(),
                produto.getUsuario().getId(),
                null)
        ;
    }

    
    public ProdutoRespostaDTO(Produto produto, Double valorPago){
        this(produto.getId(),
                produto.getNome(),
                valorPago,
                produto.getDetalhes(),
                produto.getImagem(),
                produto.getImageTipo(),
                produto.getUsuario().getNome(),
                produto.getCondicao(),
                produto.getUsuario().getId(),
                null)
        ;
    }

    public ProdutoRespostaDTO(Produto produto, Double valorPago, Double valorFrete){
        this(produto.getId(),
                produto.getNome(),
                valorPago,
                produto.getDetalhes(),
                produto.getImagem(),
                produto.getImageTipo(),
                produto.getUsuario().getNome(),
                produto.getCondicao(),
                produto.getUsuario().getId(),
                valorFrete)
        ;
    }

}