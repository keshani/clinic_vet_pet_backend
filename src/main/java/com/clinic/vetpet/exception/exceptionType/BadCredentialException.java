package com.clinic.vetpet.exception.exceptionType;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class BadCredentialException extends Exception {

    public BadCredentialException(String message, Exception ex) {
        super(message, ex);
    }
}