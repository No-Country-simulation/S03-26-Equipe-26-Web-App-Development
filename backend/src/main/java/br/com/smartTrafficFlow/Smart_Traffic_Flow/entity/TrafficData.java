package br.com.smartTrafficFlow.Smart_Traffic_Flow.entity;

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
    private String tipo;
    private String hora;
    private String clima;
    private int volume;
    private int capacidade;

    private double nivel;
    private String status;
    private String alerta;
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point geom;

}
