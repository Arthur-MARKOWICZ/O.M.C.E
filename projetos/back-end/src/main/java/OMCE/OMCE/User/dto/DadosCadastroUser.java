package OMCE.OMCE.User.dto;

import OMCE.OMCE.Enderco.DadosEndereco;
import OMCE.OMCE.User.Role;
import jakarta.validation.constraints.NotNull;

public record  DadosCadastroUser (@NotNull String nome,@NotNull  String cpf,@NotNull  String dataNasc,
                                   @NotNull  String sexo, @NotNull   DadosEndereco endereco,
                                   @NotNull  String email,@NotNull  String telefone,@NotNull  String nomeUser,
                                   @NotNull  String senha, Role role) {

    public DadosCadastroUser(String nome, String cpf, String dataNasc, String sexo, DadosEndereco endereco, String email, String telefone, String nomeUser, String senha) {
        this(nome, cpf, dataNasc, sexo, endereco, email, telefone, nomeUser, senha, Role.COMPRADOR);
    }


}
