package com.example.authservice.commonhandler.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiException extends RuntimeException {

    private String exceptionCode;
    private HttpStatus httpStatus;
    private Date timestamp;

    public ApiException(Throwable cause, HttpStatus httpStatus) {
        super(cause);
        setHttpStatus(httpStatus);
    }

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        setHttpStatus(httpStatus);
    }

    public ApiException(String message ,Throwable cause, HttpStatus httpStatus) {
        super(message,cause);
        setHttpStatus(httpStatus);
    }

    public ApiException(String message) {
        super(message);
        setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
