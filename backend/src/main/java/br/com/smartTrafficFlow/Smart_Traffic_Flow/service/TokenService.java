package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    //Use uma chave forte. Em produção, use variaveis de ambiente.
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(String email) {
        return Jwts.builder()
                .issuer("SmartTrafficFlow")
                .subject(email)
                .expiration(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1h
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public String validarToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();

        } catch (JwtException e){
            return null; //token invalido ou expirado
        }
    }
}
