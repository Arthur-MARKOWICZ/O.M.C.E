package OMCE.OMCE.User;

import OMCE.OMCE.Enderco.DadosEndereco;

public record DadosAlterarDadosUser (Long id , String nome, String cpf, String dataNasc, String sexo,
                                     DadosEndereco endereco, String email, String telefone, String nomeUser,
                                     String senha){
}
