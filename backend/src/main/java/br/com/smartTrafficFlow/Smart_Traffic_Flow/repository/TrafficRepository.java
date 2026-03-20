package br.com.smartTrafficFlow.Smart_Traffic_Flow.repository;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.TrafficData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrafficRepository extends JpaRepository<TrafficData, Long> {

    boolean existsByIdviaAndHora(Integer idVia, String hora);

    List<TrafficData> findByNomeContainingIgnoreCaseAndStatusContainingIgnoreCase(String nome, String status);

    List<TrafficData> findByClimaAndNivelGreaterThan(String clima, Double nivel);

    List<TrafficData> findByClima(String clima);

    List<TrafficData> findByNivelGreaterThan(Double nivel);

    List<TrafficData> findByAlerta(String alerta);
}
