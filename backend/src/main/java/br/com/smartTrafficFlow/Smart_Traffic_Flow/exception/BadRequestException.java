package br.com.smartTrafficFlow.Smart_Traffic_Flow.exception;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message){
        super(message);
    }
}
