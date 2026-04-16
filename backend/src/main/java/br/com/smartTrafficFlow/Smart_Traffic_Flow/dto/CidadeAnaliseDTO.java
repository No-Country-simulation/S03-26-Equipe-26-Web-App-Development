package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CidadeAnaliseDTO {
    private String cidade;
    private String caminhoDados;
    private Integer totalParadas;
    private Integer paradasComCoordenadas;
    private Integer totalRotas;
    private Integer totalViagens;
    private Integer totalHorarios;
    private Map<String, Integer> tiposRota;
    private String status;

}
