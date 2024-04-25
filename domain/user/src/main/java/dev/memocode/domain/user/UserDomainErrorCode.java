package dev.memocode.domain.user;

import dev.memocode.domain.core.ErrorCodeLogLevel;
import dev.memocode.domain.core.ErrorDetail;
import lombok.Getter;

import static dev.memocode.domain.core.ErrorCodeLogLevel.INFO;
import static dev.memocode.domain.core.ErrorCodeLogLevel.WARNING;

@Getter
public enum UserDomainErrorCode implements ErrorDetail {
    USER_NOT_FOUND("유저를 찾을 수 없습니다.", INFO),
    DELETED_USER("삭제된 유저입니다.", WARNING),
    ;

    private final String defaultMessage;
    private final ErrorCodeLogLevel logLevel;

    UserDomainErrorCode(String defaultMessage, ErrorCodeLogLevel logLevel) {
        this.defaultMessage = defaultMessage;
        this.logLevel = logLevel;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
