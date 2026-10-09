package OMCE.OMCE.unitarios;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.Execao.SenhaDiferenteDaOriginal;
import OMCE.OMCE.Execao.UserNaoEncontrado;
import OMCE.OMCE.User.Service.UserService;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import OMCE.OMCE.User.repository.UserRepository;
import OMCE.OMCE.Validacao.ValidacaoUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        verify(repository).deletePagamentosDosPedidosDoUsuario(1L);
        verify(repository).deleteItensDosPedidosDoUsuario(1L);
        verify(repository).deletePedidosDoUsuario(1L);
        verify(repository).deleteAvaliacoesDoVendedor(1L);
        verify(repository).deleteItensDosProdutosDoUsuario(1L);
        verify(repository).deleteProdutosDoUsuario(1L);
        verify(repository).delete(usuario);
        verify(repository, never()).save(any(User.class));
    }

    @Test
    public void deveRecusarExclusaoQuandoSenhaIncorreta() {
        User usuario = usuarioComSenha("senhaCorreta");
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        assertThrows(SenhaDiferenteDaOriginal.class, () -> service.excluir(1L, "senhaErrada"));
        verify(repository, never()).delete(any(User.class));
        verify(repository, never()).deleteProdutosDoUsuario(any());
    }

    @Test
    public void deveRecusarExclusaoQuandoUsuarioNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNaoEncontrado.class, () -> service.excluir(99L, "qualquer"));
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
