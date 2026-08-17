package OMCE.OMCE.integracao;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.User.Service.UserService;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.dto.AuthenticationDTO;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import OMCE.OMCE.User.dto.LoginResponseDTO;
import OMCE.OMCE.User.repository.UserRepository;
import OMCE.OMCE.service.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserServiceIntegrationTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UserService service;
    @BeforeEach
    void setup() {
        produtoRepository.deleteAll();
        userRepository.deleteAll();
        DadosEndereco dadosEndereco = new DadosEndereco("8123434", "brasil", "test",
                "test", "Rua test");
        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser("test", "12345678912"
                , "22-06-2005", "test", dadosEndereco, "test@test.com", "1231313139",
                "testToken", "test");
        User salvo = service.cadastro(dadosCadastroUser);
    }
    @Test
    void DeveFazerLogin(){
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("test@test.com","test");
        LoginResponseDTO dto = authorizationService.login(authenticationDTO);
        assertEquals("testToken",dto.nome());
    }
}
