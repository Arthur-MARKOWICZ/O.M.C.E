package OMCE.OMCE.Validacao;

import OMCE.OMCE.User.DadosAlterarDadosUser;
import OMCE.OMCE.User.DadosCadastroUser;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoUser {
    public void validarCadastroUsuario(DadosCadastroUser dados){
        if(dados.email() == null){
            throw  new RuntimeException("o campo de e-mail esta em branco");
        }
        if(dados.senha() == null){
            throw new RuntimeException("O campo de senha esta em branco");
        }
        if(dados.nome() == null){
            throw  new RuntimeException("o campo de nome esta em branco");
        }
        if(dados.cpf() == null){
            throw new RuntimeException("o campo de cpf esta em branco");
        }
        if(dados.endereco() == null){
            throw  new RuntimeException("Um dos campos de endereco esta em branco");
        }
        if(dados.nomeUser() == null){
            throw new RuntimeException("o campo de nome de usuario esta em branco");
        }
        if(dados.dataNasc() == null){
            throw  new RuntimeException("o campo de data de nascimento esta em branco ");
        }
        if (dados.telefone() == null){
            throw  new RuntimeException("o campo de telefone esta em branco");
        }
        if(dados.sexo() == null){
            throw  new RuntimeException("o campo de sexo esta em branco");
        }
    }
    public void validarAlterarUsuario(DadosAlterarDadosUser dados){
        if(dados.email() == null){
            throw  new RuntimeException("o campo de e-mail esta em branco");
        }
        if(dados.senha() == null){
            throw new RuntimeException("O campo de senha esta em branco");
        }
        if(dados.nome() == null){
            throw  new RuntimeException("o campo de nome esta em branco");
        }
        if(dados.cpf() == null){
            throw new RuntimeException("o campo de cpf esta em branco");
        }
        if(dados.endereco() == null){
            throw  new RuntimeException("Um dos campos de endereco esta em branco");
        }
        if(dados.nomeUser() == null){
            throw new RuntimeException("o campo de nome de usuario esta em branco");
        }
        if(dados.dataNasc() == null){
            throw  new RuntimeException("o campo de data de nascimento esta em branco ");
        }
        if (dados.telefone() == null){
            throw  new RuntimeException("o campo de telefone esta em branco");
        }

    }
}
