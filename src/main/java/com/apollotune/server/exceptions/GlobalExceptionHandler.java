package com.apollotune.server.exceptions;

import com.apollotune.server.payloads.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final HttpServletRequest httpServletRequest;

    public GlobalExceptionHandler(HttpServletRequest httpServletRequest) {

        this.httpServletRequest = httpServletRequest;

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> getServerExceptionHandler(@NotNull Exception exception) {

        if (exception instanceof ExpiredJwtException) {

            ApiAuthenticationExceptionModel apiAuthenticationExceptionModel = new ApiAuthenticationExceptionModel(
                    HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED,
                    "Authentication failed",
                    exception.getMessage(),
                    httpServletRequest.getRequestURL().toString(),
                    ZonedDateTime.now(ZoneId.of("Z"))
            );

            return new ResponseEntity<>(apiAuthenticationExceptionModel, HttpStatus.UNAUTHORIZED);

        }
        ApiExceptionModel apiExceptionModel = new ApiExceptionModel(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                exception.getCause(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(apiExceptionModel, HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException ex){
        ApiResponse response = new ApiResponse(ex.getMessage(), false);
        return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);
    }
}
