package OMCE.OMCE.controller;


import OMCE.OMCE.User.*;

import OMCE.OMCE.User.Service.UserService;
import OMCE.OMCE.User.dto.DadosAlterarDadosUser;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import OMCE.OMCE.User.dto.DadosRedefinirSenha;
import OMCE.OMCE.User.repository.UserRepository;
import OMCE.OMCE.Validacao.ValidacaoUser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import OMCE.OMCE.User.dto.DadosSolicitarRedefinicaoSenha;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private ValidacaoUser validar;
    @Autowired
    private UserService userService;
    @GetMapping("/{id}")
    public  ResponseEntity<User> pegarUsuario(@PathVariable Long id){
       User user = userService.pegarUserPorId(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/alterardados")
    @Transactional
    public ResponseEntity<Void> alterardados(@RequestBody DadosAlterarDadosUser dados){
       userService.alterardados(dados);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("deletar/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        userService.excluir(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/cadastro")
    public ResponseEntity<Void> cadastro(@RequestBody DadosCadastroUser dados) {
        userService.cadastro(dados);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/redefinirSenha")
    public ResponseEntity<?> redefinirSenha(@RequestBody DadosSolicitarRedefinicaoSenha dados) {
        log.info("Requisicao redefinirSenha recebida");
        userService.redefinirSenhaPorEmail(dados);
        return ResponseEntity.ok("Enviando email...");
    }
    @PutMapping("/novaSenha")
    @Transactional
    public ResponseEntity<String> novaSenha(@RequestBody DadosRedefinirSenha dados){
        userService.novaSenha(dados);
       return ResponseEntity.ok("senha alterada com sucesso");
    }



}
