package br.com.smartTrafficFlow.Smart_Traffic_Flow.controller;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.TrafficData;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.service.TrafficService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/traffic")
@CrossOrigin
public class TrafficController {

    private final TrafficService service;

    public TrafficController(TrafficService service) {
        this.service = service;
    }

    @PostMapping("/load")
    public String loadData() {
        service .loadData();
        return "Dados carregados com sucesso!";
    }

    @GetMapping
    public List<TrafficData> getAll(){
        return service.getAll();
    }
}
