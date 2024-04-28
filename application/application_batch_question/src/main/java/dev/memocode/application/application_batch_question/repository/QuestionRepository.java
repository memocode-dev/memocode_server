package dev.memocode.application.application_batch_question.repository;

import dev.memocode.domain.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
    Optional<Question> findFirstByOrderByUpdatedAtDesc();
}
