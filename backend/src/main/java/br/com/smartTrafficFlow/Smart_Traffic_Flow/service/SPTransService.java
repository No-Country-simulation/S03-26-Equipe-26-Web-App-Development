package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.Bus;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class SPTransService {

    final RestTemplate restTemplate;

    public SPTransService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${sptrans.token}")
    private String token;

    @Value("${sptrans.api.url}")
    private String apiUrl;

    private String sessionCookie;

    private final ObjectMapper mapper = new ObjectMapper();

    // =====================================================
    // LOGIN AUTOMÁTICO AO SUBIR O SISTEMA
    // =====================================================
    @PostConstruct
    public void init() {
        authenticate();
    }

    // =====================================================
    // AUTENTICAR
    // =====================================================
    private synchronized boolean authenticate() {

        try {

            String url = apiUrl + "/Login/Autenticar?token=" + token;

            ResponseEntity<Boolean> response =
                    restTemplate.postForEntity(
                            url,
                            null,
                            Boolean.class
                    );

            if (Boolean.TRUE.equals(response.getBody())) {

                List<String> cookies =
                        response.getHeaders().get(HttpHeaders.SET_COOKIE);

                if (cookies != null && !cookies.isEmpty()) {

                    sessionCookie = cookies.get(0).split(";")[0];

                    System.out.println("✅ SPTrans autenticado com sucesso");

                    return true;
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Falha autenticação SPTrans: " + e.getMessage());
        }

        return false;
    }

    // =====================================================
    // CHAMADA SEGURA COM AUTO RELOGIN
    // =====================================================
    private ResponseEntity<String> call(String endpoint) {

        if (sessionCookie == null) {
            authenticate();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionCookie);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {

            return restTemplate.exchange(
                    apiUrl + "/" + endpoint,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

        } catch (HttpClientErrorException.Unauthorized e) {

            // 🔥 sessão expirou → relogin automático
            System.out.println("🔄 Sessão SPTrans expirou. Reautenticando...");

            authenticate();

            headers = new HttpHeaders();
            headers.add(HttpHeaders.COOKIE, sessionCookie);

            entity = new HttpEntity<>(headers);

            return restTemplate.exchange(
                    apiUrl + "/" + endpoint,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
        }
    }

    // =====================================================
    // POSIÇÃO DOS ÔNIBUS
    // =====================================================
    public List<Bus> getBusPositions() {

        try {

            ResponseEntity<String> response = call("Posicao");

            JsonNode root = mapper.readTree(response.getBody());

            List<Bus> buses = new ArrayList<>();

            JsonNode linhas = root.get("l");

            if (linhas != null) {
                for (JsonNode linha : linhas) {

                    JsonNode veiculos = linha.get("vs");

                    if (veiculos != null) {
                        for (JsonNode v : veiculos) {

                            Bus bus = new Bus();

                            bus.setLatitude(v.get("py").asDouble());
                            bus.setLongitude(v.get("px").asDouble());

                            buses.add(bus);
                        }
                    }
                }
            }

            System.out.println("🚌 Ônibus encontrados: " + buses.size());

            return buses;

        } catch (Exception e) {
            System.err.println("❌ Erro SPTrans: " + e.getMessage());
            return List.of();
        }
    }
}
