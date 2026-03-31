package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.Climate;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.StatusTrafego;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TrafficAlert;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TypeOfRoute;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "TrafficResponse", description = "Payload de resposta com dados de trafego e coordenadas prontas para o frontend.")
public record TrafficResponse(
        @Schema(description = "Identificador interno do registro", example = "1")
        Long id,
        @Schema(description = "Identificador da via", example = "101")
        Integer idvia,
        @Schema(description = "Nome da via", example = "Av. Central")
        String nome,
        @Schema(description = "Tipo da via", example = "arterial")
        TypeOfRoute tipo,
        @Schema(description = "Data e hora da medicao", example = "2026-03-30T08:00:00")
        LocalDateTime hora,
        @Schema(description = "Clima no momento da medicao", example = "chuva_leve")
        Climate clima,
        @Schema(description = "Volume de veiculos", example = "820")
        int volume,
        @Schema(description = "Capacidade estimada da via", example = "1200")
        int capacidade,
        @Schema(description = "Nivel de ocupacao da via", example = "68.3")
        double nivel,
        @Schema(description = "Status do trafego", example = "LENTO")
        StatusTrafego status,
        @Schema(description = "Nivel de alerta operacional", example = "ATENCAO")
        TrafficAlert alerta,
        @Schema(description = "Latitude", example = "-23.5505")
        Double lat,
        @Schema(description = "Longitude", example = "-46.6333")
        Double lng
) {
}
