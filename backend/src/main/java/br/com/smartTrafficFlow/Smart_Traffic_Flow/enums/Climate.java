package br.com.smartTrafficFlow.Smart_Traffic_Flow.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Climate {
    LIMPO,
    CHUVA_LEVE,
    CHUVA_FORTE;

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }
}
