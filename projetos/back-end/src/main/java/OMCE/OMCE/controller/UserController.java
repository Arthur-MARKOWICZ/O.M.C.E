package OMCE.OMCE.controller;

import OMCE.OMCE.User.DadosAlterarDadosUser;
import OMCE.OMCE.User.DadosCadastroUser;
import OMCE.OMCE.User.DadosRedefinirSenha;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.UserRepository;
import OMCE.OMCE.Validacao.ValidacaoUser;
import OMCE.OMCE.config.EmailService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import OMCE.OMCE.User.DadosSolicitarRedefinicaoSenha;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ValidacaoUser validar;
    @GetMapping("/{id}")
    public  ResponseEntity pegarUsuario(@PathVariable Long id){
        var user  = userRepository.findById(id);
        return ResponseEntity.ok(user.get());
    }

    @PutMapping("/alterardados")
    @Transactional
    public ResponseEntity alterardados(@RequestBody DadosAlterarDadosUser dados){
        System.out.println(dados);
        validar.validarAlterarUsuario(dados);
        var user =  userRepository.getReferenceById(dados.id());
        user.alterarDados(dados);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("deletar/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        var user = userRepository.getReferenceById(id);
        user.excluir();
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private EmailService emailService;
    @PostMapping("/redefinirSenha")
    public ResponseEntity<?> redefinirSenha(@RequestBody DadosSolicitarRedefinicaoSenha dados) {
        var usuario = userRepository.findByEmail(dados.email());
        if (usuario == null) {
            return ResponseEntity.badRequest().body("Email não encontrado.");
        }
        String token = UUID.randomUUID().toString();
        usuario.setTokenRedefinicao(token);
        usuario.setTokenExpiracao(LocalDateTime.now().plusMinutes(30));
        userRepository.save(usuario);
        String link = "http://localhost:5500/html/novaSenha.html?token=" + token;
        String assunto = "Redefinição de Senha - OMCE";
        String corpo = "Olá, " + usuario.getNome() + "!\n\n" +
                       "Recebemos uma solicitação para redefinir sua senha. " +
                       "Clique no link abaixo para criar uma nova senha (válido por 30 minutos):\n\n" +
                       link + "\n\n" +
                       "Se você não solicitou isso, ignore este e-mail.";
        emailService.enviarEmail(usuario.getEmail(), assunto, corpo);
        return ResponseEntity.ok("Enviando email...");
    }

}
