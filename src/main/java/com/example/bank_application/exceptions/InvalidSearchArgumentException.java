package com.example.bank_application.exceptions;

public class InvalidSearchArgumentException extends IllegalArgumentException{
    public InvalidSearchArgumentException(String message) {
        super(message);
    }
}
