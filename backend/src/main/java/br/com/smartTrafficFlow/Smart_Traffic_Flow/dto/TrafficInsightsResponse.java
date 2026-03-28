package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

public record TrafficInsightsResponse(
        long totalRegistros,
        String horarioPico,
        Integer volumeHorarioPico,
        String viaMaisMovimentada,
        Double mediaVolumeViaMaisMovimentada
) {
}
