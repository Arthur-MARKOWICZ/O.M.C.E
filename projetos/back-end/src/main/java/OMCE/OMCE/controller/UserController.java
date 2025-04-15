package OMCE.OMCE.controller;

import OMCE.OMCE.User.DadosAlterarDadosUser;
import OMCE.OMCE.User.DadosCadastroUser;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.UserRepository;
import OMCE.OMCE.Validacao.ValidacaoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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

}
