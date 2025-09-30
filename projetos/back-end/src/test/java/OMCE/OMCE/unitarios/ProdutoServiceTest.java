package OMCE.OMCE.unitarios;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.DadosAlterarDadosProduto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.Produto.service.ProdutoService;
import OMCE.OMCE.User.Service.UserService;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import OMCE.OMCE.User.repository.UserRepository;
import OMCE.OMCE.Validacao.ValidacaoProduto;
import OMCE.OMCE.Validacao.ValidacaoUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static OMCE.OMCE.Produto.enums.Categoria.ESP32;
import static OMCE.OMCE.Produto.enums.Condicao.USADO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {
    @InjectMocks
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

    }
    @Test
    public void DeveCadastrarProduto(){
        when(userService.pegarUserPorId(1L)).thenReturn(usuarioCadastro);
        DadosCadastroProduto dados = new DadosCadastroProduto("test",10,"test",1l,
                "10", "10",ESP32,USADO);
        Produto produto = new Produto(dados);
        when(repository.save(any(Produto.class))).thenReturn(produto);
        Produto produtoCadastro = service.cadastro(dados);
        assertEquals("test", produtoCadastro.getNome());

    }
    @Test
    public void DeveCadastrarProdutoComPrecoNegativo(){
        DadosCadastroProduto dados = new DadosCadastroProduto("test",-1,"test",1l,
                "10", "10",ESP32,USADO);
        Produto produto = new Produto(dados);
        doThrow(new RuntimeException("O produto esta com um preco invalido"))
                .when(validacao).ValidarCadastroProduto(dados);
        assertThrows(RuntimeException.class,
                () -> service.cadastro(dados));;
    }
    @Test
    public  void DeveAlterarONomeDoProduto(){
        when(userService.pegarUserPorId(1L)).thenReturn(usuarioCadastro);
        DadosCadastroProduto dados = new DadosCadastroProduto("test",10,"test",1l,
                "10", "10",ESP32,USADO);
        Produto produto = new Produto(dados);
        produto.setId(1L);
        when(repository.save(any(Produto.class))).thenAnswer(invocation -> {
            Produto p = invocation.getArgument(0);
            p.setId(1L);

            when(repository.getReferenceById(1L)).thenReturn(p);
            return p;
        });

        Produto produtoCadastro = service.cadastro(dados);

        DadosAlterarDadosProduto dadosAlterar = new DadosAlterarDadosProduto(
                produtoCadastro.getId(), "testalterado", 10.00, "test", "10", "10"
        );

        service.alterarDadosProduto(dadosAlterar);

        assertEquals("testalterado", produtoCadastro.getNome());

    }

}
