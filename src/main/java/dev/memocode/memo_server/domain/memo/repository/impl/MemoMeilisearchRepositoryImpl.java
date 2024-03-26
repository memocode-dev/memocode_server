package dev.memocode.memo_server.domain.memo.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.Searchable;
import dev.memocode.memo_server.domain.memo.dto.MemoSearchDTO;
import dev.memocode.memo_server.domain.memo.repository.MemoMeilisearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemoMeilisearchRepositoryImpl implements MemoMeilisearchRepository {

    private final Client client;

    private final ObjectMapper objectMapper;

    @Value("${custom.meilisearch.index.memos}")
    private String meilisearchIndexMemos;

    @Override
    public List<MemoSearchDTO> searchMemos(String keyword, UUID authorId) {
        SearchRequest searchRequest = new SearchRequest(keyword)
                .setFilter(new String[]{
                        "is_deleted = false",
                        "author_id = %s".formatted(authorId.toString())
                });
        log.info("searchRequest: {}", searchRequest.toString());

        Index index = client.getIndex(meilisearchIndexMemos);
        Searchable searchable = index.search(searchRequest);

        log.info("searchable query: {}", searchable.getQuery());
        log.info("searchable: {}", searchable);

        return searchable.getHits().stream()
                .map(hit -> objectMapper.convertValue(hit, MemoSearchDTO.class))
                .toList();
    }
}
