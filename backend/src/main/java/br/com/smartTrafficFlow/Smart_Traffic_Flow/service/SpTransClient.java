package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SpTransClient {

    @Value("${sptrans.token}")
    private String token;

    private final RestTemplate restTemplate = new RestTemplate();

    private String cookie;

    // =============================
    // LOGIN SPTRANS
    // =============================
    private void autenticar() {

        if (cookie != null) return;

        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "http://api.olhovivo.sptrans.com.br/v2.1/Login/Autenticar?token=" + token,
                        HttpMethod.POST,
                        new HttpEntity<>(headers),
                        String.class
                );

        cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        System.out.println("✅ Cookie SPTrans recebido: " + cookie);
    }

    // =============================
    // BUSCAR POSIÇÃO DOS ÔNIBUS
    // =============================
    public String buscarPosicoes() {

        autenticar();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookie);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "http://api.olhovivo.sptrans.com.br/v2.1/Posicao",
                        HttpMethod.GET,
                        entity,
                        String.class
                );

        return response.getBody();
    }


    @PostConstruct
    public void testToken() {
        System.out.println("SPTRANS TOKEN -> " + token);
    }
}
