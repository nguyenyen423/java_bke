package com.example.mobilestore.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, Object> objectBody = new LinkedHashMap<>();
        objectBody.put("Current Timestamp", new Date());
        objectBody.put("Status", status.value());

        //Get all errors
        List<String> exceptionalErrors = ex.getBindingResult().getFieldErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.toList());

        objectBody.put("Errors", exceptionalErrors);
        return new ResponseEntity<>(objectBody, status);
        }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(AlreadyExistsException exception) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        String rootCauseMessage = Optional.ofNullable(exception.getRootCause())
                .map(Throwable::getMessage)
                .filter(StringUtils::hasText)
                .orElse(exception.getMessage());

        return buildErrorResponse("Data integrity violation", rootCauseMessage, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException exception) {
        return buildErrorResponse("Invalid credentials", exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }


    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("message", message);

        return ResponseEntity.status(status).body(errorResponse);
    }


    private ResponseEntity<Object> buildErrorResponse(String message, String details, HttpStatus status) {
        Map<String, Object> errorResponse = new LinkedHashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("message", message);
        errorResponse.put("details", details);

        return ResponseEntity.status(status).body(errorResponse);
    }

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", LocalDate.now());
//        body.put("message", ex.getMessage());
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
//        }
//
//    /** * Handle AlreadyExistsException: triggers when a duplicate resource is being created.
//     * * * @param ex      the AlreadyExistsException
//     * * @param request the current web request
//     * * @return a ResponseEntity with the error details and HttpStatus.CONFLICT */
//    @ExceptionHandler(AlreadyExistsException.class)
//    public ResponseEntity<Object> handleAlreadyExistsException(AlreadyExistsException ex, WebRequest request) {
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", new Date());
//        body.put("status", HttpStatus.CONFLICT.value());
//        body.put("message", ex.getMessage());
//        return new ResponseEntity<>(body, HttpStatus.CONFLICT);}


    }
