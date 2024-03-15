package dev.memocode.memo_server.domain.memo.repository;

import dev.memocode.memo_server.domain.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long>, MemoRepositoryCustom {

    Optional<Memo> findById(UUID memoId);

    @Query("SELECT MAX(m.sequence) FROM Memo m WHERE m.author.id = :authorId")
    Integer findMaxSequenceByAuthorId(@Param("authorId") UUID authorId);
}
