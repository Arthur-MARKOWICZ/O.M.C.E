package OMCE.OMCE.Produto;

public record ProdutoRespostaDTO(Long id, String nome, Double preco,String detalhes,
                                 byte[] imagem,String image_tipo,
                                 String nomeUsuario, Condicao condicao) {
    public ProdutoRespostaDTO(Produto produto){
        this(produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                produto.getDetalhes(),
                produto.getImagem(),
                produto.getImageTipo(),
                produto.getUsuario().getNome(),
                produto.getCondicao())
        ;
    }


}
