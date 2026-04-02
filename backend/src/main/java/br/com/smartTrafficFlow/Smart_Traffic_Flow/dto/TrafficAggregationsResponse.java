package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import java.util.List;

public record TrafficAggregationsResponse(
        List<VolumePorHorarioDTO> volumePorHorario,
        List<TipoViaDTO> volumePorTipoVia
) {
}
