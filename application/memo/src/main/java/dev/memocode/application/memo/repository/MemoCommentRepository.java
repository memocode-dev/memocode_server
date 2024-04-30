package dev.memocode.application.memo.repository;

import dev.memocode.domain.memo.MemoComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemoCommentRepository extends JpaRepository<MemoComment, UUID> {
}
