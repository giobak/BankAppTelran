package com.example.bank_application.exceptions;

public class NotEnoughDataToCreateEntity extends RuntimeException {
    public NotEnoughDataToCreateEntity(String message) {
        super(message);
    }
}
