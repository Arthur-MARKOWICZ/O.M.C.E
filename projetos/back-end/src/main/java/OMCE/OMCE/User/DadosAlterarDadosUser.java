package OMCE.OMCE.User;

import OMCE.OMCE.Enderco.DadosEnderco;

public record DadosAlterarDadosUser (Long id ,String nome, String cpf, String dataNasc, String sexo,
                                     DadosEnderco endereco, String email, String telefone, String nomeUser,
                                     String senha){
}
