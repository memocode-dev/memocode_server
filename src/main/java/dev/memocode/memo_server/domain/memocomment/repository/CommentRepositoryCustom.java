package dev.memocode.memo_server.domain.memocomment.repository;

import dev.memocode.memo_server.domain.memocomment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepositoryCustom {
    Page<Comment> findAllParentsCommentByMemoId(UUID memoId, Pageable pageable);
}