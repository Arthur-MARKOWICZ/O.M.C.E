package OMCE.OMCE.unitarios;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.Execao.AcessoNegado;
import OMCE.OMCE.Execao.ProdutoJaVendido;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.dto.DadosAlterarDadosProduto;
import OMCE.OMCE.Produto.dto.DadosCadastroProduto;
import OMCE.OMCE.Produto.dto.ProdutoRespostaDTO;
import OMCE.OMCE.Produto.enums.Categoria;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static OMCE.OMCE.Produto.enums.Categoria.ESP32;
import static OMCE.OMCE.Produto.enums.Condicao.USADO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

        service.alterarDadosProduto(dadosAlterar, 1L);

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
        produto.setUsuario(usuarioCadastro);
        Produto produtoCadastro = service.cadastro(dados);
        service.deletar(produto.getId(), 1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void DeveFiltrarOsProdutosDoUsuario(){
        Pageable pageable = Pageable.unpaged();
        when(repository.filtrarProdutosDoUsuario(1L, "sensor", Categoria.SENSORES, 5.0, 50.0, pageable))
                .thenReturn(Page.empty());

        Page<ProdutoRespostaDTO> resultado = service.pegarProdutosPorUser(1L, "sensor", "sensores", 5.0, 50.0, pageable);

        assertTrue(resultado.isEmpty());
        verify(repository).filtrarProdutosDoUsuario(1L, "sensor", Categoria.SENSORES, 5.0, 50.0, pageable);
    }

    @Test
    void NaoDeveExcluirProdutoDeOutroVendedor(){
        Produto produto = ProdutoTestFactory.produto(ProdutoTestFactory.dados("test", 10, "test", 1L,
                "10", "10", ESP32, USADO));
        produto.setId(11L);
        produto.setUsuario(usuarioCadastro);
        when(repository.findById(11L)).thenReturn(Optional.of(produto));

        assertThrows(AcessoNegado.class, () -> service.deletar(11L, 2L));

        verify(repository, never()).deleteById(any());
    }

    @Test
    void NaoDeveEditarProdutoDeOutroVendedor(){
        Produto produto = ProdutoTestFactory.produto(ProdutoTestFactory.dados("test", 10, "test", 1L,
                "10", "10", ESP32, USADO));
        produto.setId(12L);
        produto.setNome("original");
        produto.setUsuario(usuarioCadastro);
        when(repository.findById(12L)).thenReturn(Optional.of(produto));
        DadosAlterarDadosProduto dadosAlterar = new DadosAlterarDadosProduto(
                12L, "invadido", 1.00, "test", "10", "10");

        assertThrows(AcessoNegado.class, () -> service.alterarDadosProduto(dadosAlterar, 2L));

        assertEquals("original", produto.getNome());
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    void NaoDeveMexerEmProdutoSemVendedor(){
        Produto produto = ProdutoTestFactory.produto(ProdutoTestFactory.dados("test", 10, "test", 1L,
                "10", "10", ESP32, USADO));
        produto.setId(13L);
        produto.setUsuario(null);
        when(repository.findById(13L)).thenReturn(Optional.of(produto));
        DadosAlterarDadosProduto dadosAlterar = new DadosAlterarDadosProduto(
                13L, "qualquer", 1.00, "test", "10", "10");

        assertThrows(AcessoNegado.class, () -> service.deletar(13L, 1L));
        assertThrows(AcessoNegado.class, () -> service.alterarDadosProduto(dadosAlterar, 1L));
    }

    @Test
    void NaoDeveAnunciarProdutoEmNomeDeOutroUsuario(){
        DadosCadastroProduto dados = ProdutoTestFactory.dados("test", 10, "test", 1L,
                "10", "10", ESP32, USADO);

        assertThrows(AcessoNegado.class, () -> service.cadastro(dados, 2L));

        verify(userService, never()).pegarUserPorId(any());
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    void NaoDeveDeletarProdutoJaVendido(){
        Produto produto = ProdutoTestFactory.produto(ProdutoTestFactory.dados("test", 10, "test", 1L,
                "10", "10", ESP32, USADO));
        produto.setId(8L);
        produto.setUsuario(usuarioCadastro);
        produto.setVendido(true);
        when(repository.findById(8L)).thenReturn(Optional.of(produto));

        ProdutoJaVendido erro = assertThrows(ProdutoJaVendido.class, () -> service.deletar(8L, 1L));

        assertTrue(erro.getMessage().contains("não pode ser excluído"));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void NaoDeveAlterarDadosDeProdutoJaVendido(){
        Produto produto = ProdutoTestFactory.produto(ProdutoTestFactory.dados("test", 10, "test", 1L,
                "10", "10", ESP32, USADO));
        produto.setId(9L);
        produto.setUsuario(usuarioCadastro);
        produto.setPreco(10.0);
        produto.setVendido(true);
        when(repository.findById(9L)).thenReturn(Optional.of(produto));
        DadosAlterarDadosProduto dadosAlterar = new DadosAlterarDadosProduto(
                9L, "novo nome", 999.00, "test", "10", "10");

        ProdutoJaVendido erro = assertThrows(ProdutoJaVendido.class, () -> service.alterarDadosProduto(dadosAlterar, 1L));

        assertTrue(erro.getMessage().contains("não pode mais ser editado"));
        assertEquals(Double.valueOf(10.0), produto.getPreco());
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    void DevePegarDetalhesDoProdutoComOVendedor(){
        DadosCadastroProduto dados = ProdutoTestFactory.dados("test", 10, "test", 1L,
                "10", "10", ESP32, USADO);
        Produto produto = ProdutoTestFactory.produto(dados);
        produto.setId(5L);
        produto.setUsuario(usuarioCadastro);
        when(repository.findById(5L)).thenReturn(Optional.of(produto));
        when(userService.pegarUserPorId(1L)).thenReturn(usuarioCadastro);

        Map<String, Object> json = service.pegarDetalhesDoProduto(5L);

        assertEquals(usuarioCadastro.getNome(), json.get("nome_do_usuario"));
        assertEquals(1L, json.get("id_vendedor"));
    }

    @Test
    void DevePegarDetalhesDoProdutoVendidoPorUsuarioQueExcluiuAConta(){
        DadosCadastroProduto dados = ProdutoTestFactory.dados("test", 10, "test", 1L,
                "10", "10", ESP32, USADO);
        Produto produto = ProdutoTestFactory.produto(dados);
        produto.setId(6L);
        produto.setUsuario(null);
        when(repository.findById(6L)).thenReturn(Optional.of(produto));

        Map<String, Object> json = service.pegarDetalhesDoProduto(6L);

        assertEquals("Usuário removido", json.get("nome_do_usuario"));
        assertNull(json.get("id_vendedor"));
        verify(userService, never()).pegarUserPorId(any());
    }

}