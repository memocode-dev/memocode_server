package dev.memocode.adapter.adapter_meilisearch_core;

import com.meilisearch.sdk.SearchRequest;

public interface SearchRequestStrategy {
    SearchRequest createSearchRequest(String query, int page, int pageSize);
}
