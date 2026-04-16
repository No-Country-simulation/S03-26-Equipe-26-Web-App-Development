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
@Table(
        name = "traffic_data",
        uniqueConstraints = @UniqueConstraint(columnNames = {"idvia", "hora"})
)
@Schema(name = "TrafficData", description = "Entidade de tráfego persistida no backend.")
public class TrafficData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID interno", example = "1")
    private Long id;

    @Column(name = "idvia")
    private Integer idvia;

    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeOfRoute tipo;

    private LocalDateTime hora;

    @Enumerated(EnumType.STRING)
    private Climate clima;

    private Integer volume;

    private Integer capacidade;

    private Double nivel;

    @Enumerated(EnumType.STRING)
    private StatusTrafego status;

    @Enumerated(EnumType.STRING)
    private TrafficAlert alerta;

    /**
     * Campo espacial PostGIS
     */
    @Column(columnDefinition = "geometry(Point,4326)")
    @JsonIgnore
    private Point geom;

    // ==============================
    // LAT / LNG PARA FRONTEND
    // ==============================

    @JsonIgnore
    public Double getLat() {
        return geom != null ? geom.getY() : null;
    }

    @JsonIgnore
    public Double getLng() {
        return geom != null ? geom.getX() : null;
    }
}