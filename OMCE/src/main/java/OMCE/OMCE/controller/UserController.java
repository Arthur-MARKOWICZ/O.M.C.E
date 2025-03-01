package OMCE.OMCE.controller;

import OMCE.OMCE.User.DadosCadastroUser;
import OMCE.OMCE.User.User;
import OMCE.OMCE.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @Transactional

    public ResponseEntity<String>  cadastrarUser(@RequestBody  DadosCadastroUser dados){
       var user = new User(dados);
       userRepository.save(user);
       return ResponseEntity.ok("Usuário cadastrado com sucesso!");

    }
}
