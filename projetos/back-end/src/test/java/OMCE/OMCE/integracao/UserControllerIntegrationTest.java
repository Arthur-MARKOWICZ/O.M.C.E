package OMCE.OMCE.integracao;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.User.Service.UserService;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.dto.AuthenticationDTO;
import OMCE.OMCE.User.dto.DadosAlterarDadosUser;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import OMCE.OMCE.User.dto.LoginResponseDTO;
import OMCE.OMCE.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private String jwtToken;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService service;
    @BeforeEach
    void setupAuthentication() {
        userRepository.deleteAll();
        DadosEndereco dadosEndereco = new DadosEndereco("8123434","brasil","test",
                "test","Rua test");
        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser("test","12345678912"
                ,"22-06-2005","test",dadosEndereco,"test@test.com","1231313139",
                "testToken","test");
        User salvo = service.cadastro(dadosCadastroUser);
        System.out.println(salvo.getEmail());
        // 1. DTO de Requisição de Login (adapte à sua classe real)
        AuthenticationDTO loginRequest = new AuthenticationDTO("test@test.com", "test");

        // 2. Faz a requisição de POST para o endpoint de login
        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity("/auth/login", loginRequest, LoginResponseDTO.class);

        // 3. Verifica se o login foi bem-sucedido e extrai o token
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode(), "Login falhou! Verifique credenciais ou endpoint.");
        LoginResponseDTO authResponse = loginResponse.getBody();
        if (authResponse != null && authResponse.token() != null) {
            this.jwtToken = authResponse.token();
            System.out.println("Token JWT obtido: " + jwtToken); // Para debug
        } else {
            throw new RuntimeException("Token JWT não encontrado na resposta de login.");
        }
    }
    @Test
    void deveCadastrarUser(){
        DadosEndereco dadosEndereco = new DadosEndereco("8123434","brasil","test",
                "test","Rua test");
        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser("test","12345678912"
                ,"22-06-2005","test",dadosEndereco,"cadastro@gmail","1231313139",
                "USERTEST","test");
        User user = new User(dadosCadastroUser);
        user.setSenha("test");
        ResponseEntity<Void> response = restTemplate.postForEntity("/user/cadastro",user, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void deveAlterarDadosDoUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        DadosEndereco dadosEndereco = new DadosEndereco("8123434", "brasil", "test",
                "test", "Rua test");
        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser("test", "12345678912"
                , "22-06-2005", "test", dadosEndereco, "cadastro@gmail", "1231313139",
                "USERTEST", "test");
        User user = service.cadastro(dadosCadastroUser);

        DadosAlterarDadosUser dadosUser = new DadosAlterarDadosUser(user.getId(), "test2", "12345678912",
                "22-06-2005", dadosEndereco, "cadastro@gmail", "1231313139",
                "USERTEST", "test", "test2");
        HttpEntity<DadosAlterarDadosUser> requestEntity = new HttpEntity<>(dadosUser, headers);
        ResponseEntity<Void> response = restTemplate.exchange("/user/alterardados", HttpMethod.PUT,
                requestEntity,
                Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());


    }
    @Test
    void deveExcluirUser(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        DadosEndereco dadosEndereco = new DadosEndereco("8123434", "brasil", "test",
                "test", "Rua test");
        DadosCadastroUser dadosCadastroUser = new DadosCadastroUser("test", "12345678912"
                , "22-06-2005", "test", dadosEndereco, "cadastro@gmail", "1231313139",
                "USERTEST", "test");
        User user = service.cadastro(dadosCadastroUser);
        Long userIdToDelete = user.getId();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                "/user/deletar/{id}",
                HttpMethod.DELETE,
                requestEntity,
                Void.class,
                userIdToDelete
        );
        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
    }


}
