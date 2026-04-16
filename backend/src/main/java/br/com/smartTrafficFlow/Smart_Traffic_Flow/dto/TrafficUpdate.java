package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import java.util.List;

public record TrafficUpdate(
        double currentSpeed,
        double freeFlowSpeed,
        String incidentDescription,
        List<String> newsSnippets
) {
}
