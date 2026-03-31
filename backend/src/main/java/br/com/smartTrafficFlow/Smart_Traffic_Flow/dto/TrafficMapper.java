package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.TrafficData;

public class TrafficMapper {
    public static TrafficResponse toResponse(TrafficData data){

        return new TrafficResponse(
                data.getId(),
                data.getIdvia(),
                data.getNome(),
                data.getTipo(),
                data.getHora(),
                data.getClima(),
                data.getVolume(),
                data.getCapacidade(),
                data.getNivel(),
                data.getStatus(),
                data.getAlerta(),
                data.getLat(),
                data.getLng()

        );
    }
}
