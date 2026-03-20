package br.com.smartTrafficFlow.Smart_Traffic_Flow.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    private int volume;
    private int capacidade;

    @Column(name = "nivel_congestionamento")
    private double nivelCongestionamento;
    private String status;
    private String alerta;
}
