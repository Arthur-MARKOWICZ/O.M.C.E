package OMCE.OMCE.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticationContext {

    private final List<AuthenticationStrategy> strategies;

    public AuthenticationContext(List<AuthenticationStrategy> strategies) {
        this.strategies = strategies;
    }

    public Authentication authenticate(String token) {
        for (AuthenticationStrategy strategy : strategies) {
            if (strategy.supports(token)) {
                return strategy.authenticate(token);
            }
        }
        return null;
    }
}
