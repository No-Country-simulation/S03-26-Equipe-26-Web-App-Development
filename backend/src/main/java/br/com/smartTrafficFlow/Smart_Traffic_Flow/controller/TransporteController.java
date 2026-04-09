package br.com.smartTrafficFlow.Smart_Traffic_Flow.controller;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.RouteDTO;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.StopDTO;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.service.TransporteIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Operation(summary = "Lista todas as cidades disponíveis")
    public ResponseEntity<List<String>> getCidades() {
        List<String> cidades = transporteService.getCidades();
        return ResponseEntity.ok(cidades);
    }

    @GetMapping("/stops/{cidade}")
    @Operation(summary = "Busca paradas de uma cidade")
    public ResponseEntity<List<StopDTO>> getStops(
            @PathVariable String cidade,
            @RequestParam(defaultValue = "100") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset) {

        List<StopDTO> stops = transporteService.getStops(cidade, limit, offset);
        if (stops == null || stops.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(stops);
    }

    @GetMapping("/routes/{cidade}")
    @Operation(summary = "Busca rotas/linhas de uma cidade")
    public ResponseEntity<List<RouteDTO>> getRoutes(
            @PathVariable String cidade,
            @RequestParam(defaultValue = "50") Integer limit) {

        List<RouteDTO> routes = transporteService.getRoutes(cidade, limit);
        if (routes == null || routes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/stops/{cidade}/nearby")
    @Operation(summary = "Busca paradas próximas a uma localização")
    public ResponseEntity<List<StopDTO>> getStopsNearby(
            @PathVariable String cidade,
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "1.0") double radius) {

        List<StopDTO> stops = transporteService.getStopsNearby(cidade, lat, lon, radius);
        return ResponseEntity.ok(stops);
    }

}
