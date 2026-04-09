package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TelemetryService {

    //raio da terra em km para calculo de Haversine
    private static final double Earth_Radius_KM = 6371;

    public boolean detectarInconsistencia(double lat1, double lon1, LocalDateTime t1,
                                          double lat2, double lon2, LocalDateTime t2){

        double distancia = calcularDistancia(lat1, lon1, lat2, lon2);
        long segundos = Duration.between(t1, t2).getSeconds();

        if (segundos <= 0 )
            return true;

        double speedKmh = (distancia / segundos) * 3600;

        // se a velocidade superio a 200km/h em trecho urbano é um erro gps
        return speedKmh > 200.0;
    }

    public double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return Earth_Radius_KM * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }
}
