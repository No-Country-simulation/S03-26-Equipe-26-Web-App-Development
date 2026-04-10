package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {

    @JsonProperty("route_id")
    private String routeId;

    @JsonProperty("agency_id")
    private Integer agencyId;

    @JsonProperty("route_short_name")
    private String routeShortName;

    @JsonProperty("route_long_name")
    private String routeLongName;

    @JsonProperty("route_type")
    private Integer routeType;

    @JsonProperty("route_color")
    private String routeColor;

    @JsonProperty("route_text_color")
    private String routeTextColor;
}
