package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParadaDTO {
    private String stop_id;
    private String stop_name;
    private Double stop_lat;
    private Double stop_lon;

}
