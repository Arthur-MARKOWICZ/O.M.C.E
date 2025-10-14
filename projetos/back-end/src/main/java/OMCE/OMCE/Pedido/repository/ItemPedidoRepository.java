package OMCE.OMCE.Pedido.repository;

import OMCE.OMCE.Pedido.ItemPedido;
import OMCE.OMCE.Produto.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido,Long> {

    @Query("SELECT i.produto FROM ItemPedido i WHERE i.pedido.id = :pedidoId")
    Page<Produto> pegarProdutosDoPedido(@Param("pedidoId") Long pedidoId, Pageable pageable);

    @Query("SELECT i.produto FROM ItemPedido i JOIN i.pedido p WHERE p.compradorId = :idUsuario")
    Page<Produto> pegarProdutosDoUsuario(@Param("idUsuario") Long idUsuario, Pageable pageable);

    @Query("SELECT i.produto FROM ItemPedido i WHERE i.pedido.id = :pedidoId")
    List<Produto> findProdutosByPedidoId(@Param("pedidoId") Long pedidoId);
}
