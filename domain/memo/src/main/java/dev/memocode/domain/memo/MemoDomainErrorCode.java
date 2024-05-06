package dev.memocode.domain.memo;

import dev.memocode.domain.core.ErrorCodeLogLevel;
import dev.memocode.domain.core.ErrorDetail;
import lombok.Getter;

import static dev.memocode.domain.core.ErrorCodeLogLevel.INFO;
import static dev.memocode.domain.core.ErrorCodeLogLevel.WARNING;

@Getter
public enum MemoDomainErrorCode implements ErrorDetail {

    // 메모
    PROTECT_MEMO_SECURITY_UNMODIFIED("한번 보호된 메모는 보호 모도를 풀 수 없습니다.", INFO),
    PROTECT_MEMO_VISIBILITY_UNMODIFIED("한번 보호된 메모는 공개를 하실 수 없습니다.", INFO),
    PROTECT_MODE_DISABLED_ONCE_PUBLIC("한번이라도 공개된 메모는 보호 모드를 하실 수 없습니다.", INFO),
    CANNOT_UPDATE_SECURITY_AND_VISIBILITY_TOGETHER("보안과 공개성을 동시에 수정할 수 없습니다.", INFO),
    MEMO_NOT_FOUND("메모를 찾을 수 없습니다.", INFO),
    DELETED_MEMO("삭제된 메모입니다.", WARNING),
    NOT_MEMO_OWNER("메모의 소유자가 아닙니다.", WARNING),
    NOT_VISIBILITY_MEMO("공개되지 않은 메모입니다.", WARNING),

    // 메모 버전
    MEMO_VERSION_NOT_FOUND("메모 버전을 찾을 수 없습니다.", WARNING),
    NOT_MEMO_VERSION_OWNER("메모버전의 소유자가 아닙니다.", WARNING),
    DELETED_MEMO_VERSION("삭제된 메모버전입니다.", WARNING),

    // 메모 댓글
    MEMO_COMMENT_NOT_FOUND("메모 댓글을 찾을 수 없습니다.", WARNING),
    NOT_MEMO_COMMENT_OWNER("메모댓글의 소유자가 아닙니다.", WARNING),
    DELETED_MEMO_COMMENT("삭제된 메모댓글입니다.", WARNING),
    MEMO_COMMENT_PARENT_NOT_NULL("부모댓글이 존재합니다.", WARNING),
    ;

    private final String defaultMessage;
    private final ErrorCodeLogLevel logLevel;

    MemoDomainErrorCode(String defaultMessage, ErrorCodeLogLevel logLevel) {
        this.defaultMessage = defaultMessage;
        this.logLevel = logLevel;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
