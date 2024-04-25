package dev.memocode.domain.core;

import lombok.Getter;

@Getter
public class InternalServerException extends RuntimeException implements ErrorCodedException {

    private final ErrorDetail errorDetail;

    public InternalServerException(ErrorDetail errorDetail) {
        super(errorDetail.getDefaultMessage());
        this.errorDetail = errorDetail;
    }

    public InternalServerException(ErrorDetail errorDetail, Throwable cause) {
        super(errorDetail.getDefaultMessage(), cause);
        this.errorDetail = errorDetail;
    }
}
