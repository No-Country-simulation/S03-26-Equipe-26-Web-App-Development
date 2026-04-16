package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.Climate;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.StatusTrafego;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TrafficAlert;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TypeOfRoute;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record TrafficCreateDTO(
        @JsonProperty("idvia") Integer idvia,
        @JsonProperty("nome") String nome,
        @JsonProperty("tipo") TypeOfRoute tipo,
        @JsonProperty("hora") LocalDateTime hora,
        @JsonProperty("clima") Climate clima,
        @JsonProperty("volume") Integer volume,
        @JsonProperty("capacidade") Integer capacidade,
        @JsonProperty("nivel") Double nivel,
        @JsonProperty("status") StatusTrafego status,
        @JsonProperty("alerta") TrafficAlert alerta,
        @JsonProperty("lat") Double lat,
        @JsonProperty("lng") Double lng
) {}
