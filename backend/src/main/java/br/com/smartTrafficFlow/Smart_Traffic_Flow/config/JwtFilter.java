package br.com.smartTrafficFlow.Smart_Traffic_Flow.config;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.User;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.repository.UserRepository;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        System.out.println("=== JWT FILTER DEBUG ===");
        System.out.println("URL: " + request.getRequestURL());
        System.out.println("Header Authorization: " + header);

        // ✅ NÃO EXISTE TOKEN → segue fluxo normal
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        // ✅ TOKEN VAZIO → ignora
        if (token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String email = jwtService.getEmail(token);
            System.out.println("Email extraído: " + email);

            if (email != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                User user = userRepository.findByEmail(email).orElse(null);

                if (user != null && jwtService.isValid(token)) {

                    System.out.println("✅ Token válido. Autenticando usuário...");

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);

                    System.out.println("✅ Usuário autenticado com sucesso!");
                }
            }

        } catch (Exception ex) {
            // ✅ IMPORTANTE: nunca quebrar request
            System.err.println("Erro JWT (ignorado): " + ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * ✅ ROTAS QUE NÃO DEVEM PASSAR PELO JWT
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getServletPath();

        return path.startsWith("/login") ||
                path.startsWith("/oauth2") ||
                path.startsWith("/actuator") ||
                path.startsWith("/h2-console") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.equals("/") ||
                path.equals("/error") ||
                path.equals("/favicon.ico");
    }
}
