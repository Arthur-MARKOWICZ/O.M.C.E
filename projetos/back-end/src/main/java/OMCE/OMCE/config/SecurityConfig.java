package OMCE.OMCE.config;

import OMCE.OMCE.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    SecurityFilter  securityFilter;

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository)    {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf ->csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/user/cadastro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/redefinirSenha").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/user/novaSenha").permitAll()
                        .requestMatchers("/v3/api-docs/**","/swagger-ui.html","swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/produto/visualizarDetalhesProduto/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/produto/todos/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/produto/filtro**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/avaliacaoVendedor/media/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/avaliacoes/produto/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/carrinho/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/carrinho/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/carrinho/**").permitAll()
                        .anyRequest().authenticated())
                .cors(withDefaults())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
