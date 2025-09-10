package com.example.demo.advice;

import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidSalaryEmployeeException;
import com.example.demo.exception.UpdateInActiveEmployeeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseException responseStatusExceptionHandler(Exception e) {
        return new ResponseException(e.getMessage());
    }

    @ExceptionHandler(InvalidAgeEmployeeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException invalidAgeEmployeeExceptionHandler(InvalidAgeEmployeeException e) {
        return new ResponseException(e.getMessage());
    }

    @ExceptionHandler(InvalidSalaryEmployeeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException invalidSalaryEmployeeExceptionHandler(InvalidSalaryEmployeeException e) {
        return new ResponseException(e.getMessage());
    }

    @ExceptionHandler(UpdateInActiveEmployeeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException updateInActiveEmployeeExceptionHandler(UpdateInActiveEmployeeException e) {
        return new ResponseException(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException exceptionHandler(Exception e) {
        return new ResponseException(e.getMessage());
    }
}
