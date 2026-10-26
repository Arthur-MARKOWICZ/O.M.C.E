package OMCE.OMCE.unitarios;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserJsonTest {

    @Test
    void naoDeveExporSenhaNemTokenDeRedefinicaoNoJson() throws Exception {
        DadosEndereco endereco = new DadosEndereco("8123434", "brasil", "test", "test", "Rua test");
        User usuario = new User(new DadosCadastroUser(
                "test", "12345678912", "22-06-2005", "test", endereco, "test@gmail", "1231313139",
                "testUser", "senhaSecreta"));
        usuario.setSenha("$2a$10$hashDeTesteQueNaoPodeVazar");
        usuario.setTokenRedefinicao("token-secreto-123");
        usuario.setTokenExpiracao(LocalDateTime.now().plusMinutes(30));

        String json = new ObjectMapper().writeValueAsString(usuario);

        assertFalse(json.contains("hashDeTesteQueNaoPodeVazar"));
        assertFalse(json.contains("token-secreto-123"));
        assertFalse(json.contains("\"senha\""));
        assertFalse(json.contains("\"password\""));
        assertFalse(json.contains("tokenRedefinicao"));
        assertFalse(json.contains("tokenExpiracao"));
        assertTrue(json.contains("\"nome\":\"test\""));
        assertTrue(json.contains("\"email\""));
    }
}