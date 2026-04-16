package br.com.smartTrafficFlow.Smart_Traffic_Flow.controller;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.TrafficInsightsResponse;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.service.TrafficInsightsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/insights")
public class TrafficInsightsController {

    private final TrafficInsightsService insightsService;


    public TrafficInsightsController(TrafficInsightsService insightsService) {
        this.insightsService = insightsService;
    }

    @GetMapping
    public TrafficInsightsResponse insights(){
        return insightsService.gerarInsights();
    }
}
