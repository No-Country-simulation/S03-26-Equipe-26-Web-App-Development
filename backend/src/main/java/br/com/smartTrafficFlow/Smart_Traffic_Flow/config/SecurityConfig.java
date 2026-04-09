package br.com.smartTrafficFlow.Smart_Traffic_Flow.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.authorization.SingleResultAuthorizationManager.permitAll;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter){
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Configuração de CORS para permitir que o React (porta 5173) acesse o Java
                .cors(Customizer.withDefaults())

                // 2. Desabilitar CSRF para APIs REST
                .csrf(csrf -> csrf.disable())

                // 3. Importante: H2 Console e Frames
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // 4. Gerenciamento de Sessão:
                // Usamos STATELESS para o JWT, mas o OAuth2 precisa criar uma sessão temporária
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(
                                "/login/**",
                                "/oauth2/**",
                                "/error",
                                "/favicon.ico"
                        ).permitAll()

                        // Garante que os recursos do React não sejam bloqueados
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )

                // 5. Configuração do OAuth2 para o Google
                .oauth2Login(oauth2 -> oauth2
                        // Após o sucesso no Google, ele volta para o seu Dashboard no React
                        .defaultSuccessUrl("http://localhost:5173/dashboard", true)
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Configuração de CORS: Essencial para evitar erros de bloqueio no navegador
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Porta do seu Vite/React
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

