package br.com.smartTrafficFlow.Smart_Traffic_Flow.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class TrafficAggregationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveRetornarAggregations() throws Exception {
        mockMvc.perform(get("/traffic/aggregations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.volumePorHorario").exists())
                .andExpect(jsonPath("$.volumePorTipoVia").exists());
    }
}
