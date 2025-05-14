package OMCE.OMCE.Produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    @Query("SELECT p FROM Produto p WHERE p.usuario.id = :id_usuario")
    Page<Produto> pegarProdutosUsuario(Long id_usuario, Pageable pageable);
    Page<Produto> findAll(Pageable pageable);
    @Query("SELECT p FROM Produto p WHERE p.categoria = :categoria")
    Page<Produto> pegarTodosProdutosCategoria(Categoria categoria,Pageable pageable);
    @Query("""
    SELECT p FROM Produto p
    WHERE (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
    AND (:categoria IS NULL OR p.categoria = :categoria)
    AND (:precoMin IS NULL OR p.preco >= :precoMin)
    AND (:precoMax IS NULL OR p.preco <= :precoMax)
""")
    Page<Produto> filtrarProdutos(  @Param("nome") String nome,
                                    @Param("categoria") Categoria categoria,
                                    @Param("precoMin") Double precoMin,
                                    @Param("precoMax") Double precoMax,
                                    Pageable pageable);
    @Query("SELECT p FROM Produto p WHERE p.usuario.id = :id_usuario AND p.vendido = true")
    Page<Produto> pegarVendas(@Param("id_usuario")  Long id_usuario, Pageable pageable);
}
