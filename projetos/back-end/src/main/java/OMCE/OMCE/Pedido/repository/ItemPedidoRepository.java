package OMCE.OMCE.Pedido.repository;

import OMCE.OMCE.Pedido.ItemPedido;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.enums.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido,Long> {
    @Query("SELECT i.produto FROM ItemPedido i WHERE i.pedido.id = :pedido_id")
    Page<Produto> pegarProdutosDoPedido(@Param("pedido_id") Long pedidoId, Pageable pageable);
    @Query("""
        SELECT i.produto
        FROM ItemPedido i
        JOIN i.pedido p
        WHERE p.compradorId = :id_usuario
    """)
    Page<Produto>pegarProdutosDoUsuario(@Param("id_usuario")Long idUsuario, Pageable pageable);
    @Query("""
        SELECT i
        FROM ItemPedido i
        JOIN FETCH i.pedido p
        JOIN FETCH i.produto prod
        WHERE p.compradorId = :id_usuario
          AND (:nome IS NULL OR LOWER(prod.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
          AND (:categoria IS NULL OR prod.categoria = :categoria)
          AND (:precoMin IS NULL OR (prod.preco + p.valorFrete) >= :precoMin)
          AND (:precoMax IS NULL OR (prod.preco + p.valorFrete) <= :precoMax)
    """)
    Page<ItemPedido> filtrarComprasDoUsuario(@Param("id_usuario") Long idUsuario,
                                             @Param("nome") String nome,
                                             @Param("categoria") Categoria categoria,
                                             @Param("precoMin") Double precoMin,
                                             @Param("precoMax") Double precoMax,
                                             Pageable pageable);

    default Page<ItemPedido> pegarComprasDoUsuario(Long idUsuario, Pageable pageable) {
        return filtrarComprasDoUsuario(idUsuario, null, null, null, null, pageable);
    }
    @Query("""
        SELECT i
        FROM ItemPedido i
        JOIN FETCH i.pedido p
        JOIN FETCH i.produto prod
        LEFT JOIN FETCH prod.usuario
        WHERE p.compradorId = :id_usuario
          AND p.dataPedido BETWEEN :inicio AND :fim
        ORDER BY p.dataPedido DESC, i.itemPedidoId
    """)
    List<ItemPedido> pegarComprasNoPeriodo(@Param("id_usuario") Long idUsuario,
                                           @Param("inicio") LocalDateTime inicio,
                                           @Param("fim") LocalDateTime fim);
}