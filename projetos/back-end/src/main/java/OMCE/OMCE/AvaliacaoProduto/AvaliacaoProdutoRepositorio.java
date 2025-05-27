package OMCE.OMCE.AvaliacaoProduto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AvaliacaoProdutoRepositorio extends JpaRepository<AvaliacaoProduto, Long> {
    List<AvaliacaoProduto> findByProdutoId(Long produtoId);
}

