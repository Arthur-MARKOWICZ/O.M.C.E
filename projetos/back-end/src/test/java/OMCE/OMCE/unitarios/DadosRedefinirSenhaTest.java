package OMCE.OMCE.unitarios;

import OMCE.OMCE.User.dto.DadosRedefinirSenha;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DadosRedefinirSenhaTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void deveAceitarSenhaEnviadaComoSenha() throws Exception {
        DadosRedefinirSenha dados = mapper.readValue(
                "{\"token\":\"abc\",\"senha\":\"Nova@12345\"}", DadosRedefinirSenha.class);

        assertEquals("abc", dados.token());
        assertEquals("Nova@12345", dados.novaSenha());
    }

    @Test
    public void deveAceitarSenhaEnviadaComoNovaSenha() throws Exception {
        DadosRedefinirSenha dados = mapper.readValue(
                "{\"token\":\"abc\",\"novaSenha\":\"Nova@12345\"}", DadosRedefinirSenha.class);

        assertEquals("Nova@12345", dados.novaSenha());
    }
}