import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.TipoViaDTO;
import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.VolumePorHorarioDTO;

import java.util.List;

public record TrafficAggregationsResponse(
        List<VolumePorHorarioDTO> volumePorHorario,
        List<TipoViaDTO> volumePorTipoVia
) {
}
