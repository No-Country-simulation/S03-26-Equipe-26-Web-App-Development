package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.Climate;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.StatusTrafego;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TrafficAlert;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TypeOfRoute;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

@Schema(name = "TrafficCreateRequest", description = "Payload de entrada para criacao manual de um registro de trafego.")
public record TrafficCreateRequest(
        @Schema(description = "Identificador da via", example = "101")
        @NotNull(message = "idvia e obrigatorio")
        Integer idvia,
        @Schema(description = "Nome da via", example = "Av. Central")
        @NotBlank(message = "nome e obrigatorio")
        String nome,
        @Schema(description = "Tipo da via", example = "arterial")
        @NotNull(message = "tipo e obrigatorio")
        TypeOfRoute tipo,
        @Schema(description = "Data e hora da medicao", example = "2026-03-31T08:00:00")
        @NotNull(message = "hora e obrigatoria")
        LocalDateTime hora,
        @Schema(description = "Clima no momento da medicao", example = "chuva_leve")
        Climate clima,
        @Schema(description = "Volume de veiculos", example = "820")
        @NotNull(message = "volume e obrigatorio")
        @Positive(message = "volume deve ser maior que zero")
        Integer volume,
        @Schema(description = "Capacidade estimada da via", example = "1200")
        @NotNull(message = "capacidade e obrigatoria")
        @Positive(message = "capacidade deve ser maior que zero")
        Integer capacidade,
        @Schema(description = "Nivel de ocupacao da via", example = "68.3")
        @NotNull(message = "nivel e obrigatorio")
        @PositiveOrZero(message = "nivel deve ser maior ou igual a zero")
        Double nivel,
        @Schema(description = "Status do trafego", example = "LENTO")
        StatusTrafego status,
        @Schema(description = "Nivel de alerta operacional", example = "ATENCAO")
        @NotNull(message = "alerta e obrigatorio")
        TrafficAlert alerta,
        @Schema(description = "Latitude", example = "-23.5505")
        Double lat,
        @Schema(description = "Longitude", example = "-46.6333")
        Double lng
) {
}
