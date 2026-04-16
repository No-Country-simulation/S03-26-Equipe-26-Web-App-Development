package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GtfsClientService {
    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:8000";

    public Object getCidades() {
        return restTemplate.getForObject(
                BASE_URL + "/cidades",
                Object.class
        );
    }

    public Object getStops(String cidade) {
        return restTemplate.getForObject(
                BASE_URL + "/stops/" + cidade,
                Object.class
        );
    }

    public Object getRoutes(String cidade) {
        return restTemplate.getForObject(
                BASE_URL + "/routes/" + cidade,
                Object.class
        );
    }

    public Object getHeatmap(String cidade) {
        return restTemplate.getForObject(
                BASE_URL + "/heatmap/" + cidade,
                Object.class
        );
    }
}
