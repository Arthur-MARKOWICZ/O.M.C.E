package OMCE.OMCE.User;

import OMCE.OMCE.Enderco.DadosEnderco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public record  DadosCadastroUser (String nome, String cpf, String dataNasc, String sexo,
                                    DadosEnderco endereco,String email,String telefone, String nomeUser,
                                    String senha) {


}
