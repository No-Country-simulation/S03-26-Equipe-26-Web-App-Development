package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class WeatherResponse {

    private MainData main;
    private WeatherDescription[] weather;

    @Getter
    @Setter
    public static class MainData {
        private double temp;
        private int humidity;
    }

    @Getter
    @Setter
    public static class WeatherDescription {
        public String main;
        public String description;
    }


}
