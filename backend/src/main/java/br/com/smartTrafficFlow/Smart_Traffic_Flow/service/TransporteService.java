package br.com.smartTrafficFlow.Smart_Traffic_Flow.service;

import br.com.smartTrafficFlow.Smart_Traffic_Flow.dto.ParadaDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransporteService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String PYTHON_SERVICE_URL = "http://127.0.0.1:8000";

    public ParadaDTO[] buscarParadas(String cidade) {
        String url = PYTHON_SERVICE_URL + "/paradas/" + cidade;
        return restTemplate.getForObject(url, ParadaDTO[].class);
    }

    public Object buscarRotasPorLocal(String cidade, String local) {
        String url = "http://127.0.0.1:8000/buscar_rota" + cidade + "/"+ local;
        return restTemplate.getForObject(url,Object.class);
    }
}
