package com.barcommerce.barcommerce.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    // Trata falhas de validação @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleValidation(
            MethodArgumentNotValidException ex, WebRequest request) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    // Trata IllegalArgumentException do mapper (tipo inválido, categoria obrigatória)
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArg(IllegalArgumentException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(),
                List.of(ex.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Captura quaisquer outras exceções
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAll(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                List.of("Erro interno: " + ex.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
