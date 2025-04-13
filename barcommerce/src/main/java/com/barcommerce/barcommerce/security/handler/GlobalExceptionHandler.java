package com.barcommerce.barcommerce.security.handler;

import com.barcommerce.barcommerce.security.dto.ErroDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Captura e trata erros de validação e outros erros da API.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura erros de validação em @Valid (ex: @NotBlank, @Email, @Size).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> mensagens = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(erro -> {
                    if (erro instanceof FieldError) {
                        FieldError fieldError = (FieldError) erro;
                        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
                    }
                    return erro.getDefaultMessage();
                })
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(new ErroDTO(mensagens));
    }

    /**
     * Captura erros de validação fora do contexto de @RequestBody (ex: @RequestParam).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroDTO> handleConstraintViolations(ConstraintViolationException ex) {
        List<String> mensagens = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(new ErroDTO(mensagens));
    }

    /**
     * Captura outras exceções não tratadas.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroDTO> handleGenericException(Exception ex) {
        return ResponseEntity.internalServerError().body(new ErroDTO("Erro interno: " + ex.getMessage()));
    }
}
