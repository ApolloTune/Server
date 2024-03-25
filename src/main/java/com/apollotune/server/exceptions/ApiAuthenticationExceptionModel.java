package com.apollotune.server.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class ApiAuthenticationExceptionModel {
    private int statusCode;
    private HttpStatus status;
    private String error;
    private String message;
    private String path;
    private ZonedDateTime timestamp;

    public ApiAuthenticationExceptionModel(int statusCode, HttpStatus status, String error, String message, String path, ZonedDateTime timestamp) {
        this.statusCode = statusCode;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }


}
