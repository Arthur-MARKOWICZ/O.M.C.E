package OMCE.OMCE.Pedido;

import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ProdutoRepository produtoRepository;

    public void CadastroCompra(PedidoCadastroDTO dto){
        ArrayList<Produto> produtos = new ArrayList<>();
        ArrayList<Long> idsProdutos = dto.id_produtos();
        for (Long idsProduto : idsProdutos) {
            produtoRepository.produtoVendido(idsProduto);
            Produto produto = produtoRepository.getReferenceById(idsProduto);
            produtos.add(produto);
        }
        Pedido pedido = new Pedido(dto);
        pedidoRepository.save(pedido);
    }

}
