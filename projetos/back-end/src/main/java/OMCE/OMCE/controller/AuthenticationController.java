package OMCE.OMCE.controller;

import OMCE.OMCE.Produto.repository.ProdutoRepository;
import OMCE.OMCE.User.*;
import OMCE.OMCE.User.dto.AuthenticationDTO;
import OMCE.OMCE.User.dto.DadosCadastroUser;
import OMCE.OMCE.User.dto.DadosRedefinirSenha;
import OMCE.OMCE.User.dto.LoginResponseDTO;
import OMCE.OMCE.User.repository.UserRepository;
import OMCE.OMCE.Validacao.ValidacaoUser;
import OMCE.OMCE.config.TokenService;
import OMCE.OMCE.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthorizationService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthenticationDTO dto) {
        LoginResponseDTO dtoResponse = service.login(dto);
        return ResponseEntity.ok(dtoResponse);
    }



    @PostMapping("/redefinirSenha")
    public ResponseEntity<?> redefinirSenha(@RequestBody DadosRedefinirSenha dados) {
        service.redefinirSenha(dados);
        return ResponseEntity.ok("Senha redefinida com sucesso!");
    }

}
