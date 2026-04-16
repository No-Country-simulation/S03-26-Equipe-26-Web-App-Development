package br.com.smartTrafficFlow.Smart_Traffic_Flow.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Climate {
    LIMPO,
    CHUVA_LEVE,
    CHUVA_FORTE,
    NORMAL,        // ✓ Verificar se existe
    CHUVA,         // ou RAINY
    NUBLADO,       // ou CLOUDY
    SOL;

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }
}
