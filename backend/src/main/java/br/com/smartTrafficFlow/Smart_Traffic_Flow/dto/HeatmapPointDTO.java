package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeatmapPointDTO {

    private double lat;
    private double lng;
    private double intensity;

    public HeatmapPointDTO(double lat, double lng, double intensity) {
        this.lat = lat;
        this.lng = lng;
        this.intensity = intensity;
    }
}

