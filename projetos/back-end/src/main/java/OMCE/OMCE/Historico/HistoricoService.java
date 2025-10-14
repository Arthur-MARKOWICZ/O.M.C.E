package OMCE.OMCE.Historico;

import OMCE.OMCE.Pedido.repository.ItemPedidoRepository;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.ProdutoRespostaDTO;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class HistoricoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    public Page<ProdutoRespostaDTO> pegarHistoricoDeVenda(Long idUsuario, Pageable pageable){
        Page<Produto> historicoVenda = produtoRepository.pegarVendas(idUsuario, pageable);
        return historicoVenda.map(ProdutoRespostaDTO::new);
    }

    public Page<ProdutoRespostaDTO> pegarHistoricoDeCompra(Long idUsuario, Pageable pageable) {
        Page<Produto> produtosComprados = itemPedidoRepository.pegarProdutosDoUsuario(idUsuario, pageable);
        return produtosComprados.map(ProdutoRespostaDTO::new);
    }
}
