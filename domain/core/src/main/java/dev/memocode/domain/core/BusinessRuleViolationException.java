package dev.memocode.domain.core;

import lombok.Getter;

@Getter
public class BusinessRuleViolationException extends RuntimeException implements ErrorCodedException {

    private final ErrorDetail errorDetail;

    public BusinessRuleViolationException(ErrorDetail errorDetail) {
        super(errorDetail.getDefaultMessage());
        this.errorDetail = errorDetail;
    }
}
