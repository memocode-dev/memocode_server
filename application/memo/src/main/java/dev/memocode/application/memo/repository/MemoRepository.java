package dev.memocode.application.memo.repository;

import dev.memocode.domain.memo.Memo;
import dev.memocode.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MemoRepository extends JpaRepository<Memo, UUID> {
    List<Memo> findAllByUserAndDeletedAtIsNullOrderByCreatedAt(User user);
}
