package OMCE.OMCE.User.Service;

import OMCE.OMCE.Execao.SenhaIgualAOriginal;
import OMCE.OMCE.Execao.UserNaoEncontrado;
import OMCE.OMCE.User.*;
import OMCE.OMCE.User.dto.DadosAlterarDadosUser;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import OMCE.OMCE.User.dto.DadosRedefinirSenha;
import OMCE.OMCE.User.dto.DadosSolicitarRedefinicaoSenha;
import OMCE.OMCE.User.repository.UserRepository;
import OMCE.OMCE.Validacao.ValidacaoUser;
import OMCE.OMCE.config.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ValidacaoUser validar;
    @Autowired
    private EmailService emailService;

    private BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();
    public void alterardados(DadosAlterarDadosUser dados){
        validar.validarAlterarUsuario(dados);
        User user =  userRepository.getReferenceById(dados.id());
        if(!encoder.matches(dados.senha(),user.getSenha())){
            throw  new RuntimeException("Senha diferente da original");
        }

        String novoHash = null;
        if (dados.novaSenha() != null && !dados.novaSenha().isEmpty()) {
            novoHash = encoder.encode(dados.novaSenha());
        }
        user.alterarDados(dados,novoHash);
        userRepository.save(user);
    }
    public User pegarUserPorId(Long id){
        User user = userRepository.getReferenceById(id);
        return user;
    }
    public void excluir(Long id){
        User user = userRepository.getReferenceById(id);
        user.excluir();
    }
    public void redefinirSenhaPorEmail(DadosSolicitarRedefinicaoSenha dados){
        User usuario = userRepository.findByEmail(dados.email());
        if (usuario == null) {
            throw new UserNaoEncontrado("Usuário nao encontrado");
        }
        String token = UUID.randomUUID().toString();
        usuario.setTokenRedefinicao(token);
        usuario.setTokenExpiracao(LocalDateTime.now().plusMinutes(30));
        userRepository.save(usuario);
        String link = "http://127.0.0.1:5500/O.M.C.E/projetos/front-end/html/novaSenha.html?token=" + token;
        String assunto = "Redefinição de Senha - OMCE";
        String corpo = "Olá, " + usuario.getNome() + "!\n\n" +
                "Recebemos uma solicitação para redefinir sua senha. " +
                "Clique no link abaixo para criar uma nova senha (válido por 30 minutos):\n\n" +
                link + "\n\n" +
                "Se você não solicitou isso, ignore este e-mail.";
        emailService.enviarEmail(usuario.getEmail(), assunto, corpo);
        log.info("Email enviado com sucesso");
    }
    @Transactional
    public void novaSenha(DadosRedefinirSenha dados){
        User user = userRepository.findByTokenRedefinicao(dados.token());
        if (user == null) {
           throw  new UserNaoEncontrado("User nao foi encontrado");
        }
        BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();
        if(encoder.matches(dados.novaSenha(),user.getSenha())){
           throw  new SenhaIgualAOriginal("Senha nova igual a original");
        }
        String novoHash =  encoder.encode(dados.novaSenha());
        user.setSenha(novoHash);
    }
    public User cadastro(DadosCadastroUser dados){
        validar.validarCadastroUsuario(dados);
        String encryptedPassword = new BCryptPasswordEncoder().encode(dados.senha());
        User newUser = new User(dados);
        newUser.setSenha(encryptedPassword);
        userRepository.save(newUser);
        return newUser;
    }
}
