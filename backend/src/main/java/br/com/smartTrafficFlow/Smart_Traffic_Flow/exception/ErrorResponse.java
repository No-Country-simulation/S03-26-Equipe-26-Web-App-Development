package br.com.smartTrafficFlow.Smart_Traffic_Flow.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error
) {}
