package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrafficVolumeResponse {

    private String location;
    private String hour;
    private int volume;
    private int currentSpeed;
    private int freeFlowSpeed;
    private String status;
    private double confidence;

    public TrafficVolumeResponse() {}
}
