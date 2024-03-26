package dev.memocode.memo_server.domain.memo.repository;

import dev.memocode.memo_server.domain.memo.dto.MemoSearchDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemoMeilisearchRepository {
    List<MemoSearchDTO> searchMemos(String keyword, UUID authorId);
}
