package edu.bsit.samarinta.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> auth(AuthException e) {
        return Map.of("error", e.getMessage(), "message", e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> denied(ForbiddenException e) {
        return Map.of("error", e.getMessage(), "message", e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> bad(IllegalArgumentException e) {
        return Map.of("error", e.getMessage(), "message", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validation(MethodArgumentNotValidException e) {
        String msg = "Validation failed";
        if (e.getBindingResult().getFieldError() != null) {
            String defaultMsg = e.getBindingResult().getFieldError().getDefaultMessage();
            if (defaultMsg != null && !defaultMsg.isBlank()) {
                msg = defaultMsg;
            }
        }
        return Map.of("error", msg, "message", msg);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> generic(Exception e) {
        String msg = e.getMessage() != null ? e.getMessage() : "An unexpected server error occurred.";
        return Map.of("error", msg, "message", msg);
    }
}
