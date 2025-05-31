package OMCE.OMCE.User;

import OMCE.OMCE.Enderco.DadosEndereco;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DadosAlterarDadosUser(@JsonProperty("id") Long id,
                                         @JsonProperty("nome") String nome,
                                         @JsonProperty("cpf") String cpf,
                                         @JsonProperty("dataNasc") String dataNasc,
                                         @JsonProperty("endereco") DadosEndereco endereco,
                                         @JsonProperty("email") String email,
                                         @JsonProperty("telefone") String telefone,
                                         @JsonProperty("nomeUser") String nomeUser,
                                         @JsonProperty("senha") String senha,
                                         @JsonProperty("novaSenha") String novaSenha   ){
}
