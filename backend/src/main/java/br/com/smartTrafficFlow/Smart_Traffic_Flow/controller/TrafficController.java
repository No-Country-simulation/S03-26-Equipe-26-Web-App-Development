package br.com.smartTrafficFlow.Smart_Traffic_Flow.controller;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.TrafficInsightsResponse;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.TrafficResponse;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.TrafficData;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.Climate;
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
    public List<TrafficResponse> getAll(){
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<TrafficData> CreateTraffic(@RequestBody TrafficData data){
        TrafficData salvo = service.save(data);
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    @GetMapping("/filter")
    public List<TrafficResponse> filterTraffic(
            @RequestParam(required = false) Climate clima,
            @RequestParam(required = false) Double nivel,
            @RequestParam(required = false) String alerta) {

        return service.findByFilters(clima, nivel, alerta);
    }

    @GetMapping("/insights")
    public TrafficInsightsResponse getInsights() {
        return service.getInsights();
    }

}
