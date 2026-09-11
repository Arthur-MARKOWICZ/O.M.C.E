package OMCE.OMCE.Historico.exportacao.dto;

import OMCE.OMCE.Pedido.ItemPedido;
import OMCE.OMCE.Produto.Produto;

import java.time.LocalDateTime;

/**
 * Uma linha do relatorio. A quantidade e fixa em 1 porque o PedidoService grava
 * um ItemPedido por produto, e o preco unitario e lido do produto (nao ha snapshot
 * de preco no momento da compra).
 */
public record LinhaHistoricoDTO(Long pedidoId, LocalDateTime dataCompra, String produto, String vendedor,
                                String categoria, String condicao, int quantidade, double precoUnitario,
                                double total, byte[] imagem, String imagemTipo) {

    public LinhaHistoricoDTO(ItemPedido item) {
        this(item.getPedido().getId(),
                item.getPedido().getDataPedido(),
                item.getProduto().getNome(),
                item.getProduto().getUsuario() != null ? item.getProduto().getUsuario().getNome() : "",
                nomeEnum(item.getProduto()),
                item.getProduto().getCondicao() != null ? item.getProduto().getCondicao().name() : "",
                1,
                precoDe(item.getProduto()),
                precoDe(item.getProduto()),
                item.getProduto().getImagem(),
                item.getProduto().getImageTipo());
    }

    private static String nomeEnum(Produto produto) {
        return produto.getCategoria() != null ? produto.getCategoria().name() : "";
    }

    private static double precoDe(Produto produto) {
        return produto.getPreco() != null ? produto.getPreco() : 0d;
    }
}
