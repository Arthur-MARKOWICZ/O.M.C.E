package OMCE.OMCE.Execao;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErro> handleNotFound(EntityNotFoundException ex, HttpServletRequest req) {
        ApiErro error = new ApiErro();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError("Recurso não encontrado");
        error.setMessage(ex.getMessage());
        error.setPath(req.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<  Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String mensagem;

        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException &&
                ex.getMessage().contains("user.email")) {
            mensagem = "E-mail já cadastrado. Escolha outro.";
        }
        if(ex.getCause() instanceof  org.hibernate.exception.ConstraintViolationException &&
                ex.getMessage().contains("user.nome_user")){
            mensagem = "Nome de usuario ja cadastrado";
        }
        else {
            mensagem = "Erro de integridade nos dados enviados.";
        }

        Map<String, String> erro = new HashMap<>();
        erro.put("mensagem", mensagem);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erro);
    }

}
