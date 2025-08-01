package OMCE.OMCE.Execao.global;

import OMCE.OMCE.Execao.CategoriaInvalida;
import OMCE.OMCE.Execao.ProdutoNaoEncontrado;
import OMCE.OMCE.Execao.SenhaIgualAOriginal;
import OMCE.OMCE.Execao.UserNaoEncontrado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        return  buildResponse(HttpStatus.CONFLICT,ex.getMessage());
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
