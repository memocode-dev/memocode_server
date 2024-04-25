package dev.memocode.application.user;

import dev.memocode.domain.core.ErrorCodeLogLevel;
import dev.memocode.domain.core.ErrorDetail;
import lombok.Getter;

import static dev.memocode.domain.core.ErrorCodeLogLevel.CRITICAL;

@Getter
public enum AuthorApplicationErrorCode implements ErrorDetail {
    AUTHOR_NOT_FOUND("author를 찾을 수 없습니다.", CRITICAL),
    ;

    private final String defaultMessage;
    private final ErrorCodeLogLevel logLevel;

    AuthorApplicationErrorCode(String defaultMessage, ErrorCodeLogLevel logLevel) {
        this.defaultMessage = defaultMessage;
        this.logLevel = logLevel;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
