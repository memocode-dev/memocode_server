package dev.memocode.memo_server.domain.memo.repository.impl;

import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MemoVersionRepositoryCustom {

    Page<MemoVersion> findAllByMemoVersion(UUID memoId, Pageable pageable);
}
