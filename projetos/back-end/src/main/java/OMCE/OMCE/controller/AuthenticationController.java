package OMCE.OMCE.controller;

import OMCE.OMCE.Produto.DadosCadastroProduto;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.ProdutoRepository;
import OMCE.OMCE.User.*;
import OMCE.OMCE.config.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        var usermanePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usermanePassword);
        var id = ((User) auth.getPrincipal()).getId();
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token, id));
    }

    @PostMapping("/cadastro")
    public ResponseEntity cadastro(@RequestBody DadosCadastroUser dados) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(dados.senha());
        User newUser = new User(dados);
        newUser.setSenha(encryptedPassword);
        this.userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cadastroProduto")
    public ResponseEntity cadastroProduto(@RequestBody DadosCadastroProduto dados) {
        Produto newproduto = new Produto(dados);
        System.out.println(newproduto);
        this.produtoRepository.save(newproduto);
        return ResponseEntity.ok().build();
    }
}
