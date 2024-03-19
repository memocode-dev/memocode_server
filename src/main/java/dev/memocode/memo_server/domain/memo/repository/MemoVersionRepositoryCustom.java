package dev.memocode.memo_server.domain.memo.repository;

import dev.memocode.memo_server.domain.memo.entity.MemoVersion;

import java.util.List;
import java.util.UUID;

public interface MemoVersionRepositoryCustom {

    List<MemoVersion> findAllByMemoVersion(UUID memoId);
}
