package OMCE.OMCE.AvaliacaoVendedor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvaliacaoVendedorRepository extends JpaRepository<AvaliacaoVendedor,Long> {
    Page<AvaliacaoVendedor> findByVendedorId(Long vendedorId, Pageable pageable);
    @Query("SELECT a.nota FROM AvaliacaoVendedor a WHERE a.vendedor.id = :vendedorId")
    List<Integer> pegarTodasNotasAvaliacose(@Param("vendedorId")Long vendedorId);

}
