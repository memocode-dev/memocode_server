package dev.memocode.domain.core;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException implements ErrorCodedException {

    private final ErrorDetail errorDetail;

    public ForbiddenException(ErrorDetail errorDetail) {
        super(errorDetail.getDefaultMessage());
        this.errorDetail = errorDetail;
    }
}
