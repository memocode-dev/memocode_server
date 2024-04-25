package dev.memocode.adapter.core;

import dev.memocode.domain.core.ErrorCodeLogLevel;
import dev.memocode.domain.core.ErrorDetail;
import lombok.Getter;

import static dev.memocode.domain.core.ErrorCodeLogLevel.*;

@Getter
public enum CoreAdapterErrorCode implements ErrorDetail {

    PERMISSION_DENIED("권한이 부족합니다.", WARNING),
    UNAUTHENTICATED("인증이 필요합니다.", WARNING),
    INTERNAL_SERVER_ERROR("서버 에러", CRITICAL),
    VALIDATION_ERROR("서버 에러", INFO),
    ;

    private final String defaultMessage;
    private final ErrorCodeLogLevel logLevel;

    CoreAdapterErrorCode(String defaultMessage, ErrorCodeLogLevel logLevel) {
        this.defaultMessage = defaultMessage;
        this.logLevel = logLevel;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
