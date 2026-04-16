package br.com.smartTrafficFlow.Smart_Traffic_Flow.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class DashboardDTO {
    private TrafficInsightsResponse insights;
    private List<TrafficResponse> trafficData;
    private String newsJson; // O JSON bruto retornado pelo Serper
    private LocalDateTime timestamp;

    public DashboardDTO(TrafficInsightsResponse insights, List<TrafficResponse> trafficData, String newsJson) {
        this.insights = insights;
        this.trafficData = trafficData;
        this.newsJson = newsJson;
        this.timestamp = LocalDateTime.now();
  }
}
