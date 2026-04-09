package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.RouteDTO;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.StopDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;

@Service
public class TransporteIntegrationService {

    private final RestTemplate restTemplate;

    @Value("${transporte.service.url:http://localhost:8000}")
    private String transporteServiceUrl;

    public TransporteIntegrationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Lista todas as cidades disponíveis
     */
    public List<String> getCidades() {
        try {
            String url = transporteServiceUrl + "/api/cidades";
            ResponseEntity<List<String>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<String>>() {}
            );
            return response.getBody();
        } catch (RestClientException e) {
            System.err.println("Erro ao buscar cidades: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Busca paradas de uma cidade com paginação
     */
    public List<StopDTO> getStops(String cidade, Integer limit, Integer offset) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(transporteServiceUrl + "/api/stops/" + cidade)
                    .queryParam("limit", limit != null ? limit : 100)
                    .queryParam("offset", offset != null ? offset : 0)
                    .build()
                    .toUriString();

            ResponseEntity<List<StopDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<StopDTO>>() {}
            );
            return response.getBody();
        } catch (RestClientException e) {
            System.err.println("Erro ao buscar paradas de " + cidade + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Busca rotas de uma cidade
     */
    public List<RouteDTO> getRoutes(String cidade, Integer limit) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(transporteServiceUrl + "/api/routes/" + cidade)
                    .queryParam("limit", limit != null ? limit : 50)
                    .build()
                    .toUriString();

            ResponseEntity<List<RouteDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<RouteDTO>>() {}
            );
            return response.getBody();
        } catch (RestClientException e) {
            System.err.println("Erro ao buscar rotas de " + cidade + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Busca paradas próximas a uma localização
     */
    public List<StopDTO> getStopsNearby(String cidade, double lat, double lon, double radiusKm) {
        List<StopDTO> allStops = getStops(cidade, 1000, 0);
        if (allStops == null || allStops.isEmpty()) {
            return Collections.emptyList();
        }

        return allStops.stream()
                .filter(stop -> stop.getStopLat() != null && stop.getStopLon() != null)
                .filter(stop -> calculateDistance(lat, lon, stop.getStopLat(), stop.getStopLon()) <= radiusKm)
                .limit(20)
                .toList();
    }

    /**
     * Calcula distância entre dois pontos (Haversine)
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
