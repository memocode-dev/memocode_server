package dev.memocode.adapter.adapter_meilisearch_core;

import dev.memocode.domain.core.ErrorCodeLogLevel;
import dev.memocode.domain.core.ErrorDetail;
import lombok.Getter;

@Getter
public enum AdapterMeilisearchErrorCode implements ErrorDetail {

    MEMO_SEARCH_PARSING_ERROR("검색 시 파싱 오류", ErrorCodeLogLevel.CRITICAL),
    MEILISEARCH_SEARCH_ERROR("meilisearch 검색 시 오류 발생", ErrorCodeLogLevel.CRITICAL),
    MEILISEARCH_INVALID_PAGE_NUMBER("meilisearch 검색 시 page 0이하로 하실 수 없습니다.", ErrorCodeLogLevel.INFO),
    ;

    private final String defaultMessage;
    private final ErrorCodeLogLevel logLevel;

    AdapterMeilisearchErrorCode(String defaultMessage, ErrorCodeLogLevel logLevel) {
        this.defaultMessage = defaultMessage;
        this.logLevel = logLevel;
    }

    @Override
    public String getErrorCode() {
        return this.name();
    }
}
