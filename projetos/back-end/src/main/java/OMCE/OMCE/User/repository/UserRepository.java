package OMCE.OMCE.User.repository;

import OMCE.OMCE.User.Role;
import OMCE.OMCE.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByTokenRedefinicao(String token);

    long countByRole(Role role);

    @Modifying
    @Query(value = "DELETE FROM pagamento WHERE pedido_id IN (SELECT pedido_id FROM pedido WHERE comprador_id = :id)", nativeQuery = true)
    void deletePagamentosDosPedidosDoUsuario(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM itens_pedido WHERE pedido_id IN (SELECT pedido_id FROM pedido WHERE comprador_id = :id)", nativeQuery = true)
    void deleteItensDosPedidosDoUsuario(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM pedido WHERE comprador_id = :id", nativeQuery = true)
    void deletePedidosDoUsuario(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM avaliacao_vendedor WHERE vendedor_id = :id", nativeQuery = true)
    void deleteAvaliacoesDoVendedor(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM itens_pedido WHERE produto_id IN (SELECT id FROM produto WHERE id_usuario = :id)", nativeQuery = true)
    void deleteItensDosProdutosDoUsuario(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM produto WHERE id_usuario = :id", nativeQuery = true)
    void deleteProdutosDoUsuario(@Param("id") Long id);
}