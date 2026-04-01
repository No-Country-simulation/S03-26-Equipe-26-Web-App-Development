package br.com.smartTrafficFlow.Smart_Traffic_Flow.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "openweather.api.key=test-key",
        "openweather.api.url=https://api.openweathermap.org/data/2.5/weather"
})
class TrafficControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnTrafficList() throws Exception {
        mockMvc.perform(get("/traffic"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"idvia\":1")))
                .andExpect(content().string(containsString("\"nome\":\"Av. Central\"")))
                .andExpect(content().string(containsString("\"tipo\":\"arterial\"")))
                .andExpect(content().string(containsString("\"lat\":-23.5505")))
                .andExpect(content().string(containsString("\"lng\":-46.6333")))
                .andExpect(content().string(not(containsString("\"envelope\""))));
    }

    @Test
    void shouldFilterTrafficByLevel() throws Exception {
        mockMvc.perform(get("/traffic/filter").param("nivel", "40"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"nivel\":45.0")))
                .andExpect(content().string(not(containsString("\"nivel\":20.2"))));
    }

    @Test
    void shouldFilterTrafficByAlert() throws Exception {
        mockMvc.perform(get("/traffic/filter").param("alerta", "ANOMALIA"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"alerta\":\"ANOMALIA\"")))
                .andExpect(content().string(not(containsString("\"alerta\":\"NORMAL\""))));
    }

    @Test
    void shouldReturnTrafficInsights() throws Exception {
        mockMvc.perform(get("/traffic/insights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRegistros", greaterThan(0)))
                .andExpect(jsonPath("$.horarioPico", not(emptyOrNullString())))
                .andExpect(jsonPath("$.volumeHorarioPico", greaterThan(0)))
                .andExpect(jsonPath("$.viaMaisMovimentada", not(emptyOrNullString())))
                .andExpect(jsonPath("$.mediaVolumeViaMaisMovimentada", greaterThanOrEqualTo(0.0)));
    }

    @Test
    void shouldCreateTrafficUsingRequestDto() throws Exception {
        String payload = """
                {
                  "idvia": 999,
                  "nome": "Via de Teste",
                  "tipo": "arterial",
                  "hora": "2026-03-31T08:00:00",
                  "clima": "limpo",
                  "volume": 250,
                  "capacidade": 1000,
                  "nivel": 25.0,
                  "status": "FLUIDO",
                  "alerta": "NORMAL",
                  "lat": -23.5505,
                  "lng": -46.6333
                }
                """;

        mockMvc.perform(post("/traffic")
                        .contentType("application/json")
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("\"idvia\":999")))
                .andExpect(content().string(containsString("\"nome\":\"Via de Teste\"")))
                .andExpect(content().string(containsString("\"lat\":-23.5505")))
                .andExpect(content().string(containsString("\"lng\":-46.6333")));
    }

    @Test
    void shouldRejectInvalidTrafficPayload() throws Exception {
        String payload = """
                {
                  "nome": "",
                  "tipo": "arterial",
                  "hora": "2026-03-31T08:00:00",
                  "volume": 0,
                  "capacidade": 1000,
                  "nivel": 25.0,
                  "alerta": "NORMAL"
                }
                """;

        mockMvc.perform(post("/traffic")
                        .contentType("application/json")
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldExposeOpenApiDocument() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"title\":\"Smart Traffic Flow API\"")))
                .andExpect(content().string(containsString("\"/traffic\"")))
                .andExpect(content().string(containsString("\"/traffic/insights\"")));
    }

    @Test
    void shouldExposeSwaggerUi() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }
}
