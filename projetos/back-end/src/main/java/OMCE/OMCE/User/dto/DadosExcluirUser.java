package OMCE.OMCE.User.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DadosExcluirUser(@JsonProperty("senha") String senha) {
}
