package dev.memocode.adapter.memo;

import dev.memocode.domain.core.ErrorCodeLogLevel;
import dev.memocode.domain.core.ErrorDetail;
import lombok.Getter;

import static dev.memocode.domain.core.ErrorCodeLogLevel.CRITICAL;
import static dev.memocode.domain.core.ErrorCodeLogLevel.INFO;

@Getter
public enum MemoAdapterErrorCode implements ErrorDetail {

    MEMO_SEARCH_PARSING_ERROR("검색 시 파싱 오류", CRITICAL),
    MEILISEARCH_SEARCH_ERROR("meilisearch 검색 시 오류 발생", CRITICAL),
    MEILISEARCH_INVALID_PAGE_NUMBER("meilisearch 검색 시 page 0이하로 하실 수 없습니다.", INFO),
    ;

    private final String defaultMessage;
    private final ErrorCodeLogLevel logLevel;

    MemoAdapterErrorCode(String defaultMessage, ErrorCodeLogLevel logLevel) {
        this.defaultMessage = defaultMessage;
        this.logLevel = logLevel;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
