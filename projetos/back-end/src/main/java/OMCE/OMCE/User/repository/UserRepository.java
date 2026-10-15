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
    @Query(value = "DELETE FROM avaliacao_vendedor WHERE vendedor_id = :id", nativeQuery = true)
    void deleteAvaliacoesDoVendedor(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE pedido SET logradouro = 'Removido', pais = 'Removido', estado = 'Removido', cidade = 'Removido', cep = 'Removido' WHERE comprador_id = :id", nativeQuery = true)
    void anonimizarEnderecoDosPedidosDoUsuario(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM produto WHERE id_usuario = :id AND NOT EXISTS (SELECT 1 FROM itens_pedido WHERE itens_pedido.produto_id = produto.id)", nativeQuery = true)
    void deleteProdutosSemVendaDoUsuario(@Param("id") Long id);
}