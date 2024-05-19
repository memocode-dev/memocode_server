package dev.memocode.application.question.repository;

import dev.memocode.domain.question.immutable.ImmutableQuestion;
import org.springframework.data.domain.Page;

public interface SearchQuestionRepository {
    Page<ImmutableQuestion> searchQuestion(String keyword, int page, int pageSize);
    Page<ImmutableQuestion> findQuestionList(String username, int page, int pageSize);
}
