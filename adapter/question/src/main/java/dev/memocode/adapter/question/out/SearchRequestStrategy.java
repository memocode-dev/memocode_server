package dev.memocode.adapter.question.out;

import com.meilisearch.sdk.SearchRequest;

public interface SearchRequestStrategy {
    SearchRequest createSearchRequest(String query, int page, int pageSize);
}
