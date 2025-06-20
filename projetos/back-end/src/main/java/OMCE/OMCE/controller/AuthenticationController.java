package OMCE.OMCE.controller;

import OMCE.OMCE.Produto.DadosCadastroProduto;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.Produto.ProdutoRepository;
import OMCE.OMCE.User.*;
import OMCE.OMCE.Validacao.ValidacaoUser;
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

import java.time.LocalDateTime;
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
    @Autowired
    private ValidacaoUser validar;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var authentication = authenticationManager.authenticate(usernamePassword);
        var user = (User) authentication.getPrincipal();
       if(!user.isAtivo()){
           return ResponseEntity.badRequest().build();
       }
        var token = tokenService.generateToken(user);
       Long idUser = user.getId();
       String nome = user.getNomeUser();
        return ResponseEntity.ok(new LoginResponseDTO(token,idUser,nome));
    }

    @PostMapping("/cadastro")
    public ResponseEntity cadastro(@RequestBody DadosCadastroUser dados) {
        validar.validarCadastroUsuario(dados);
        String encryptedPassword = new BCryptPasswordEncoder().encode(dados.senha());
        User newUser = new User(dados);
        newUser.setSenha(encryptedPassword);
        this.userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/redefinirSenha")
    public ResponseEntity<?> redefinirSenha(@RequestBody DadosRedefinirSenha dados) {
    User user = userRepository.findByTokenRedefinicao(dados.token());
    if (user == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido.");
    }
    if (user.getTokenExpiracao() == null || user.getTokenExpiracao().isBefore(LocalDateTime.now())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expirado.");
    }
    String novaSenhaHash = new BCryptPasswordEncoder().encode(dados.novaSenha());
    user.setSenha(novaSenhaHash);
    user.setTokenRedefinicao(null);
    user.setTokenExpiracao(null);
    userRepository.save(user);
    return ResponseEntity.ok("Senha redefinida com sucesso!");
    }

}
