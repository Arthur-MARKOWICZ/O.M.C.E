package OMCE.OMCE.Produto;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.User.DadosCadastroUser;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ProdutoRepositoryTest {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UserRepository userRepository;
    @Test
    @Rollback(value = false)
    void deveSalvarProduto() {
        DadosCadastroProduto dados =  new DadosCadastroProduto("test",100.00,"test",
                3,"test",".test");
        User user = userRepository.getReferenceById(dados.id_usuario());
        Produto produto = new Produto(dados);
        produto.setUsuario(user);
        Produto salvo = produtoRepository.save(produto);
        assertNotNull(salvo.getId());
    }
    @Test
    void pegarProdutosUsuario() {

    }
}