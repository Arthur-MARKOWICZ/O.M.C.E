package OMCE.OMCE.AvaliacaoProduto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AvaliacaoProdutoRepositorio extends JpaRepository<AvaliacaoProduto, Long> {

    List<AvaliacaoProduto> findByProdutoId(Long id_produto);

    Page<AvaliacaoProduto> findByProdutoId(Long id_produto, Pageable pageable);

    @Query("SELECT AVG(a.nota) FROM AvaliacaoProduto a WHERE a.produto.id = :idProduto")
    Double mediaPorProduto(@Param("idProduto") Long idProduto);

}



