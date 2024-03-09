package dev.memocode.memo_server.domain.memo.repository.impl;

import dev.memocode.memo_server.domain.memo.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MemoRepositoryCustom {

    Page<Memo> findByAuthorId(UUID authorId, Pageable pageable);
}
