package br.com.smartTrafficFlow.Smart_Traffic_Flow.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TrafficAlert {
    NORMAL("Normal"),
    ATENCAO("Atenção"),
    CRITICO("Crítico"),
    ANOMALIA_EMOJI("\uD83D\uDEA8 ANOMALIA");

    private final String descricao;

    TrafficAlert(String descricao){
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao(){
        return descricao;
    }
}
