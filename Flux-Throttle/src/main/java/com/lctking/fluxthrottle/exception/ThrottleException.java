package com.lctking.fluxthrottle.exception;

public class ThrottleException extends RuntimeException{
    public ThrottleException(String message){
        super(message);
    }
}
