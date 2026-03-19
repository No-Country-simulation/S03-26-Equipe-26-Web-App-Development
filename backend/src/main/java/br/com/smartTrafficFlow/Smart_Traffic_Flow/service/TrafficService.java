package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.TrafficData;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.repository.TrafficRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

@Service
public class TrafficService {

    private final TrafficRepository repository;

    public TrafficService(TrafficRepository repository) {
        this.repository = repository;
    }

    public void loadDataFromJson() {

        try {
            ObjectMapper mapper = new ObjectMapper();

            InputStream inputStream = getClass()
                    .getResourceAsStream("/traffic_data.json");

            List<TrafficData> dados = mapper.readValue(
                    inputStream,
                    new TypeReference<List<TrafficData>>() {
                    }
            );
            repository.saveAll(dados);
        } catch (Exception e){
            throw new RuntimeException("Erro ao carrgar JSON", e);
        }
    }

    public List<TrafficData> getAll(){
        return repository.findAll();
    }
}
