package OMCE.OMCE.Pedido.service;

import OMCE.OMCE.Enderco.Endereco;
import OMCE.OMCE.Entrega.EntregaCalculada;
import OMCE.OMCE.Entrega.EntregaContext;
import OMCE.OMCE.Execao.ProdutoNaoEncontrado;
import OMCE.OMCE.Pagamento.service.PagamentoService;
import OMCE.OMCE.Pedido.ItemPedido;
import OMCE.OMCE.Pedido.Pedido;
import OMCE.OMCE.Pedido.dto.PedidoCadastroDTO;
import OMCE.OMCE.Pedido.repository.ItemPedidoRepository;
import OMCE.OMCE.Pedido.repository.PedidoRepository;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
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
    @Autowired
    private PagamentoService pagamentoService;
    @Autowired
    private EntregaContext entregaContext;

@Transactional
    public void CadastroCompra(PedidoCadastroDTO dto) {
        Endereco endereco = new Endereco(dto.endereco());
        EntregaCalculada entrega = entregaContext.escolher(dto.tipoEntrega()).calcular(dto.valor(), endereco);
        Pedido pedido = pedidoRepository.save(new Pedido(dto, entrega));

        for (Long idProduto : dto.id_produtos()) {
            produtoRepository.produtoVendido(idProduto);
            Produto produto = produtoRepository.findById(idProduto)
                    .orElseThrow(() -> new ProdutoNaoEncontrado("Produto não encontrado com id: " + idProduto));
            ItemPedido item = new ItemPedido(pedido, produto);
            itemPedidoRepository.save(item);
        }

        pagamentoService.registrarPagamento(pedido, dto.metodoPagamento(), pedido.getValor());
    }
}