package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.SerperResponse;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.TomTomResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TrafficIntegrationService {
    private final RestTemplate restTemplate;

    @Value("${api.tomtom.key}")
    private String tomtomKey;

    @Value("${api.serper.key}")
    private String serperKey;

    public TrafficIntegrationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Agora retorna TomTomResponse em vez de String
    public TomTomResponse getTomTomFlow(double lat, double lon) {
        String url = String.format(
                "https://api.tomtom.com/traffic/services/4/flowSegmentData/absolute/10/json?key=%s&point=%f,%f",
                tomtomKey, lat, lon
        );
        // O RestTemplate converte o JSON automaticamente para o seu Record
        return restTemplate.getForObject(url, TomTomResponse.class);
    }

    // Agora retorna SerperResponse em vez de String
    public SerperResponse getTrafficNews(String query) {
        String url = "https://google.serper.dev/search";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", serperKey);

        // Configura a busca para o Brasil e em Português
        Map<String, String> body = Map.of(
                "q", query + " trânsito agora",
                "gl", "br",
                "hl", "pt-br"
        );

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(url, request, SerperResponse.class);
    }
}
