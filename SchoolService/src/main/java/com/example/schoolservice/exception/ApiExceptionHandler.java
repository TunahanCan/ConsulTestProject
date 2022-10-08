package com.example.schoolservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author tunahanCan
 */
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApiException(ApiException e) {
        Map<String, Object> message = new HashMap<>();
        message.put("errorMessage", e.getMessage());
        message.put("status", e.getHttpStatus());
        message.put("date-> ", new Date());
        return new ResponseEntity(message, e.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        Map<String, Object> message = new HashMap<>();
        message.put("errorMessage", e.getMessage());
        message.put("date-> ", new Date());
        return new ResponseEntity(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}