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

    public List<TrafficData> loadFromJson(){
        try {
            ObjectMapper mapper = new ObjectMapper();

            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("traffic_data.json");

            if (inputStream == null) {
                throw new RuntimeException("Arquivo Traffic_data.json não encontrado");
            }

            return mapper.readValue(inputStream, new TypeReference<List<TrafficData>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar JSON", e);
        }

    }

    public void loadData() {
        List<TrafficData> dados = loadFromJson();

        for (TrafficData d : dados){
            if (!repository.existsByIdviaAndHora(d.getIdvia(), d.getHora())){
                repository.save(d);
            }
        }
    }

    public List<TrafficData> getAll(){
        return repository.findAll();
    }

    public TrafficData save(TrafficData data){
        return repository.save(data);
    }

    public List<TrafficData> findByFilters(String cidade, String status){
        String c = (cidade == null) ? "" : cidade;
        String s = (status == null) ? "" : status;

        return repository.findByNomeContainingIgnoreCaseAndStatusContainingIgnoreCase(c, s);
    }
}
