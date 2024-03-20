package dev.memocode.memo_server.domain.memocomment.repository;

import dev.memocode.memo_server.domain.memocomment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostCommentRepository extends JpaRepository<Comment, UUID> {
}
