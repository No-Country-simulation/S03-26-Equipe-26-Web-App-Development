package br.com.smartTrafficFlow.Smart_Traffic_Flow.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TypeOfRoute {

    ARTERIAL,
    RODOVIA,
    RESIDENCIAL;

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }
}
