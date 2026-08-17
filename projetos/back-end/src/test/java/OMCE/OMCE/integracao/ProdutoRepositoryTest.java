package OMCE.OMCE.integracao;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.Produto.repository.ProdutoRepository;

import OMCE.OMCE.User.User;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import OMCE.OMCE.User.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static OMCE.OMCE.Produto.enums.Categoria.ESP32;
import static OMCE.OMCE.Produto.enums.Condicao.USADO;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProdutoRepositoryTest {
    @Autowired
    private ProdutoRepository repository;
    @Autowired
    private UserRepository userRepository;
    private User user;
    @BeforeEach
    void setup() {
        repository.deleteAll();
        userRepository.deleteAll();
        DadosEndereco dadosEndereco = new DadosEndereco("8123434","brasil","test",
                "test","Rua test");
        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser("test","12345678912"
                ,"22-06-2005","test",dadosEndereco,"test@gmail","1231313139",
                "testUser","test");
        User usuario = new User(dadosCadastroUser);
        user = userRepository.save(usuario);

    }

    @Test
    void    deveSalvarProdutoNoBanco(){
        DadosCadastroProduto dados = new DadosCadastroProduto("test",10,"test", user.getId(),
                "10", "10",ESP32,USADO);
        Produto produto = new Produto(dados);
        produto.setUsuario(user);
       var produtoBanco = repository.save(produto);
        var produtoRetornadoBanco = repository.findById(produtoBanco.getId())
                .orElseThrow();
        assertEquals("test", produtoRetornadoBanco.getNome());
    }
    @Test
    void deveSalvarProdutoEObterPorIdDoUSer(){
        DadosCadastroProduto dados = new DadosCadastroProduto("test",10,"test", user.getId(),
                "10", "10",ESP32,USADO);
        Produto produto = new Produto(dados);
        produto.setUsuario(user);
        var produtoBanco = repository.save(produto);
        var produtosRetornadoBanco = repository.pegarProdutosUsuario(user.getId(), Pageable.unpaged());
        assertEquals("test", produtosRetornadoBanco.get().toList().getFirst().getNome());
    }
}
