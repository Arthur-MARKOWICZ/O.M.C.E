package OMCE.OMCE.Produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    @Query("SELECT p FROM Produto p WHERE p.usuario.id = :id_usuario")
    Page<Produto> pegarProdutosUsuario(Long id_usuario, Pageable pageable);
    Page<Produto> findAll(Pageable pageable);
}
