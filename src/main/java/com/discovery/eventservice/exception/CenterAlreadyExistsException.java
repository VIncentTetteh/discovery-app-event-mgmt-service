package com.discovery.eventservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CenterAlreadyExistsException extends RuntimeException {
    public CenterAlreadyExistsException(String message) {
        super(message);
    }
}

