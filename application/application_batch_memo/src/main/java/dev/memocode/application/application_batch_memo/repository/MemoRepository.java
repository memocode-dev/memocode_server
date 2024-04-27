package dev.memocode.application.application_batch_memo.repository;

import dev.memocode.domain.memo.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemoRepository extends JpaRepository<Memo, UUID> {
    Optional<Memo> findFirstByOrderByUpdatedAtDesc();
}
