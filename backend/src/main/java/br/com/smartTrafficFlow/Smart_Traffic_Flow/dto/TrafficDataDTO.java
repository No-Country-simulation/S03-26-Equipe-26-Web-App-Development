package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.Climate;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.StatusTrafego;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TrafficAlert;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TypeOfRoute;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrafficDataDTO {

    private Integer idvia;
    private String nome;
    private TypeOfRoute tipo;
    private String hora;
    private Climate clima;
    private int volume;
    private int capacidade;
    private double nivel;
    private StatusTrafego status;
    private TrafficAlert alerta;

    private Double lat;
    private Double lng;
}
