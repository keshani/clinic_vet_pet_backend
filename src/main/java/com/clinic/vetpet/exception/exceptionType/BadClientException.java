package com.clinic.vetpet.exception.exceptionType;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadClientException extends Exception {

    public BadClientException(String message) {
        super(message);
    }
}