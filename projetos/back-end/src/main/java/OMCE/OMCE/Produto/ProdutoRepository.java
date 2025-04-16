package OMCE.OMCE.Produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    @Query("""
            SELECT p FROM Produto p WHERE p.id_usuario = :id_usuario
            """)
    List<Produto> pegarProdutosUsuario(Long id_usuario);
}
