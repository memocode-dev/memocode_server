package dev.memocode.memo_server.domain.memo.repository;

import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import dev.memocode.memo_server.domain.memo.entity.SelectedMemoVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<SelectedMemoVersion, Long> {
    void deleteByMemoAndMemoVersion(Memo memo, MemoVersion memoVersion);
}
