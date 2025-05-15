package OMCE.OMCE.Pedido;

import OMCE.OMCE.Produto.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido,Long> {
    @Query("SELECT i.produto FROM ItemPedido i WHERE i.pedido.id = :pedido_id")
    Page<Produto> pegarProdutosDoPedido(@Param("pedido_id") Long pedidoId, Pageable pageable);
    @Query("""
        SELECT i.produto 
        FROM ItemPedido i 
        JOIN i.pedido p 
        WHERE p.compradorId = :id_usuario
    """)
    Page<Produto> pegarProdutosDoUsuario(@Param("id_usuario")Long idUsuario, Pageable pageable);
}
