package com.example.bank_application.exceptions;

public class ClientNotFoundByTaxCodeException extends RuntimeException{
    public ClientNotFoundByTaxCodeException(String message) {
        super(message);
    }
}
