package OMCE.OMCE.service;

import OMCE.OMCE.User.User;
import OMCE.OMCE.User.dto.AuthenticationDTO;
import OMCE.OMCE.User.dto.DadosRedefinirSenha;
import OMCE.OMCE.User.dto.LoginResponseDTO;
import OMCE.OMCE.User.repository.UserRepository;
import OMCE.OMCE.config.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado com email: " + username);
        }
        return user;
    }
    public LoginResponseDTO login(AuthenticationDTO dto){
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(dto.email()
                , dto.senha());
        var authentication = authenticationManager.authenticate(usernamePassword);
        User user = (User) authentication.getPrincipal();

        String token = tokenService.generateToken(user);
        long idUser = user.getId();
        String nome = user.getNomeUser();
        return new LoginResponseDTO(token,idUser,nome);
    }
    public void redefinirSenha(DadosRedefinirSenha dados){
        User user = userRepository.findByTokenRedefinicao(dados.token());
        String novaSenhaHash = new BCryptPasswordEncoder().encode(dados.novaSenha());
        user.setSenha(novaSenhaHash);
        user.setTokenRedefinicao(null);
        user.setTokenExpiracao(null);
        userRepository.save(user);

    }



}
