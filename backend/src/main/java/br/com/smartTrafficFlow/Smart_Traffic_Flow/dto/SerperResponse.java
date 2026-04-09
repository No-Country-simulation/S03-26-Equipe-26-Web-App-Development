package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import java.util.List;

public record SerperResponse(List<OrganicResult> organic) {
    public record OrganicResult(
            String title,
            String link,
            String snippet,
            String date
    ) {}
}
