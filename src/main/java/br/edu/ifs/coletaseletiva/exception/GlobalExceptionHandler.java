package br.edu.ifs.coletaseletiva.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Erros de validação do @Valid no Controller (retorna 400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> violations = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                violations.put(error.getField(), error.getDefaultMessage())
        );
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Erro de validação", violations);
    }

    // Erros de validação do Hibernate/JPA (retorna 400)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> violations = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                violations.put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Erro de validação", violations);
    }

    // Violação de restrição do banco (ex: nome duplicado, email duplicado)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {

        String mensagem = "Erro de integridade de dados";

        // Tenta extrair mensagem mais específica
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("duplicate") || ex.getMessage().contains("duplicado")) {
                mensagem = "Já existe um registro com esse valor";
            } else if (ex.getMessage().contains("violates not-null")) {
                mensagem = "Campo obrigatório não informado";
            } else if (ex.getMessage().contains("violates foreign key")) {
                mensagem = "Referência inválida: o registro relacionado não existe";
            }
        }

        return buildErrorResponse(HttpStatus.CONFLICT, mensagem, null);
    }

    // Recurso não encontrado (retorna 404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    // Erros genéricos (retorna 500)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
    }

    // Método auxiliar para montar a resposta padronizada
    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            HttpStatus status, String message, Map<String, String> violations) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        if (violations != null && !violations.isEmpty()) {
            body.put("violations", violations);
        }

        return ResponseEntity.status(status).body(body);
    }
}