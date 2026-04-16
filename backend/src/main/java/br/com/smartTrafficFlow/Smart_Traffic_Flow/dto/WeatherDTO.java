package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDTO {

    private double temperatura;
    private int umidade; // Ex: "Chuva" em vez de "Rain"
    private String condicao;
    private String descricao; // Ex: "chuva leve"
    private double fatorImpacto; // Um cálculo seu: 1.0 (sol) a 0.5 (caos no trânsito)
}
