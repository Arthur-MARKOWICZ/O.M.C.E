package OMCE.OMCE.User;

import OMCE.OMCE.Enderco.DadosEndereco;
import jakarta.validation.constraints.NotNull;

public record  DadosCadastroUser (String subistituir,@NotNull String nome,@NotNull  String cpf,@NotNull  String dataNasc,
                                  @NotNull  String sexo, @NotNull   DadosEndereco endereco,
                                  @NotNull  String email,@NotNull  String telefone,@NotNull  String nomeUser,
                                  @NotNull  String senha) {


}
