package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.Climate;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.StatusTrafego;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TrafficAlert;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TypeOfRoute;

import java.time.LocalDateTime;

public record TrafficResponse(
        Long id,
        Integer idvia,
        String nome,
        TypeOfRoute tipo,
        LocalDateTime hora,
        Climate clima,
        int volume,
        int capacidade,
        double nivel,
        StatusTrafego status,
        TrafficAlert alerta,
        Double lat,
        Double lng
) {
}
