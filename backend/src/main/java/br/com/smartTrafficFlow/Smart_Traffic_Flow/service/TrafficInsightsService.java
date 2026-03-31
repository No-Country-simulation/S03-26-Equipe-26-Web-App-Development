package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.TrafficInsightsResponse;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.TrafficData;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.repository.TrafficRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TrafficInsightsService {

    private final TrafficRepository repository;

    public TrafficInsightsService(TrafficRepository repository){
        this.repository = repository;
    }

    public TrafficInsightsResponse gerarInsights(){

        List<TrafficData> dados = repository.findAll();

        if(dados.isEmpty()){
            return new TrafficInsightsResponse(
                    0,
                    "N/D",
                    0,
                    "N/D",
                    0.0
            );
        }

        // ✅ Volume por horário
        Map<LocalDateTime, Integer> volumePorHora =
                dados.stream()
                        .collect(Collectors.groupingBy(
                                TrafficData::getHora,
                                Collectors.summingInt(TrafficData::getVolume)
                        ));

        // ✅ Média por via
        Map<String, Double> mediaPorVia =
                dados.stream()
                        .collect(Collectors.groupingBy(
                                TrafficData::getNome,
                                Collectors.averagingInt(TrafficData::getVolume)
                        ));

        // ✅ Horário de pico
        var horarioPico =
                volumePorHora.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .orElse(null);

        // ✅ Via mais movimentada
        var viaMaisMovimentada =
                mediaPorVia.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .orElse(null);

        return new TrafficInsightsResponse(
                dados.size(),
                horarioPico.getKey().toString(),
                horarioPico.getValue(),
                viaMaisMovimentada.getKey(),
                viaMaisMovimentada.getValue()
        );
    }
}
