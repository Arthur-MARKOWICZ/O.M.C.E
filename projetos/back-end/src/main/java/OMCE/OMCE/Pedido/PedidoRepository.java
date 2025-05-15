package OMCE.OMCE.Pedido;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoRepository extends JpaRepository<Pedido,Long> {

    @Query("SELECT p FROM Pedido p WHERE p.compradorId = :compradorId")
    Page<Pedido> pegarPedidoCompradoUsuario(@Param("compradorId") Long compradorId, Pageable pageable);





}
