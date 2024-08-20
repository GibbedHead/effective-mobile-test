package com.chaplygin.task_manager.exception;

import com.chaplygin.task_manager.exception.model.AccessTokenExpiredException;
import com.chaplygin.task_manager.exception.model.AccessTokenSignatureException;
import com.chaplygin.task_manager.exception.model.AppErrorResponse;
import com.chaplygin.task_manager.exception.model.InvalidUserException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<Object> handleInvalidUserException(InvalidUserException ex) {
        AppErrorResponse appErrorResponse = new AppErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "User error: '%s'".formatted(ex.getMessage())
        );

        return new ResponseEntity<>(appErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessTokenExpiredException.class)
    public ResponseEntity<Object> handleAccessTokenExpiredException(AccessTokenExpiredException ex) {
        AppErrorResponse appErrorResponse = new AppErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Access token error: '%s'".formatted(ex.getMessage())
        );

        return new ResponseEntity<>(appErrorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessTokenSignatureException.class)
    public ResponseEntity<Object> handleAccessTokenSignatureException(AccessTokenSignatureException ex) {
        AppErrorResponse appErrorResponse = new AppErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Access token sign error: '%s'".formatted(ex.getMessage())
        );

        return new ResponseEntity<>(appErrorResponse, HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        AppErrorResponse appErrorResponse = new AppErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Method argument error: '%s'".formatted(ex.getMessage())
        );

        return new ResponseEntity<>(appErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppErrorResponse> handleGenericException(Exception ex) {
        AppErrorResponse errorResponse = new AppErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected error: " + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
