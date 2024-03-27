package dev.memocode.memo_server.domain.memo.repository;

import com.meilisearch.sdk.model.SearchResultPaginated;
import dev.memocode.memo_server.domain.memo.dto.MemoSearchRequestDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoMeilisearchRepository {
    SearchResultPaginated searchMemos(MemoSearchRequestDTO dto);
}
