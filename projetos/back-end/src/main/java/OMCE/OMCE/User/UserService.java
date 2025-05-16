package OMCE.OMCE.User;

import OMCE.OMCE.Validacao.ValidacaoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ValidacaoUser validar;
    private BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();
    public void alterardados(DadosAlterarDadosUser dados){
        validar.validarAlterarUsuario(dados);
        User user =  userRepository.getReferenceById(dados.id());
        if(!encoder.matches(dados.senha(),user.getSenha())){
            throw  new RuntimeException("Senha diferente da original");
        }
       String novoHash =  encoder.encode(dados.novaSenha());
        user.alterarDados(dados,novoHash);
    }
}
