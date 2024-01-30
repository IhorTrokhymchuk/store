package com.example.store.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, Object> errorsBody = new LinkedHashMap<>();
        errorsBody.put("timestamp", LocalDateTime.now());
        errorsBody.put("status", HttpStatus.BAD_REQUEST);
        final List<String> messagesList = ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList();
        errorsBody.put("errors", messagesList);
        return new ResponseEntity<>(errorsBody, headers, status);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String field = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            return field + " " + message;
        }
        return e.getDefaultMessage();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleCustomException(EntityNotFoundException ex,
                                                        WebRequest request) {
        return getObjectResponseEntity(ex.getMessage(), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Object> handleCustomException(RegistrationException ex,
                                                        WebRequest request) {
        return getObjectResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<Object> handleCustomException(EntityAlreadyExistsException ex,
                                                        WebRequest request) {
        return getObjectResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
    }

    private ResponseEntity<Object> getObjectResponseEntity(String message,
                                                           HttpStatus httpStatus) {
        Map<String, Object> errorsBody = new LinkedHashMap<>();
        errorsBody.put("timestamp", LocalDateTime.now());
        errorsBody.put("status", httpStatus);
        errorsBody.put("errors", message);
        return new ResponseEntity<>(errorsBody, httpStatus);
    }
}
