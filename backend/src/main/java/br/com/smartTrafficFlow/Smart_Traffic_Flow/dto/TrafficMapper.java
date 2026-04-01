package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.TrafficData;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

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

    public static TrafficData toEntity(TrafficCreateRequest request, GeometryFactory geometryFactory) {
        TrafficData entity = new TrafficData();
        entity.setIdvia(request.idvia());
        entity.setNome(request.nome());
        entity.setTipo(request.tipo());
        entity.setHora(request.hora());
        entity.setClima(request.clima());
        entity.setVolume(request.volume());
        entity.setCapacidade(request.capacidade());
        entity.setNivel(request.nivel());
        entity.setStatus(request.status());
        entity.setAlerta(request.alerta());

        if (request.lat() != null && request.lng() != null) {
            Point point = geometryFactory.createPoint(new Coordinate(request.lng(), request.lat()));
            point.setSRID(4326);
            entity.setGeom(point);
        }

        return entity;
    }
}
