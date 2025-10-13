package OMCE.OMCE.config;

import OMCE.OMCE.User.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationStrategy implements AuthenticationStrategy {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public JwtAuthenticationStrategy(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(String token) {

        return token != null && token.startsWith("Bearer ");
    }

    @Override
    public Authentication authenticate(String token) {
        String rawToken = token.replace("Bearer ", "");
        String email = tokenService.validateToken(rawToken);
        if (email == null) return null;

        UserDetails user = userRepository.findByEmail(email);
        if (user == null) return null;

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
}
