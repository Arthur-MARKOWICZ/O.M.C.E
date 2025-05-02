package OMCE.OMCE.User;

import OMCE.OMCE.Enderco.DadosEndereco;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserREpositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    @Rollback(false)
    void criarUSer(){
        DadosEndereco dadosEndereco = new DadosEndereco("080431413","test","test","test","test");
        DadosCadastroUser dadosUser = new DadosCadastroUser("test","11111111111","2005-07-25","masculino",
                dadosEndereco,"test@test.com","999999999","test","test");
        User usuario = new User(dadosUser);
        usuario.setSenha(dadosUser.senha());
        userRepository.save(usuario);
    }
}
