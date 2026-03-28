package br.com.smartTrafficFlow.Smart_Traffic_Flow.repository;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.TrafficData;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.enums.Climate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TrafficRepository extends JpaRepository<TrafficData, Long> {

    boolean existsByIdviaAndHora(Integer idVia, LocalDateTime hora);

    List<TrafficData> findByClimaAndNivelGreaterThan(Climate clima, Double nivel);

    List<TrafficData> findByClima(Climate clima);

    List<TrafficData> findByNivelGreaterThan(Double nivel);

    List<TrafficData> findByAlerta(String alerta);
}
