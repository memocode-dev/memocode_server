package dev.memocode.domain.question;

import dev.memocode.domain.core.ErrorCodeLogLevel;
import dev.memocode.domain.core.ErrorDetail;
import lombok.Getter;

import static dev.memocode.domain.core.ErrorCodeLogLevel.INFO;
import static dev.memocode.domain.core.ErrorCodeLogLevel.WARNING;

@Getter
public enum QuestionDomainErrorCode implements ErrorDetail {

    NOT_QUESTION_OWNER("질문의 소유자가 아닙니다.", WARNING),
    DELETED_QUESTION("삭제된 질문입니다.", INFO),
    QUESTION_NOT_FOUND("질문을 찾을 수 없습니다.", INFO),
    DELETED_QUESTION_COMMENT("삭제된 질문 댓글입니다.", INFO),
    QUESTION_COMMENT_NOT_FOUND("질문 댓글을 찾을 수 없습니다.", INFO),
    ;

    private final String defaultMessage;
    private final ErrorCodeLogLevel logLevel;

    QuestionDomainErrorCode(String defaultMessage, ErrorCodeLogLevel logLevel) {
        this.defaultMessage = defaultMessage;
        this.logLevel = logLevel;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
