package dev.memocode.adapter.adapter_api_core;

import dev.memocode.domain.core.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static dev.memocode.adapter.adapter_api_core.AdapterApiErrorCode.VALIDATION_ERROR;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@RestControllerAdvice
public class ApiExceptionController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception ex) {

        log.error("{}", ex);

        ErrorResponse response = ErrorResponse.builder()
                .code(AdapterApiErrorCode.INTERNAL_SERVER_ERROR.getErrorCode())
                .message(AdapterApiErrorCode.INTERNAL_SERVER_ERROR.getDefaultMessage())
                .build();

        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> validationException(ValidationException ex) {

        log.error("{}", ex);

        ErrorResponse response = ErrorResponse.builder()
                .code(ex.getErrorDetail().getErrorCode())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> businessRuleViolationException(BusinessRuleViolationException ex) {

        log.error("{}", ex);

        ErrorResponse response = ErrorResponse.builder()
                .code(ex.getErrorDetail().getErrorCode())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.unprocessableEntity().body(response);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> forbiddenException(ForbiddenException ex) {

        log.error("{}", ex);

        ErrorResponse response = ErrorResponse.builder()
                .code(ex.getErrorDetail().getErrorCode())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(FORBIDDEN).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException ex) {

        log.error("{}", ex);

        ErrorResponse response = ErrorResponse.builder()
                .code(ex.getErrorDetail().getErrorCode())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> internalServerException(InternalServerException ex) {

        log.error("{}", ex);

        ErrorResponse response = ErrorResponse.builder()
                .code(ex.getErrorDetail().getErrorCode())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolationException(ConstraintViolationException ex) {
        log.error("{}", ex);
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().stream().findFirst()
                .orElseThrow(() -> new InternalServerException(AdapterApiErrorCode.INTERNAL_SERVER_ERROR));
        String message = constraintViolation.getMessage();
        try {
            ErrorResponse response = ErrorResponse.builder()
                    .code(message.split(":", 2)[0])
                    .message(message.split(":", 2)[1])
                    .build();

            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            ErrorResponse response = ErrorResponse.builder()
                    .code(VALIDATION_ERROR.getErrorCode())
                    .message(message)
                    .build();

            return ResponseEntity.badRequest().body(response);
        }

    }
}
