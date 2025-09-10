package com.example.demo.exception;

public class UpdateInActiveEmployeeException extends RuntimeException {
    public UpdateInActiveEmployeeException(String message) {
        super(message);
    }
}
