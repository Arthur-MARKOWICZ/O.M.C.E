package OMCE.OMCE.Execao.global;

import OMCE.OMCE.Execao.*;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final Pattern CHAVE_DUPLICADA = Pattern.compile("for key '([^']+)'");

    @ExceptionHandler(UserNaoEncontrado.class)
    public  ResponseEntity<Object> handleUserNaoEncontrado(UserNaoEncontrado ex){
        return  buildResponse(HttpStatus.NOT_FOUND,ex.getMessage());
    }
    @ExceptionHandler(SenhaIgualAOriginal.class)
    public  ResponseEntity<Object> handleSenhaIgualAOriginal(SenhaIgualAOriginal ex){
        return  buildResponse(HttpStatus.CONFLICT,ex.getMessage());
    }
    @ExceptionHandler(CategoriaInvalida.class)
    public  ResponseEntity<Object> handleCategoriaInvalida(CategoriaInvalida ex){
        return  buildResponse(HttpStatus.CONFLICT,ex.getMessage());
    }
    @ExceptionHandler(ProdutoNaoEncontrado.class)
    public  ResponseEntity<Object> handleProdutoNaoEncontrado(ProdutoNaoEncontrado ex){
        return  buildResponse(HttpStatus.NOT_FOUND,ex.getMessage());
    }
    @ExceptionHandler(PagamentoNaoEncontrado.class)
    public  ResponseEntity<Object> handlePagamentoNaoEncontrado(PagamentoNaoEncontrado ex){
        return  buildResponse(HttpStatus.NOT_FOUND,ex.getMessage());
    }
    @ExceptionHandler(ProdutoJaVendido.class)
    public  ResponseEntity<Object> handleProdutoJaVendido(ProdutoJaVendido ex){
        return  buildResponse(HttpStatus.CONFLICT,ex.getMessage());
    }
    @ExceptionHandler(TokenRedefinicaoInvalido.class)
    public  ResponseEntity<Object> handleTokenRedefinicaoInvalido(TokenRedefinicaoInvalido ex){
        return  buildResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
    }
    @ExceptionHandler(AcessoNegado.class)
    public  ResponseEntity<Object> handleAcessoNegado(AcessoNegado ex){
        return  buildResponse(HttpStatus.FORBIDDEN,ex.getMessage());
    }
    @ExceptionHandler(SenhaDiferenteDaOriginal.class)
    public  ResponseEntity<Object> handleSenhaDiferenteDaOriginal(SenhaDiferenteDaOriginal ex){
        return  buildResponse(HttpStatus.CONFLICT,ex.getMessage());
    }
    @ExceptionHandler(PeriodoInvalido.class)
    public  ResponseEntity<Object> handlePeriodoInvalido(PeriodoInvalido ex){
        return  buildResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Recurso não encontrado: " + ex.getMessage());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex){
        String detalhe = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : "";
        log.warn("Violacao de integridade de dados: {}", detalhe);
        return buildResponse(HttpStatus.BAD_REQUEST, mensagemAmigavel(detalhe));
    }

    private String mensagemAmigavel(String detalheDoBanco) {
        String detalhe = detalheDoBanco == null ? "" : detalheDoBanco.toLowerCase();
        if (detalhe.contains("duplicate entry")) {
            Matcher chave = CHAVE_DUPLICADA.matcher(detalhe);
            String nomeDaChave = chave.find() ? chave.group(1) : "";
            if (nomeDaChave.endsWith("email")) return "Este e-mail já está cadastrado.";
            if (nomeDaChave.endsWith("nome_user")) return "Este nome de usuário já está em uso.";
            return "Já existe um registro com esses dados.";
        }
        if (detalhe.contains("cannot delete or update a parent row")) {
            return "Não foi possível excluir: este registro está vinculado a outras informações do sistema, como um histórico de compras.";
        }
        if (detalhe.contains("cannot add or update a child row")) {
            return "Não foi possível salvar: um dos dados informados não existe mais.";
        }
        return "Não foi possível concluir a operação por conflito com dados já existentes.";
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex){
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(e ->
                sb.append(e.getField()).append(": ").append(e.getDefaultMessage()).append("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, "Campos inválidos: " + sb);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleParametroAusente(MissingServletRequestParameterException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, "Parâmetro obrigatório ausente: " + ex.getParameterName());
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTipoInvalido(MethodArgumentTypeMismatchException ex){
        Class<?> tipo = ex.getRequiredType();
        String aceitos = tipo != null && tipo.isEnum()
                ? " Valores aceitos: " + String.join(", ", java.util.Arrays.stream(tipo.getEnumConstants()).map(String::valueOf).toList()) + "."
                : "";
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Valor inválido para o parâmetro '" + ex.getName() + "': " + ex.getValue() + "." + aceitos);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleNotReadable(HttpMessageNotReadableException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou JSON malformado");
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResource(NoResourceFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Endpoint não encontrado: " + ex.getResourcePath());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntime(RuntimeException ex){
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno no servidor: " + (ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName()));
    }


    private ResponseEntity<Object> buildResponse(HttpStatus status, String messagem){
        Map<String,Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status",status.value());
        body.put("error", status.getReasonPhrase());
        body.put("messagem", messagem);
        return new ResponseEntity<>(body,status);
    }

}