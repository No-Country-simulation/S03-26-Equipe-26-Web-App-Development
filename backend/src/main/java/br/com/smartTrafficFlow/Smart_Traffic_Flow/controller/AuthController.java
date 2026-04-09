package br.com.smartTrafficFlow.Smart_Traffic_Flow.controller;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.DadosLogin;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.DadosLoginGoogle;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.DadosTokenJWT;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.User;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.repository.UserRepository;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.security.SecurityUser;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.service.JwtService;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.service.TokenService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private JwtService jwtService;


    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public User register(@RequestBody User usuario){

        usuario.setSenha(encoder.encode(usuario.getSenha()));
        usuario.setProvider("LOCAL");

        return repository.save(usuario);
    }


    // ADICIONE ESTAS LINHAS AQUI (Declaração da variável)
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @PostMapping("/google")
    public ResponseEntity loginGoogle(@RequestBody DadosLoginGoogle dados) {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        try {
            String tokenLimpo = dados.idToken().trim();
            GoogleIdToken idToken = verifier.verify(tokenLimpo);

            if (idToken != null) {
                var payload = idToken.getPayload();
                String email = payload.getEmail();
                String nome = (String) payload.get("name");

                User usuario = repository.findByEmail(email)
                        .orElseGet(() -> {
                            User novo = new User();
                            novo.setEmail(email);
                            novo.setNome(nome);
                            novo.setProvider("GOOGLE");
                            novo.setSenha("");
                            return repository.save(novo);
                        });

                String tokenInterno = jwtService.generateToken(usuario.getEmail());  // ✅ USAR JwtService
                return ResponseEntity.ok(new DadosTokenJWT(tokenInterno));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erro na validação: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody DadosLogin dados) {
        var authToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = authenticationManager.authenticate(authToken);
        var securityUser = (SecurityUser) authentication.getPrincipal();

        String token = jwtService.generateToken(securityUser.getUsername());  // ✅ USAR JwtService
        return ResponseEntity.ok(new DadosTokenJWT(token));
    }
}
