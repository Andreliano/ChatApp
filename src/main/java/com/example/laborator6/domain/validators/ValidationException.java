package com.example.laborator6.domain.validators;

public class ValidationException extends RuntimeException{
    public ValidationException(String message){
        super(message);
    }

}
