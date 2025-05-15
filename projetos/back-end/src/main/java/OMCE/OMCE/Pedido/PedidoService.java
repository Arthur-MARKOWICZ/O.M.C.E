package OMCE.OMCE.Pedido;

import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

@Transactional
    public void CadastroCompra(PedidoCadastroDTO dto) {
        Pedido pedido = pedidoRepository.save(new Pedido(dto));

        for (Long idProduto : dto.id_produtos()) {
            produtoRepository.produtoVendido(idProduto);
            Produto produto = produtoRepository.getReferenceById(idProduto);
            ItemPedido item = new ItemPedido(pedido, produto);
            itemPedidoRepository.save(item);
        }
    }
}
