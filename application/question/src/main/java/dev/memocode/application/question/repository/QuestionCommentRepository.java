package dev.memocode.application.question.repository;

import dev.memocode.domain.question.QuestionComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuestionCommentRepository extends JpaRepository<QuestionComment, UUID> {

    Page<QuestionComment> findAllQuestionCommentByUserId(UUID userId, Pageable pageable);
}
