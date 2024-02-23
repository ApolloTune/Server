package com.apollotune.server.exceptions;

import com.apollotune.server.payloads.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException ex){
        ApiResponse response = new ApiResponse(ex.getMessage(), false);
        return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);
    }
}
