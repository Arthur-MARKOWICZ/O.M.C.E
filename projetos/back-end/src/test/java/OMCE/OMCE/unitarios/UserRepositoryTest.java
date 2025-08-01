package OMCE.OMCE.unitarios;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import OMCE.OMCE.User.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {
    @Mock
    private UserRepository repository;
    @Test
    @DisplayName("Deve pega o usuario pelo id")
    void PegarUserPeloId(){
        // Arrange
        DadosEndereco dadosEndereco = new DadosEndereco("8123434","brasil","test",
                "test","Rua test");
        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser("test","12345678912"
                ,"22-06-2005","test",dadosEndereco,"test@gmail","1231313139",
                "testUser","test");
        User usuario = new User(dadosCadastroUser);
        usuario.setId(1l);
        when(repository.findById(1l)).thenReturn(Optional.of(usuario));
        // Act
        Optional<User> result = repository.findById(1l);
        // Assert
        assertTrue(result.isPresent());
        assertEquals("testUser",result.get().getNomeUser());
    }
    @Test
    @DisplayName("deve pegar usuario pelo email")
    void PegarUserPeloEmail(){
        // Arrange
        DadosEndereco dadosEndereco = new DadosEndereco("8123434","brasil","test",
                "test","Rua test");
        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser("test","12345678912"
                ,"22-06-2005","test",dadosEndereco,"test@gmail","1231313139",
                "testUser","test");
        User usuario = new User(dadosCadastroUser);
        when(repository.findByEmail("test@gmail")).thenReturn(usuario);
        // Act
        User result = repository.findByEmail("test@gmail");
        // Assert
        assertEquals("test@gmail", result.getEmail());
    }
    @Test
    @DisplayName("deve pefar usuario pelo token de redefinicao")
    void PegarUserPeloTokenDeRedefinicao(){
        // Arrange
        DadosEndereco dadosEndereco = new DadosEndereco("8123434","brasil","test",
                "test","Rua test");
        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser("test","12345678912"
                ,"22-06-2005","test",dadosEndereco,"test@gmail","1231313139",
                "testUser","test");
        User usuario = new User(dadosCadastroUser);
        usuario.setTokenRedefinicao("test");
        when(repository.findByTokenRedefinicao("test")).thenReturn(usuario);
        // Act
        User result = repository.findByTokenRedefinicao("test");
        // Assert
        assertEquals("test", result.getTokenRedefinicao());
    }

}
