package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TomTomService {

    @Value("${tomtom.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public TomTomService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public double getTrafficIntensity(double lat, double lon) {

        String url =
                "https://api.tomtom.com/traffic/services/4/flowSegmentData/absolute/10/json"
                        + "?point=" + lat + "," + lon
                        + "&unit=KMPH"
                        + "&key=" + apiKey;

        Map response =
                restTemplate.getForObject(url, Map.class);

        Map flow =
                (Map) response.get("flowSegmentData");

        double current =
                ((Number) flow.get("currentSpeed")).doubleValue();

        double free =
                ((Number) flow.get("freeFlowSpeed")).doubleValue();

        double intensity = 1 - (current / free);

        return Math.max(0, Math.min(1, intensity));
    }
}
