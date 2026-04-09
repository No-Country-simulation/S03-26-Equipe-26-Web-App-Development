package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

public record TomTomResponse(FlowSegmentData flowSegmentData) {
    public record FlowSegmentData(
            int currentSpeed,
            int freeFlowSpeed,
            int confidence,
            String roadClosure
    ) {}
}
