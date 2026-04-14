package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.HeatmapPointDTO;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.Bus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {

    private final SPTransService spTransService;
    private final TomTomService tomTomService;

    public AnalyticsService(
            SPTransService spTransService,
            TomTomService tomTomService
    ) {
        this.spTransService = spTransService;
        this.tomTomService = tomTomService;
    }

    public List<HeatmapPointDTO> getCrowdFlowData() {

        List<HeatmapPointDTO> heatmap = new ArrayList<>();

        List<Bus> buses = spTransService.getBusPositions();

        for (Bus bus : buses) {

            heatmap.add(
                    new HeatmapPointDTO(
                            bus.getLatitude(),
                            bus.getLongitude(),
                            1.0
                    )
            );
        }

        return heatmap;
    }
}