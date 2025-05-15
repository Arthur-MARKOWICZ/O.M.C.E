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
            Page<Pedido> pedidos = pedidoRepository.pegarPedidoCompradoUsuario(id_usuario, pageable);
        List<Produto> produtos = new ArrayList<>();
        List<ProdutoRespostaDTO> dtosCompra = produtos.stream()
                .map(ProdutoRespostaDTO::new)
                .collect(Collectors.toList());
            return (Page<ProdutoRespostaDTO>) dtosCompra;
    }

}
