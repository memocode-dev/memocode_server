package dev.memocode.memo_server.domain.memo.repository;

import dev.memocode.memo_server.domain.memo.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MemoRepositoryCustom {

    List<Memo> findByAuthorId(UUID authorId);

    Page<Memo> findByPosts(Pageable pageable);
}
