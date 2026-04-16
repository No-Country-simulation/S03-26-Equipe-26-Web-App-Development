package br.com.smartTrafficFlow.Smart_Traffic_Flow.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/teste")
public class TesteController {

    @GetMapping("/me")
    public Map<String, Object> usuario(@AuthenticationPrincipal OAuth2User user) {

        if (user == null) {
            return Map.of("status", "Não autenticado");
        }

        Map<String, Object> attr = user.getAttributes();

        return Map.of(
                "nome", attr.getOrDefault("name", "sem nome"),
                "email", attr.getOrDefault("email", "sem email"),
                "foto", attr.getOrDefault("picture", "")
        );
    }
}