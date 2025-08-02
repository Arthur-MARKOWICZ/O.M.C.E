package OMCE.OMCE.integracao;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import OMCE.OMCE.User.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRespositoryIntegrationTest {
    @Autowired
    private UserRepository repository;
    @BeforeEach
    void setup() {
        repository.deleteAll();
    }
    @Test
    void deveSalvarUserNoBancoDeDadosEBuscarPeloNome(){
        DadosEndereco dadosEndereco = new DadosEndereco("8123434","brasil","test",
                "test","Rua test");
        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser("test","12345678912"
                ,"22-06-2005","test",dadosEndereco,"test@gmail","1231313139",
                "testUser","test");
        User user = new User(dadosCadastroUser);
        user.setSenha(dadosCadastroUser.senha());
        repository.save(user);
        User result = repository.findByEmail(user.getEmail());

        assertEquals("testUser", result.getNomeUser());
    }

    @Test
    void deveSalvarNoBancoDeDadosEBusaPeloTokenDeRedefinicao(){
        DadosEndereco dadosEndereco = new DadosEndereco("8123434","brasil","test",
                "test","Rua test");
        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser("test","12345678912"
                ,"22-06-2005","test",dadosEndereco,"test@gmail","1231313139",
                "testUser","test");
        User user = new User(dadosCadastroUser);
        user.setSenha(dadosCadastroUser.senha());
        user.setTokenRedefinicao("test");
        repository.save(user);
        User result = repository.findByTokenRedefinicao("test");
        assertEquals("testUser", result.getNomeUser());
    }
}
