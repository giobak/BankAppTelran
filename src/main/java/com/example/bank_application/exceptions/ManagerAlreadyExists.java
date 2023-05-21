package com.example.bank_application.exceptions;

public class ManagerAlreadyExists extends RuntimeException{
    public ManagerAlreadyExists(String message) {
        super(message);
    }
}
