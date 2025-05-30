package OMCE.OMCE.controller;


import OMCE.OMCE.User.*;

import OMCE.OMCE.Validacao.ValidacaoUser;
import OMCE.OMCE.config.EmailService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    @Autowired
    private UserService userService;
    @GetMapping("/{id}")
    public  ResponseEntity pegarUsuario(@PathVariable Long id){
        var user  = userRepository.findById(id);
        System.out.println(user);
        return ResponseEntity.ok(user.get());
    }

    @PutMapping("/alterardados")
    @Transactional
    public ResponseEntity alterardados(@RequestBody DadosAlterarDadosUser dados){
       userService.alterardados(dados);
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
        String link = "http://localhost:5500/front-end/html/novaSenha.html?token=" + token;
        String assunto = "Redefinição de Senha - OMCE";
        String corpo = "Olá, " + usuario.getNome() + "!\n\n" +
                       "Recebemos uma solicitação para redefinir sua senha. " +
                       "Clique no link abaixo para criar uma nova senha (válido por 30 minutos):\n\n" +
                       link + "\n\n" +
                       "Se você não solicitou isso, ignore este e-mail.";
        emailService.enviarEmail(usuario.getEmail(), assunto, corpo);
        return ResponseEntity.ok("Enviando email...");
    }
    @PutMapping("/novaSenha")
    @Transactional
    public ResponseEntity novaSenha(@RequestBody DadosRedefinirSenha dados){
        User user = userRepository.findByTokenRedefinicao(dados.token());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
        BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();
        if(encoder.matches(dados.novaSenha(),user.getSenha())){
            return ResponseEntity.badRequest().body(" Senha nova igual a original");
        }
        String novoHash =  encoder.encode(dados.novaSenha());
       user.setSenha(novoHash);
       return ResponseEntity.ok("senha alterada com sucesso");
    }



}
