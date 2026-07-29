package com.todolist.api.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.validation.FieldError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Tratamento centralizado de exceções de todos os controllers.
 *
 * <p>Garante o status HTTP adequado para cada tipo de falha e impede que detalhes internos
 * (stack traces, mensagens do driver do banco) vazem na resposta.</p>
 *
 * <p>Erros de validação respondem com um mapa campo → mensagem, para o cliente destacar cada
 * campo problemático. Os demais respondem com {@code {"error": "..."}}.</p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Mensagem deliberadamente genérica: a causa real vai para o log, não para o cliente. */
    private static final String GENERIC_ERROR_MESSAGE = "Internal server error. Please try again.";

    /** Falhas de Bean Validation em corpos anotados com {@code @Valid} → 400 com mapa de campos. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Parâmetro de URL com o tipo errado, como {@code GET /api/tasks/abc} → 400.
     *
     * <p>Sem este handler o caso cairia no tratamento genérico e retornaria 500, culpando o
     * servidor por um erro que é do cliente.</p>
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        logger.debug("Invalid value '{}' for parameter '{}'", ex.getValue(), ex.getName());
        return errorResponse(HttpStatus.BAD_REQUEST,
                "Invalid value for parameter '" + ex.getName() + "'");
    }

    /** Corpo ilegível — JSON malformado ou ausente onde é obrigatório → 400 em vez de 500. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleUnreadableBody(HttpMessageNotReadableException ex) {
        logger.debug("Unreadable request body: {}", ex.getMessage());
        return errorResponse(HttpStatus.BAD_REQUEST, "Malformed or missing request body");
    }

    /** Método HTTP não suportado pelo endpoint → 405 com o cabeçalho {@code Allow}. */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        logger.debug("Unsupported method {} for this endpoint", ex.getMethod());

        HttpHeaders headers = new HttpHeaders();
        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        if (supportedMethods != null) {
            headers.setAllow(supportedMethods);
        }

        Map<String, String> error = new HashMap<>();
        error.put("error", "Method " + ex.getMethod() + " is not supported for this endpoint");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).headers(headers).body(error);
    }

    /** Argumento rejeitado pela lógica de negócio → 400. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return errorResponse(HttpStatus.BAD_REQUEST,
                ex.getMessage() != null ? ex.getMessage() : "Invalid request");
    }

    /** Rede de segurança: registra a causa no log e devolve 500 com mensagem genérica. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericError(Exception ex) {
        logger.error("Unhandled exception: ", ex);
        return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, GENERIC_ERROR_MESSAGE);
    }

    /** Monta o corpo {@code {"error": "..."}} usado por todos os handlers exceto o de validação. */
    private ResponseEntity<Map<String, String>> errorResponse(HttpStatus status, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return ResponseEntity.status(status).body(error);
    }
}
