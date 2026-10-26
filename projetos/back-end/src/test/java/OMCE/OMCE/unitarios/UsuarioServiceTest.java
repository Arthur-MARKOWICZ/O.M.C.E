package OMCE.OMCE.unitarios;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.Execao.AcessoNegado;
import OMCE.OMCE.Execao.SenhaDiferenteDaOriginal;
import OMCE.OMCE.Execao.TokenRedefinicaoInvalido;
import OMCE.OMCE.Execao.UserNaoEncontrado;
import OMCE.OMCE.User.Role;
import OMCE.OMCE.User.Service.UserService;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import OMCE.OMCE.User.dto.DadosRedefinirSenha;
import OMCE.OMCE.User.repository.UserRepository;
import OMCE.OMCE.Validacao.ValidacaoUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @InjectMocks
    private UserService service;
    @Mock
    private UserRepository repository;
    @Mock
    private ValidacaoUser validacao;

    @Test
    public void DeveCadastrarUsuario(){
        DadosEndereco dadosEndereco = new DadosEndereco("8123434","brasil","test",
                "test","Rua test");
        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser("test","12345678912"
                ,"22-06-2005","test",dadosEndereco,"test@gmail","1231313139",
                "testUser","test");
        User usuario = new User(dadosCadastroUser);
        when(repository.save(any(User.class))).thenReturn(usuario);

        var usuarioCadastro = service.cadastro(dadosCadastroUser);
        assertEquals("test", usuarioCadastro.getNome());

    }
    @Test
    public void DeveCadastrarUsuarioSemSenha(){
        DadosEndereco dadosEndereco = new DadosEndereco("8123434","brasil","test",
                "test","Rua test");
        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser("test","12345678912"
                ,"22-06-2005","test",dadosEndereco,"test@gmail","1231313139",
                "testUser","");
        doThrow(new RuntimeException("O campo de senha esta em branco"))
                .when(validacao).validarCadastroUsuario(dadosCadastroUser);

        assertThrows(RuntimeException.class,
                () -> service.cadastro(dadosCadastroUser));
    }
    @Test
    public void DeveCadastrarUsuarioSemEmail(){
        DadosEndereco dadosEndereco = new DadosEndereco("8123434","brasil","test",
                "test","Rua test");
        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser("test","12345678912"
                ,"22-06-2005","test",dadosEndereco,"","1231313139",
                "testUser","test");
        doThrow(new RuntimeException("O campo de e-mail esta em branco"))
                .when(validacao).validarCadastroUsuario(dadosCadastroUser);

        assertThrows(RuntimeException.class,
                () -> service.cadastro(dadosCadastroUser));
    }

    @Test
    public void deveExcluirUsuarioQuandoSenhaCorreta() {
        User usuario = usuarioComSenha("senhaCorreta");
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        service.excluir(1L, "senhaCorreta");

        // o endereco dos pedidos e apagado antes, e o delete do usuario e sempre o ultimo passo
        InOrder ordem = inOrder(repository);
        ordem.verify(repository).anonimizarEnderecoDosPedidosDoUsuario(1L);
        ordem.verify(repository).deleteAvaliacoesDoVendedor(1L);
        ordem.verify(repository).deleteProdutosSemVendaDoUsuario(1L);
        ordem.verify(repository).delete(usuario);
        verify(repository, never()).save(any(User.class));
    }

    @Test
    public void deveRecusarExclusaoQuandoSenhaIncorreta() {
        User usuario = usuarioComSenha("senhaCorreta");
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        assertThrows(SenhaDiferenteDaOriginal.class, () -> service.excluir(1L, "senhaErrada"));
        verify(repository, never()).delete(any(User.class));
        verify(repository, never()).anonimizarEnderecoDosPedidosDoUsuario(any());
        verify(repository, never()).deleteProdutosSemVendaDoUsuario(any());
    }

    @Test
    public void deveRecusarExclusaoQuandoUsuarioNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNaoEncontrado.class, () -> service.excluir(99L, "qualquer"));
    }

    @Test
    public void deveRedefinirSenhaComTokenValidoELimparOToken() {
        User usuario = usuarioComSenha("SenhaAntiga@1");
        usuario.setTokenRedefinicao("token-valido");
        usuario.setTokenExpiracao(LocalDateTime.now().plusMinutes(10));
        when(repository.findByTokenRedefinicao("token-valido")).thenReturn(usuario);

        service.novaSenha(new DadosRedefinirSenha("token-valido", "SenhaNova@123"));

        assertTrue(new BCryptPasswordEncoder().matches("SenhaNova@123", usuario.getSenha()));
        assertNull(usuario.getTokenRedefinicao());
        assertNull(usuario.getTokenExpiracao());
    }

    @Test
    public void deveRecusarRedefinicaoComTokenExpirado() {
        User usuario = usuarioComSenha("SenhaAntiga@1");
        String hashAntigo = usuario.getSenha();
        usuario.setTokenRedefinicao("token-velho");
        usuario.setTokenExpiracao(LocalDateTime.now().minusMinutes(1));
        when(repository.findByTokenRedefinicao("token-velho")).thenReturn(usuario);

        assertThrows(TokenRedefinicaoInvalido.class,
                () -> service.novaSenha(new DadosRedefinirSenha("token-velho", "SenhaNova@123")));
        assertEquals(hashAntigo, usuario.getSenha());
    }

    @Test
    public void deveRecusarRedefinicaoComTokenSemValidadeRegistrada() {
        User usuario = usuarioComSenha("SenhaAntiga@1");
        usuario.setTokenRedefinicao("token-sem-validade");
        when(repository.findByTokenRedefinicao("token-sem-validade")).thenReturn(usuario);

        assertThrows(TokenRedefinicaoInvalido.class,
                () -> service.novaSenha(new DadosRedefinirSenha("token-sem-validade", "SenhaNova@123")));
    }

    @Test
    public void deveRecusarRedefinicaoComTokenInexistente() {
        when(repository.findByTokenRedefinicao("nao-existe")).thenReturn(null);

        assertThrows(TokenRedefinicaoInvalido.class,
                () -> service.novaSenha(new DadosRedefinirSenha("nao-existe", "SenhaNova@123")));
    }

    @Test
    public void deveRecusarRedefinicaoComTokenEmBrancoSemConsultarOBanco() {
        assertThrows(TokenRedefinicaoInvalido.class,
                () -> service.novaSenha(new DadosRedefinirSenha(null, "SenhaNova@123")));
        assertThrows(TokenRedefinicaoInvalido.class,
                () -> service.novaSenha(new DadosRedefinirSenha("  ", "SenhaNova@123")));
        verify(repository, never()).findByTokenRedefinicao(any());
    }

    @Test
    public void naoDevePermitirReutilizarOMesmoToken() {
        User usuario = usuarioComSenha("SenhaAntiga@1");
        usuario.setTokenRedefinicao("uso-unico");
        usuario.setTokenExpiracao(LocalDateTime.now().plusMinutes(10));
        when(repository.findByTokenRedefinicao("uso-unico")).thenReturn(usuario);

        service.novaSenha(new DadosRedefinirSenha("uso-unico", "SenhaNova@123"));

        assertThrows(TokenRedefinicaoInvalido.class,
                () -> service.novaSenha(new DadosRedefinirSenha("uso-unico", "OutraSenha@456")));
        assertTrue(new BCryptPasswordEncoder().matches("SenhaNova@123", usuario.getSenha()));
    }

    @Test
    public void deveEntregarOsDadosDaContaAoProprioDono() {
        User dono = usuarioComSenha("senhaCorreta");
        when(repository.findById(1L)).thenReturn(Optional.of(dono));

        assertEquals(dono, service.pegarUserParaLeitura(1L, dono));
    }

    @Test
    public void naoDeveEntregarOsDadosDeOutraConta() {
        User outro = usuarioComSenha("senhaCorreta");
        outro.setId(2L);

        assertThrows(AcessoNegado.class, () -> service.pegarUserParaLeitura(1L, outro));

        verify(repository, never()).findById(any());
    }

    @Test
    public void deveEntregarOsDadosDeQualquerContaParaAdministrador() {
        User admin = usuarioComSenha("senhaCorreta");
        admin.setId(3L);
        admin.setRole(Role.ADMIN);
        User alvo = usuarioComSenha("outraSenha");
        when(repository.findById(1L)).thenReturn(Optional.of(alvo));

        assertEquals(alvo, service.pegarUserParaLeitura(1L, admin));
    }

    @Test
    public void naoDeveEntregarDadosSemUsuarioLogado() {
        assertThrows(AcessoNegado.class, () -> service.pegarUserParaLeitura(1L, null));
    }

    private User usuarioComSenha(String senha) {
        DadosEndereco dadosEndereco = new DadosEndereco("8123434", "brasil", "test",
                "test", "Rua test");
        DadosCadastroUser dados = new DadosCadastroUser("test", "12345678912",
                "22-06-2005", "test", dadosEndereco, "test@gmail", "1231313139",
                "testUser", senha);
        User usuario = new User(dados);
        usuario.setId(1L);
        usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
        return usuario;
    }
}