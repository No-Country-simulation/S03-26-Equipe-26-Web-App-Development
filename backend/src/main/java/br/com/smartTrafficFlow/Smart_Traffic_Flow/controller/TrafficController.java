package br.com.smartTrafficFlow.Smart_Traffic_Flow.controller;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.TrafficAggregationsResponse;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.TrafficInsightsResponse;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.TrafficResponse;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.TrafficData;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.Climate;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.service.SPTransService;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.service.TrafficService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/traffic")
@Tag(name = "Traffic", description = "Operacoes para consulta, carga e analise dos dados de trafego.")
public class TrafficController {


    private final TrafficService service;

    @Autowired
    private SPTransService spTransService;

    public TrafficController(TrafficService service) {
        this.service = service;
    }
    @PostMapping("/load")
    @Operation(
            summary = "Carrega dados de trafego do arquivo JSON",
            description = "Importa a massa de dados de traffic_data.json para a base em memoria."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carga executada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Falha ao carregar o arquivo de dados")
    })
    public String loadData() {
        service .loadData();
        return "Dados carregados com sucesso!";
    }

    @GetMapping
    @Operation(
            summary = "Lista todos os registros de trafego",
            description = "Retorna os registros persistidos, com latitude e longitude prontas para consumo no frontend."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de registros retornada com sucesso",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrafficResponse.class)))
    )
    public List<TrafficResponse> getAll(){
        return service.getAll();
    }

    @PostMapping
    @Operation(
            summary = "Cria um novo registro de trafego",
            description = "Persiste um registro manual na base em memoria."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro criado com sucesso",
                    content = @Content(schema = @Schema(implementation = TrafficData.class))),
            @ApiResponse(responseCode = "400", description = "Payload invalido")
    })
    public ResponseEntity<TrafficData> CreateTraffic(@RequestBody TrafficData data){
        TrafficData salvo = service.save(data);
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    @GetMapping("/filter")
    @Operation(
            summary = "Filtra registros de trafego",
            description = "Filtra por clima, nivel minimo de ocupacao ou alerta. Quando nenhum filtro e informado, retorna todos os registros."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrafficResponse.class)))),
            @ApiResponse(responseCode = "400", description = "Parametro de filtro invalido")
    })
    public List<TrafficResponse> filterTraffic(
            @Parameter(description = "Clima a ser filtrado", example = "chuva_leve")
            @RequestParam(required = false) Climate clima,
            @Parameter(description = "Nivel minimo de ocupacao da via", example = "40")
            @RequestParam(required = false) Double nivel,
            @Parameter(description = "Alerta de trafego", example = "ANOMALIA")
            @RequestParam(required = false) String alerta) {

        return service.findByFilters(clima, nivel, alerta);
    }

    @GetMapping("/insights")
    @Operation(
            summary = "Retorna insights consolidados do trafego",
            description = "Calcula total de registros, horario de pico e a via com maior volume medio."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Insights retornados com sucesso",
            content = @Content(schema = @Schema(implementation = TrafficInsightsResponse.class))
    )
    public TrafficInsightsResponse getInsights() {
        return service.getInsights();
    }

    @GetMapping("/teste-sptrans")
    public String testarConexão(){
        try{
            boolean logado = spTransService.authenticate();
            if (logado) {
                return " Aplicação conectada à SPTrans com sucesso!";
            } else {
                return "Falha na autenticação. Verifique o Token.";
            }
        } catch (Exception e){
            return " Erro técnico: " + e.getMessage();
        }
    }

    @GetMapping("/aggregations")
    @Operation(summary = "Dados agragados de tráfego para dashgoards")
    public TrafficAggregationsResponse getAggregations() {
        return service.getAggregations();
    }

}
