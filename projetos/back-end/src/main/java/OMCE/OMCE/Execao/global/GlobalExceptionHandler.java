package OMCE.OMCE.Execao.global;

import OMCE.OMCE.Execao.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
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
    @ExceptionHandler(SenhaDiferenteDaOriginal.class)
    public  ResponseEntity<Object> handleSenhaDiferenteDaOriginal(SenhaDiferenteDaOriginal ex){
        return  buildResponse(HttpStatus.CONFLICT,ex.getMessage());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "Recurso não encontrado: " + ex.getMessage());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex){
        String msg = ex.getMostSpecificCause() != null
                ? "Erro de integridade de dados: " + ex.getMostSpecificCause().getMessage()
                : "Erro de integridade de dados";
        return buildResponse(HttpStatus.BAD_REQUEST, msg);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex){
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(e ->
                sb.append(e.getField()).append(": ").append(e.getDefaultMessage()).append("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, "Campos inválidos: " + sb);
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
