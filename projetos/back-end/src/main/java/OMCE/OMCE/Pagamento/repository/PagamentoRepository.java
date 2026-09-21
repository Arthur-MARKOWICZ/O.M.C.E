package OMCE.OMCE.Pagamento.repository;

import OMCE.OMCE.Pagamento.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    Optional<Pagamento> findByPedidoId(Long pedidoId);

    List<Pagamento> findByPedidoIdIn(List<Long> pedidoIds);
}