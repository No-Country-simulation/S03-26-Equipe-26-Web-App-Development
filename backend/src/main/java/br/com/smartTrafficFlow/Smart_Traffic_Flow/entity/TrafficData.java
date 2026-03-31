package br.com.smartTrafficFlow.Smart_Traffic_Flow.entity;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.Climate;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.StatusTrafego;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TrafficAlert;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TypeOfRoute;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "traffic_data",
        uniqueConstraints = @UniqueConstraint(columnNames = {"idvia", "hora"}))
@Schema(name = "TrafficData", description = "Entidade de trafego persistida no backend.")
public class TrafficData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador interno do registro", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "idvia")
    @Schema(description = "Identificador da via", example = "101")
    private Integer idvia;

    @Schema(description = "Nome amigavel da via", example = "Av. Central")
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Tipo da via", example = "arterial")
    private TypeOfRoute tipo;

    @Schema(description = "Data e hora da medicao", example = "2026-03-30T08:00:00")
    private LocalDateTime hora;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Condicao climatica", example = "chuva_leve")
    private Climate clima;

    @Schema(description = "Volume medido de veiculos", example = "820")
    private int volume;

    @Schema(description = "Capacidade estimada da via", example = "1200")
    private int capacidade;

    @Schema(description = "Nivel de ocupacao da via", example = "68.3")
    private double nivel;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Status do trafego", example = "LENTO")
    private StatusTrafego status;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Nivel de alerta operacional", example = "ATENCAO")
    private TrafficAlert alerta;

    @Column(columnDefinition = "geometry(Point, 4326)")
    @Schema(description = "Geometria espacial interna usada pelo backend", hidden = true)
    private Point geom;

    @JsonIgnore
    public Double getLat() {
        if (geom == null) return null;
        return geom.getY();
    }
    @JsonIgnore
    public Double getLng() {
        if (geom == null) return null;
        return geom.getX();
    }
}
