package OMCE.OMCE.unitarios;

import OMCE.OMCE.Execao.ProdutoJaVendido;
import OMCE.OMCE.Execao.global.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    private String mensagemDe(ResponseEntity<Object> resposta) {
        return (String) ((Map<?, ?>) resposta.getBody()).get("messagem");
    }

    private ResponseEntity<Object> erroDoBanco(String detalheDoBanco) {
        return handler.handleDataIntegrity(
                new DataIntegrityViolationException("erro", new SQLException(detalheDoBanco)));
    }

    @Test
    void deveTraduzirExclusaoDeRegistroQueEstaEmUso() {
        ResponseEntity<Object> resposta = erroDoBanco(
                "Cannot delete or update a parent row: a foreign key constraint fails (`omce`.`itens_pedido`, "
                        + "CONSTRAINT `itens_pedido_ibfk_2` FOREIGN KEY (`produto_id`) REFERENCES `produto` (`id`))");

        String mensagem = mensagemDe(resposta);
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertTrue(mensagem.contains("vinculado"));
        assertFalse(mensagem.toLowerCase().contains("constraint"));
        assertFalse(mensagem.contains("itens_pedido"));
    }

    @Test
    void deveTraduzirEmailDuplicado() {
        assertEquals("Este e-mail já está cadastrado.",
                mensagemDe(erroDoBanco("Duplicate entry 'a@b.com' for key 'user.email'")));
    }

    @Test
    void deveTraduzirNomeDeUsuarioDuplicadoMesmoQuandoOValorTemAPalavraEmail() {
        assertEquals("Este nome de usuário já está em uso.",
                mensagemDe(erroDoBanco("Duplicate entry 'emailguy' for key 'user.nome_user'")));
    }

    @Test
    void deveUsarMensagemGenericaSemVazarDetalhesDoBanco() {
        String mensagem = mensagemDe(erroDoBanco("Data truncation: Data too long for column 'cep' at row 1"));

        assertTrue(mensagem.contains("conflito"));
        assertFalse(mensagem.contains("Data truncation"));
    }

    @Test
    void deveResponderConflitoQuandoOProdutoJaFoiVendido() {
        ResponseEntity<Object> resposta = handler.handleProdutoJaVendido(ProdutoJaVendido.aoEditar());

        assertEquals(HttpStatus.CONFLICT, resposta.getStatusCode());
        assertTrue(mensagemDe(resposta).contains("já foi vendido"));
    }
}