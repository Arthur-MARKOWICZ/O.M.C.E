package OMCE.OMCE.unitarios;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.DadosAlterarDadosProduto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.utils.ProdutoTestFactory;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.Produto.service.ProdutoService;
import OMCE.OMCE.Produto.service.template.CadastroArduino;
import OMCE.OMCE.Produto.service.template.CadastroBateria;
import OMCE.OMCE.Produto.service.template.CadastroCabo;
import OMCE.OMCE.Produto.service.template.CadastroConector;
import OMCE.OMCE.Produto.service.template.CadastroESP32;
import OMCE.OMCE.Produto.service.template.CadastroMotor;
import OMCE.OMCE.Produto.service.template.CadastroOutro;
import OMCE.OMCE.Produto.service.template.CadastroResistor;
import OMCE.OMCE.Produto.service.template.CadastroSensor;
import OMCE.OMCE.User.Service.UserService;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import OMCE.OMCE.User.repository.UserRepository;
import OMCE.OMCE.Validacao.ValidacaoProduto;
import OMCE.OMCE.Validacao.ValidacaoUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static OMCE.OMCE.Produto.enums.Categoria.ESP32;
import static OMCE.OMCE.Produto.enums.Condicao.USADO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {
    private ProdutoService service;
    @Mock
    private ProdutoRepository repository;
    @Mock
    private UserService userService;
    @Mock
    private ValidacaoProduto validacao;
    @Mock
    private UserRepository userRepository;
    private User usuarioCadastro;
    @BeforeEach
    void setup(){
        DadosEndereco dadosEndereco = new DadosEndereco("8123434", "brasil", "test", "test", "Rua test");
        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser(
                "test", "12345678912", "22-06-2005", "test",
                dadosEndereco, "test@gmail", "1231313139",
                "testUser", "test"
        );

        usuarioCadastro = new User(dadosCadastroUser);
        usuarioCadastro.setId(1L);

        // os cadastros por categoria sao reais: quem valida e salva continua sendo o Template Method
        service = new ProdutoService(
                repository,
                userService,
                new CadastroESP32(repository, userService, validacao),
                new CadastroArduino(repository, userService, validacao),
                new CadastroResistor(repository, userService, validacao),
                new CadastroSensor(repository, userService, validacao),
                new CadastroBateria(repository, userService, validacao),
                new CadastroCabo(repository, userService, validacao),
                new CadastroMotor(repository, userService, validacao),
                new CadastroConector(repository, userService, validacao),
                new CadastroOutro(repository, userService, validacao)
        );
    }
    @Test
    public void DeveCadastrarProduto(){
        when(userService.pegarUserPorId(1L)).thenReturn(usuarioCadastro);
        DadosCadastroProduto dados = ProdutoTestFactory.dados("test",10,"test",1l,
                "10", "10",ESP32,USADO);
        Produto produto = ProdutoTestFactory.produto(dados);
        when(repository.save(any(Produto.class))).thenReturn(produto);
        Produto produtoCadastro = service.cadastro(dados);
        assertEquals("test", produtoCadastro.getNome());

    }
    @Test
    public void DeveCadastrarProdutoComPrecoNegativo(){
        DadosCadastroProduto dados = ProdutoTestFactory.dados("test",-1,"test",1l,
                "10", "10",ESP32,USADO);
        Produto produto = ProdutoTestFactory.produto(dados);
        doThrow(new RuntimeException("O produto esta com um preco invalido"))
                .when(validacao).ValidarCadastroProduto(dados);
        assertThrows(RuntimeException.class,
                () -> service.cadastro(dados));;
    }
    @Test
    public  void DeveAlterarONomeDoProduto(){
        when(userService.pegarUserPorId(1L)).thenReturn(usuarioCadastro);
        DadosCadastroProduto dados = ProdutoTestFactory.dados("test",10,"test",1l,
                "10", "10",ESP32,USADO);
        Produto produto = ProdutoTestFactory.produto(dados);
        produto.setId(1L);
        AtomicReference<Produto> produtoSalvo = new AtomicReference<>();

        when(repository.save(any(Produto.class))).thenAnswer(invocation -> {
            Produto p = invocation.getArgument(0);
            p.setId(1L);
            produtoSalvo.set(p);
            return p;
        });

        when(repository.findById(1L))
                .thenAnswer(invocation -> Optional.ofNullable(produtoSalvo.get()));

        Produto produtoCadastro = service.cadastro(dados);

        DadosAlterarDadosProduto dadosAlterar = new DadosAlterarDadosProduto(
                produtoCadastro.getId(), "testalterado", 10.00, "test", "10", "10"
        );

        service.alterarDadosProduto(dadosAlterar);

        assertEquals("testalterado", produtoCadastro.getNome());

    }
    @Test
    void DeveDeletarProduto(){
        when(userService.pegarUserPorId(1L)).thenReturn(usuarioCadastro);
        DadosCadastroProduto dados = ProdutoTestFactory.dados("test", 10, "test", 1L,
                "10", "10", ESP32, USADO);
        Produto produto = ProdutoTestFactory.produto(dados);
        produto.setId(1L);
        when(repository.save(any(Produto.class))).thenReturn(produto);
        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        Produto produtoCadastro = service.cadastro(dados);
        service.deletar(produto.getId());
        verify(repository).deleteById(1L);
    }

}
