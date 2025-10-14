package OMCE.OMCE.Pedido.service;

import OMCE.OMCE.NotificacaoEmail.Compra;
import OMCE.OMCE.Pedido.ItemPedido;
import OMCE.OMCE.Pedido.Pedido;
import OMCE.OMCE.Pedido.dto.PedidoCadastroDTO;
import OMCE.OMCE.Pedido.repository.ItemPedidoRepository;
import OMCE.OMCE.Pedido.repository.PedidoRepository;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final List<Compra> observers;

    public PedidoService(PedidoRepository pedidoRepository,
                         ProdutoRepository produtoRepository,
                         ItemPedidoRepository itemPedidoRepository,
                         List<Compra> observers) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.observers = observers;
    }

    private void notificarObservers(Pedido pedido) {
        for (Compra observer : observers) {
            observer.atualizar(pedido);
        }
    }

    @Transactional
    public void CadastroCompra(PedidoCadastroDTO dto) {
        Pedido pedido = new Pedido(dto);
        pedido.setCompradorId(dto.id_comprador());
        pedidoRepository.save(pedido);
        for (Long idProduto : dto.id_produtos()) {
            Produto produto = produtoRepository.getReferenceById(idProduto);
            produto.setVendido(true);
            produtoRepository.save(produto);

            ItemPedido item = new ItemPedido(pedido, produto);
            itemPedidoRepository.save(item);
        }
        notificarObservers(pedido);
    }
}
