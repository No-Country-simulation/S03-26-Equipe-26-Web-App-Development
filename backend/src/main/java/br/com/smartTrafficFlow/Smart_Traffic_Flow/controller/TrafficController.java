package br.com.smartTrafficFlow.Smart_Traffic_Flow.controller;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.TrafficData;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.service.TrafficService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/traffic")
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

    @PostMapping
    public ResponseEntity<TrafficData> CreateTraffic(@RequestBody TrafficData data){
        TrafficData salvo = service.save(data);
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    @GetMapping("/filter")
    public List<TrafficData> filterTraffic(
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String status) {

        return service.findByFilters(cidade, status);
    }

}
