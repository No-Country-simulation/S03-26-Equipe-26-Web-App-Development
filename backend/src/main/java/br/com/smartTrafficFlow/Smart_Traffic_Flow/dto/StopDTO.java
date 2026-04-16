package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StopDTO {
    @JsonProperty("stop_id")
    private Integer stopId;

    @JsonProperty("stop_name")
    private String stopName;

    @JsonProperty("stop_desc")
    private String stopDesc;

    @JsonProperty("stop_lat")
    private Double stopLat;

    @JsonProperty("stop_lon")
    private Double stopLon;
}
