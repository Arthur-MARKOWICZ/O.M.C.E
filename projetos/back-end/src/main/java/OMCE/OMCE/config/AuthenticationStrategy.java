package OMCE.OMCE.config;

import org.springframework.security.core.Authentication;

public interface AuthenticationStrategy {
    boolean supports(String token);
    Authentication authenticate(String token);
}
