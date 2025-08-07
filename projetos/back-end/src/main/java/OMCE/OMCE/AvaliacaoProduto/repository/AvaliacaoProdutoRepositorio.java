package OMCE.OMCE.AvaliacaoProduto.repository;

import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

public interface AvaliacaoProdutoRepositorio extends JpaRepository<AvaliacaoProduto, Long> {


    Page<AvaliacaoProduto> findByProdutoId(Long id_produto, Pageable pageable);

    @Query("SELECT AVG(a.nota) FROM AvaliacaoProduto a WHERE a.produto.id = :idProduto")
    Double mediaPorProduto(@Param("idProduto") Long idProduto);

}



