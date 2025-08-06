package OMCE.OMCE;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@Profile("test")
public class NoSecurityConfig  {
    @Bean
    public HttpSecurity filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf ->csrf.disable()) // Disables CSRF protection
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Ensures no sessions are created (common for REST APIs)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());// Allows all requests without authentication/authorization
    }
}
