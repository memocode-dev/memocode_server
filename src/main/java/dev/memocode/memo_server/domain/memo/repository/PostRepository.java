package dev.memocode.memo_server.domain.memo.repository;

import dev.memocode.memo_server.domain.memo.entity.SelectedMemoVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<SelectedMemoVersion, Long> {
}
