package br.com.smartTrafficFlow.Smart_Traffic_Flow.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message){
        super (message);
    }
}
