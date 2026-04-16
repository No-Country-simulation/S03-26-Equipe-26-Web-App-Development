package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;


import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.CidadeAnaliseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class GTFSAnalyticsService {

    private final RestTemplate restTemplate;

    @Value("${gtfs.service.url:http://localhost:8000}")
    private String gtfsServiceUrl;

    public GTFSAnalyticsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CidadeAnaliseDTO getAnaliseCidade(String cidade) {
        try {
            String url = gtfsServiceUrl + "/api/analytics/analise/" + cidade;
            return restTemplate.getForObject(url, CidadeAnaliseDTO.class);
        } catch (RestClientException e) {
            System.err.println("Erro ao buscar análise de " + cidade + ": " + e.getMessage());
            return null;
        }
    }

    public Map<String, CidadeAnaliseDTO> getAnaliseTodasCidades() {
        try {
            String url = gtfsServiceUrl + "/api/analytics/analise/todas";
            return restTemplate.getForObject(url, Map.class);
        } catch (RestClientException e) {
            System.err.println("Erro ao buscar análise de todas cidades: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    public Map<String, Object> getHeatmap(String cidade, Integer horaInicio, Integer horaFim) {
        try {
            StringBuilder url = new StringBuilder(gtfsServiceUrl + "/api/analytics/heatmap/" + cidade);
            if (horaInicio != null && horaFim != null) {
                url.append("?hora_inicio=").append(horaInicio).append("&hora_fim=").append(horaFim);
            }
            return restTemplate.getForObject(url.toString(), Map.class);
        } catch (RestClientException e) {
            System.err.println("Erro ao buscar heatmap: " + e.getMessage());
            return Collections.emptyMap();
        }
    }
}
