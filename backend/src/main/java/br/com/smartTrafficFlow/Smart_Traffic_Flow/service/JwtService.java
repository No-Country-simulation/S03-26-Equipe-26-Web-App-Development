package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] secretBytes = secret.getBytes();
        System.out.println("🔑 Chave JWT carregada - Tamanho: " + secretBytes.length + " bytes");
        key = Keys.hmacShaKeyFor(secretBytes);
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        String token = Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();

        System.out.println("✅ Token gerado para: " + email);
        return token;
    }

    public String getEmail(String token) {
        try {
            Claims claims = getClaims(token);
            String email = claims.getSubject();
            System.out.println("📧 Email extraído: " + email);
            return email;
        } catch (Exception e) {
            System.out.println("❌ Erro ao extrair email: " + e.getMessage());
            return null;
        }
    }

    public boolean isValid(String token) {
        try {
            getClaims(token);
            System.out.println("✅ Token válido");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Token inválido: " + e.getMessage());
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
