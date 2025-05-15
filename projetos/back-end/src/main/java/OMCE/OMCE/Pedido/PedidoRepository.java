package OMCE.OMCE.Pedido;

import OMCE.OMCE.Produto.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface PedidoRepository extends JpaRepository<Pedido,Long> {

    @Query("SELECT prod FROM Pedido p JOIN p.produtos prod WHERE p.id_comprador.id = :id_comprador")
    Page<Produto> pegarProdutoCompradoUsuario(@Param("id_comprador") Long id_comprador, Pageable pageable);

}
