package br.com.smartTrafficFlow.Smart_Traffic_Flow.controller;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.RouteDTO;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.RouteSearchRequest;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.StopDTO;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.service.TransporteIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/transporte")
@Tag(name = "Transporte", description = "Endpoints para consulta de dados de transporte público")
public class TransporteController {

    private final TransporteIntegrationService transporteService;

    public TransporteController(TransporteIntegrationService transporteService) {
        this.transporteService = transporteService;
    }

    @GetMapping("/cidades")
    public ResponseEntity<List<String>> getCidades() {
        List<String> cidades = transporteService.getCidades();
        return ResponseEntity.ok(cidades);
    }

    @GetMapping("/stops/{cidade}")
    public ResponseEntity<List<StopDTO>> getStops(
            @PathVariable String cidade,
            @RequestParam(defaultValue = "100") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset) {
        return ResponseEntity.ok(transporteService.getStops(cidade, limit, offset));
    }

    @GetMapping("/routes/{cidade}")
    public ResponseEntity<List<RouteDTO>> getRoutes(
            @PathVariable String cidade,
            @RequestParam(defaultValue = "50") Integer limit) {
        return ResponseEntity.ok(transporteService.getRoutes(cidade, limit));
    }

    @GetMapping("/stops/{cidade}/nearby")
    public ResponseEntity<List<StopDTO>> getStopsNearby(
            @PathVariable String cidade,
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "1.0") double radius) {
        // Correção: Passando os parâmetros na ordem correta para o Service
        List<StopDTO> stops = transporteService.getStopsNearby(cidade, lat, lon, radius);
        return ResponseEntity.ok(stops);
    }

    @PostMapping("/calculate-route")
    public ResponseEntity<?> calculateRoute(@RequestBody RouteSearchRequest request) {
        // Acesso correto para RECORD: request.origin() e request.destination()
        System.out.println("Origem: " + request.origin() + " Destino: " + request.destination());

        Object rota = transporteService.calculateBestRoute(request);
        return rota != null ? ResponseEntity.ok(rota) : ResponseEntity.badRequest().build();
    }
}

