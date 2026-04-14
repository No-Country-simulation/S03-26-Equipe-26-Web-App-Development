package br.com.smartTrafficFlow.Smart_Traffic_Flow.controller;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.DadosLogin;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.DadosLoginGoogle;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.DadosTokenJWT;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.RegisterDTO;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.User;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.repository.UserRepository;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.security.SecurityUser;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.service.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173") // Adicione CORS
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO data) {

        System.out.println("========== REGISTER ==========");
        System.out.println("Nome: " + data.nome());
        System.out.println("Email: " + data.email());
        System.out.println("Senha: " + data.senha());

        // validação básica
        if (data.email() == null || data.senha() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email e senha são obrigatórios"));
        }

        // email já existe
        if (repository.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Email já cadastrado"));
        }

        // cria usuário
        User user = new User();
        user.setNome(data.nome());
        user.setEmail(data.email());
        user.setSenha(encoder.encode(data.senha()));
        user.setProvider("LOCAL");

        User saved = repository.save(user);

        String token = jwtService.generateToken(saved.getEmail());

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "user", Map.of(
                                "id", saved.getId(),
                                "nome", saved.getNome(),
                                "email", saved.getEmail(),
                                "provider", saved.getProvider()
                        )
                )
        );
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody DadosLogin dados) {

        var auth = new UsernamePasswordAuthenticationToken(
                dados.email(),
                dados.senha()
        );

        var authentication = authenticationManager.authenticate(auth);
        var userSecurity = (SecurityUser) authentication.getPrincipal();

        String token = jwtService.generateToken(userSecurity.getUsername());

        var user = repository.findByEmail(dados.email()).orElse(null);

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "user", user != null ? Map.of(
                                "id", user.getId(),
                                "nome", user.getNome(),
                                "email", user.getEmail(),
                                "provider", user.getProvider()
                        ) : null
                )
        );
    }

    // =========================
    // GOOGLE LOGIN
    // =========================
    @PostMapping("/google")
    public ResponseEntity<?> loginGoogle(@RequestBody DadosLoginGoogle dados) {

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(dados.idToken().trim());

            if (idToken == null) {
                return ResponseEntity.status(401)
                        .body(Map.of("message", "Token Google inválido"));
            }

            var payload = idToken.getPayload();
            String email = payload.getEmail();
            String nome = (String) payload.get("name");

            User user = repository.findByEmail(email)
                    .orElseGet(() -> {
                        User novo = new User();
                        novo.setEmail(email);
                        novo.setNome(nome);
                        novo.setProvider("GOOGLE");
                        novo.setSenha("");
                        return repository.save(novo);
                    });

            String token = jwtService.generateToken(user.getEmail());

            return ResponseEntity.ok(
                    Map.of(
                            "token", token,
                            "user", Map.of(
                                    "id", user.getId(),
                                    "nome", user.getNome(),
                                    "email", user.getEmail(),
                                    "provider", user.getProvider()
                            )
                    )
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
