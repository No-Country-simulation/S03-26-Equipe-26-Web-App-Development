package br.com.smartTrafficFlow.Smart_Traffic_Flow.entity;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.Climate;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.StatusTrafego;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TrafficAlert;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TypeOfRoute;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@Table(name = "traffic_data",
        uniqueConstraints = @UniqueConstraint(columnNames = {"idvia", "hora"}))
public class TrafficData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idvia")
    private Integer idvia;
    private String nome;

    @Enumerated(EnumType.STRING)
    private TypeOfRoute tipo;
    private String hora;
    @Enumerated(EnumType.STRING)
    private Climate clima;
    private int volume;
    private int capacidade;

    private double nivel;
    @Enumerated(EnumType.STRING)
    private StatusTrafego status;

    @Enumerated(EnumType.STRING)
    private TrafficAlert alerta;
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point geom;

}
