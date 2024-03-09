package dev.memocode.memo_server.domain.memo.repository;

import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemoVersionRepository extends JpaRepository<MemoVersion, Long> {

    @Query("select max(mv.version) from MemoVersion mv where mv.memo.id = :memoId")
    Integer findVersionByMemoId(@Param("memoId") UUID memoId);

    Optional<MemoVersion> findById(UUID memoVersionId);
}
