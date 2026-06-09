package dev.hope.vulcaobackend.Services;

public class OrderValidationException extends  RuntimeException{
    public OrderValidationException(String message){
        super(message);
    }
}
