package OMCE.OMCE.Historico;

import OMCE.OMCE.Pedido.repository.ItemPedidoRepository;
import OMCE.OMCE.Pedido.repository.PedidoRepository;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.Produto.dto.ProdutoRespostaDTO;
import OMCE.OMCE.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

@Service
public class HistoricoService {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    public Page<ProdutoRespostaDTO> pegarHistoricoDeVenda(Long id_usuario, Pageable pageable){

        Page<Produto> historicoVenda = produtoRepository.pegarVendas(id_usuario, pageable);
        Page<ProdutoRespostaDTO> dtosVenda = historicoVenda.map(ProdutoRespostaDTO::new);
        return dtosVenda;
    }
    public Page<ProdutoRespostaDTO>pegarHistoricoDeCompra(Long idUsuario,@PageableDefault(size=10) Pageable pageable) {
        Page<Produto> produtos = itemPedidoRepository.pegarProdutosDoUsuario(idUsuario, pageable);
        return produtos.map(ProdutoRespostaDTO::new);
    }


}
