package br.com.smartTrafficFlow.Smart_Traffic_Flow.entity;


import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.VehicleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelo;
    private String placa;

    @Enumerated(EnumType.STRING)
    private VehicleType tipo; // carro, moto , caminhao

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

}
