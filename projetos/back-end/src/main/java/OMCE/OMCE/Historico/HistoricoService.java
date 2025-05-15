package OMCE.OMCE.Historico;

import OMCE.OMCE.Pedido.PedidoRepository;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.ProdutoRepository;
import OMCE.OMCE.Produto.ProdutoRespostaDTO;
import OMCE.OMCE.User.UserRepository;
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
    private PedidoRepository pedidoRepository;

    public Page<ProdutoRespostaDTO> pegarHistoricoDeVenda(Long id_usuario, Pageable pageable){

        Page<Produto> historicoVenda = produtoRepository.pegarVendas(id_usuario, pageable);
        Page<ProdutoRespostaDTO> dtosVenda = historicoVenda.map(ProdutoRespostaDTO::new);
        return dtosVenda;
    }
    public Page<ProdutoRespostaDTO> pegarHistoricoDeCompra(Long id_usuario,Pageable pageable){
        Page<Produto> historicoCompra = pedidoRepository.pegarProdutoCompradoUsuario(id_usuario, pageable);
        Page<ProdutoRespostaDTO> dtosCompra = historicoCompra.map(ProdutoRespostaDTO::new);
        return dtosCompra;
    }

}
