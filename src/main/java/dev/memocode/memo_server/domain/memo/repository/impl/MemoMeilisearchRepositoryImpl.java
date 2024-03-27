package dev.memocode.memo_server.domain.memo.repository.impl;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.SearchResultPaginated;
import dev.memocode.memo_server.domain.memo.dto.MemoSearchRequestDTO;
import dev.memocode.memo_server.domain.memo.repository.MemoMeilisearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemoMeilisearchRepositoryImpl implements MemoMeilisearchRepository {

    private final Client client;

    @Value("${custom.meilisearch.index.memos}")
    private String meilisearchIndexMemos;

    @Override
    public SearchResultPaginated searchMemos(MemoSearchRequestDTO dto) {
        SearchRequest searchRequest = new SearchRequest(dto.getKeyword())
                .setFilter(new String[]{
                        "is_deleted = false",
                        "author_id = %s".formatted(dto.getAuthorId().toString())
                })
                .setAttributesToRetrieve(new String[]{"id", "title", "summary", "author_id"})
                .setAttributesToHighlight(new String[]{"title", "content", "summary"})
                .setAttributesToCrop(new String[]{"content"})
                .setCropLength(50)
                .setPage(dto.getPage())
                .setHitsPerPage(dto.getPageSize());

        log.info("searchRequest: {}", searchRequest.toString());

        Index index = client.getIndex(meilisearchIndexMemos);
        SearchResultPaginated searchResult = (SearchResultPaginated) index.search(searchRequest);

        log.info("searchResult: {}", searchResult);

        return searchResult;
    }
}
