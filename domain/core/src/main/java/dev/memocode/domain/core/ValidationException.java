package dev.memocode.domain.core;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException implements ErrorCodedException {

    private final ErrorDetail errorDetail;

    public ValidationException(ErrorDetail errorDetail) {
        super(errorDetail.getDefaultMessage());
        this.errorDetail = errorDetail;
    }

    public ValidationException(ErrorDetail errorDetail, String message) {
        super(message);
        this.errorDetail = errorDetail;
    }
}
