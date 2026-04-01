package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SPTransService {

    @Value("${sptrans.token}")
    private String token;

    @Value("${sptrans.api.url}")
    private String apiUrl; // Certifique-se que no application.properties está http://api.olhovivo.sptrans.com.br/v2.1

    private String sessionCookie;
    private final RestTemplate restTemplate = new RestTemplate();

    // Autenticação
    public boolean authenticate() {
        try {
            // Construímos a URL garantindo que não existam barras duplas //
            String url = apiUrl + "/Login/Autenticar?token=" + token;

            // Criamos um header vazio apenas para garantir a requisição POST
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Boolean> response = restTemplate.postForEntity(url, entity, Boolean.class);

            if (response.getStatusCode().is2xxSuccessful() && Boolean.TRUE.equals(response.getBody())) {
                List<String> cookies = response.getHeaders().get("Set-Cookie");
                if (cookies != null && !cookies.isEmpty()) {
                    // Pegamos apenas a parte necessária do cookie (antes do ponto e vírgula)
                    this.sessionCookie = cookies.get(0).split(";")[0];
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao autenticar na SPTrans: " + e.getMessage());
        }
        return false;
    }

    // Buscar posição dos ônibus
    public String getBusPositions() {
        if (this.sessionCookie == null) {
            if (!authenticate()) return "Falha na autenticação";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionCookie);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl + "/Posicao",
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            return response.getBody();
        } catch (Exception e) {
            return "Erro ao buscar posições: " + e.getMessage();
        }
    }
}
