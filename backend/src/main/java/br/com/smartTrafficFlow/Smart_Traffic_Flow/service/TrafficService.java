package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.TrafficDataDTO;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.TrafficInsightsResponse;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.TrafficResponse;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.TrafficData;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.Climate;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TrafficAlert;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.exception.BadRequestException;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.repository.TrafficRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TrafficService {
    private final TrafficRepository repository;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    public TrafficService(TrafficRepository repository) {
        this.repository = repository;
    }

    public List<TrafficDataDTO> loadFromJson(){
        try {
            ObjectMapper mapper = new ObjectMapper();

            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("traffic_data.json");

            if (inputStream == null) {
                throw new RuntimeException("Arquivo não encontrado");
            }

            return mapper.readValue(inputStream, new TypeReference<List<TrafficDataDTO>>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar JSON", e);
        }

    }

    public void loadData() {
        List<TrafficDataDTO> dados = loadFromJson();

        for (TrafficDataDTO dto : dados){
            if (!repository.existsByIdviaAndHora(dto.getIdvia(), dto.getHora())){
                TrafficData entity = new TrafficData();

                //mapear os dados
                entity.setIdvia(dto.getIdvia());
                entity.setNome(dto.getNome());
                entity.setTipo(dto.getTipo());
                entity.setHora(dto.getHora());
                entity.setClima(dto.getClima());
                entity.setVolume(dto.getVolume());
                entity.setCapacidade(dto.getCapacidade());
                entity.setNivel(dto.getNivel());
                entity.setStatus(dto.getStatus());
                entity.setAlerta(dto.getAlerta());

                //converter lat/lng -> Point (Geom)
                if (dto.getLat() != null && dto.getLng() != null){
                    Point point = geometryFactory.createPoint(
                            new Coordinate(dto.getLng(), dto.getLat())
                    );
                    entity.setGeom(point);
                }
                repository.save(entity);
            }
        }
    }

    public List<TrafficResponse> getAll(){
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public TrafficData save(TrafficData data){
        return repository.save(data);
    }

    public List<TrafficResponse> findByFilters(Climate clima, Double nivel, String alerta){
        TrafficAlert parsedAlert = parseAlert(alerta);
        List<TrafficData> result;

        if (clima != null && nivel != null) {
            result = repository.findByClimaAndNivelGreaterThan(clima, nivel);
        } else if (clima != null) {
            result = repository.findByClima(clima);
        } else if (nivel != null) {
            result = repository.findByNivelGreaterThan(nivel);
        } else if (parsedAlert != null) {
            result = repository.findByAlerta(parsedAlert);
        } else {
            result = repository.findAll();
        }

        return result.stream()
                .map(this::toResponse)
                .toList();
    }

    public TrafficInsightsResponse getInsights() {
        List<TrafficData> trafficData = repository.findAll();
        if (trafficData.isEmpty()) {
            return new TrafficInsightsResponse(0, "N/D", 0, "N/D", 0.0);
        }

        Map< LocalDateTime, Integer> volumePorHora =
                trafficData.stream()
                        .collect(Collectors.groupingBy(
                                TrafficData::getHora,
                                Collectors.summingInt(TrafficData::getVolume)
                        ));

        Map<String, Double> mediaPorVia = trafficData.stream()
                .collect(Collectors.groupingBy(TrafficData::getNome, Collectors.averagingInt(TrafficData::getVolume)));

        Map.Entry<LocalDateTime, Integer> horarioPico =
                volumePorHora.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .orElse(Map.entry(LocalDateTime.MIN, 0));


        Map.Entry<String, Double> viaMaisMovimentada = mediaPorVia.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(Map.entry("N/D", 0.0));

        return new TrafficInsightsResponse(
                trafficData.size(),
                horarioPico.getKey().toString(),
                horarioPico.getValue(),
                viaMaisMovimentada.getKey(),
                viaMaisMovimentada.getValue()
        );
    }

    private TrafficAlert parseAlert(String alerta) {
        if (alerta == null || alerta.isBlank()) {
            return null;
        }

        try {
            return TrafficAlert.valueOf(alerta.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Parametro alerta invalido");
        }
    }

    private TrafficResponse toResponse(TrafficData entity) {
        Double lat = entity.getGeom() == null ? null : entity.getGeom().getY();
        Double lng = entity.getGeom() == null ? null : entity.getGeom().getX();

        return new TrafficResponse(
                entity.getId(),
                entity.getIdvia(),
                entity.getNome(),
                entity.getTipo(),
                entity.getHora(),
                entity.getClima(),
                entity.getVolume(),
                entity.getCapacidade(),
                entity.getNivel(),
                entity.getStatus(),
                entity.getAlerta(),
                lat,
                lng
        );
    }
}
