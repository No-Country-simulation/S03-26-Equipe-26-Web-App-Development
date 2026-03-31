package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TrafficInsightsResponse", description = "Resumo analitico do comportamento do trafego.")
public record TrafficInsightsResponse(
        @Schema(description = "Quantidade total de registros analisados", example = "120")
        long totalRegistros,
        @Schema(description = "Horario com maior volume agregado", example = "2026-03-30T08:00")
        String horarioPico,
        @Schema(description = "Volume agregado no horario de pico", example = "6400")
        Integer volumeHorarioPico,
        @Schema(description = "Via com maior media de volume", example = "Av. Central")
        String viaMaisMovimentada,
        @Schema(description = "Media de volume da via mais movimentada", example = "812.5")
        Double mediaVolumeViaMaisMovimentada
) {
}
