package OMCE.OMCE.Historico;

import OMCE.OMCE.Pedido.Pedido;
import OMCE.OMCE.Pedido.PedidoRepository;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.ProdutoRepository;
import OMCE.OMCE.Produto.ProdutoRespostaDTO;
import OMCE.OMCE.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            Page<Pedido> pedidos = pedidoRepository.pegarProdutoIdCompradoUsuario(id_usuario, pageable);
            List<Produto> produtos = new ArrayList<>();
        for (Pedido pedido : pedidos.getContent()) {
            List<Long> idsProdutos = pedido.getId_produtos();
            for (Long id : idsProdutos) {
                produtoRepository.findById(id).ifPresent(produtos::add);
            }
        }
        Page<Produto> historicoCompra = null;
        Page<ProdutoRespostaDTO> dtosCompra = historicoCompra.map(ProdutoRespostaDTO::new);
            return dtosCompra;
    }

}
