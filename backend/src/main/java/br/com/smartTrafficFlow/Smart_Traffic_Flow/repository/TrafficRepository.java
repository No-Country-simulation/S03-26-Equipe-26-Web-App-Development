package br.com.smartTrafficFlow.Smart_Traffic_Flow.repository;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.entity.TrafficData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrafficRepository extends JpaRepository<TrafficData, Long> {

    boolean existsByIdviaAndHora(Integer idVia, String hora);
}
