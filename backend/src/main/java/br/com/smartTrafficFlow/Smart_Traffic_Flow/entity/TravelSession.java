package br.com.smartTrafficFlow.Smart_Traffic_Flow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "travel_session")
public class TravelSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime inciio;
    private LocalDateTime fim;

    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "rota_id")
    private Route route;

}
