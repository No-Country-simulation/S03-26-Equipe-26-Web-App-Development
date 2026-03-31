package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.TrafficDataDTO;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.TrafficInsightsResponse;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.TrafficResponse;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.TrafficData;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.Climate;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.TrafficAlert;
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

        return repository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public TrafficData save(TrafficData data){
        return repository.save(data);
    }

    public List<TrafficData> findByFilters(Climate clima, Double nivel, String alerta){

        if (alerta != null && !alerta.trim().isEmpty()) {
            try {
                TrafficAlert alertaEnum = TrafficAlert.valueOf(alerta.toUpperCase());
                return repository.findByAlerta(alertaEnum);
            } catch (IllegalArgumentException e) {
                System.err.println("Aviso: Alerta '" + alerta + "' não encontrado no Enum TrafficAlert.");
                // Se o alerta for inválido, podemos optar por ignorar o filtro de alerta e seguir para os outros
            }
        }

        if (clima != null && nivel != null) {
            return repository.findByClimaAndNivelGreaterThan(clima, nivel);
        }

        if (clima != null) {
            return repository.findByClima(clima);
        }

        if (nivel != null) {
            return repository.findByNivelGreaterThan(nivel);
        }

        return repository.findAll();
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

    private TrafficResponse convertToResponse(TrafficData data) {

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
