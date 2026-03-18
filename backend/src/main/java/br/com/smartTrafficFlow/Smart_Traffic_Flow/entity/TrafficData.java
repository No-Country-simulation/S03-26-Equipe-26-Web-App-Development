package br.com.smartTrafficFlow.Smart_Traffic_Flow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TrafficData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int idvia;
    private String nome;
    private String tipo;
    private String hora;
    private int volume;
    private int capacidade;
    private double nivelCongestionamento;
    private String status;
    private String aleta;
}
