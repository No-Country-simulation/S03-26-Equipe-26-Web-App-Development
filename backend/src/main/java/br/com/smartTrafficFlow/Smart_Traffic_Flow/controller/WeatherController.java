package br.com.smartTrafficFlow.Smart_Traffic_Flow.controller;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.WeatherDTO;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.WeatherResponse;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/clima")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public WeatherResponse testarClima(@RequestParam double lat,
                                       @RequestParam double lon){
        return weatherService.getWeather(lat, lon);
    }

    @GetMapping("/atual")
    public ResponseEntity<WeatherDTO> obterClima(@RequestParam double lat, @RequestParam double lon){
        WeatherDTO dto = weatherService.getClimaAmigavel(lat, lon);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }
}
