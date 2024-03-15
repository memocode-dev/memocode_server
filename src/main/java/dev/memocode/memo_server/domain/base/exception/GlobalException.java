package dev.memocode.memo_server.domain.base.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final GlobalError error;

    public GlobalException(GlobalErrorCode code) {
        super(code.getMessage());
        this.error = GlobalError.of(code);
    }

    public GlobalException(GlobalErrorCode code, String message) {
        super(code.getMessage());
        this.error = GlobalError.of(code, message);
    }
}

