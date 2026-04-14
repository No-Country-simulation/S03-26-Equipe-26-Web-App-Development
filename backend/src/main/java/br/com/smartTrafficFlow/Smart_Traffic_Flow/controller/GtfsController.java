package br.com.smartTrafficFlow.Smart_Traffic_Flow.controller;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.service.GtfsClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gtfs")
public class GtfsController {
    @Autowired
    private GtfsClientService service;

    @GetMapping("/cidades")
    public Object cidades(){
        return service.getCidades();
    }

    @GetMapping("/stops/{cidade}")
    public Object stops(@PathVariable String cidade){
        return service.getStops(cidade);
    }

    @GetMapping("/routes/{cidade}")
    public Object routes(@PathVariable String cidade){
        return service.getRoutes(cidade);
    }

    @GetMapping("/heatmap/{cidade}")
    public Object heatmap(@PathVariable String cidade){
        return service.getHeatmap(cidade);
    }
}
