package OMCE.OMCE.AvaliacaoProduto.repository;

import OMCE.OMCE.AvaliacaoProduto.AvaliacaoProduto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvaliacaoProdutoRepositorio
        extends JpaRepository<AvaliacaoProduto, Long> {

    Page<AvaliacaoProduto> findByProdutoId(
            Long idProduto,
            Pageable pageable);

    @Query("""
        SELECT a.nota
        FROM AvaliacaoProduto a
        WHERE a.produto.id = :idProduto
    """)
    List<Integer> buscarTodasNotas(
            @Param("idProduto") Long idProduto);
}