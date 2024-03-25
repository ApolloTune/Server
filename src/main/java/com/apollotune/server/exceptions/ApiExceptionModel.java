package com.apollotune.server.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
public class ApiExceptionModel {
    private int statusCode;
    private String message;
    private Throwable cause;
    private HttpStatus httpStatus;
    private ZonedDateTime timestamp;

    public ApiExceptionModel(int value, String message, Throwable cause, HttpStatus httpStatus, ZonedDateTime z) {
        statusCode = value;
        this.message = message;
        this.cause = cause;
        this.httpStatus = httpStatus;
        this.timestamp = z;
    }
}
