package com.example.demo.exception;

public class InvalidSalaryEmployeeException extends RuntimeException {
    public InvalidSalaryEmployeeException(String message) {
        super(message);
    }
}
