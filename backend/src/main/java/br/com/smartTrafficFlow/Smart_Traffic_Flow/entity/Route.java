package br.com.smartTrafficFlow.Smart_Traffic_Flow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.LineString;

@Entity
@Getter
@Setter
@Table(name = "route")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origem;
    private String destino;
    private Double distancia;
    private Double tempoEstimado;

    //linha geografica da rota
    @Column(columnDefinition = "geometry(LineString, 4326)")
    private LineString trajeto;

}
