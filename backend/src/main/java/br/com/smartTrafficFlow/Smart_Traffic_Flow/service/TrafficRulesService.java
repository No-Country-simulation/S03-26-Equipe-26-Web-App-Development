package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.TrafficData;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.StatusTrafego;
import org.springframework.stereotype.Service;

@Service
public class TrafficRulesService {
    public void aplicarRegras(TrafficData traffic){

        definirStatus(traffic);
        validarVolume(traffic);
    }

    private void definirStatus(TrafficData traffic){
       double ocupacao =
               (double) traffic.getVolume() / traffic.getCapacidade();
       if (ocupacao >= 0.8) {
           traffic.setStatus(StatusTrafego.CONGESTIONADO);
       } else if (ocupacao >= 0.4){
           traffic.setStatus(StatusTrafego.LENTO);
       } else {
           traffic.setStatus(StatusTrafego.FLUIDO);
       }
    }

    private void validarVolume(TrafficData traffic){
        if(traffic.getVolume() < 0){
            throw new RuntimeException("Volume inválido");
        }
    }
}
