package OMCE.OMCE.User.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosRedefinirSenha(String token, @JsonAlias("senha") String novaSenha) {
}