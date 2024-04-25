package dev.memocode.application.question.repository;

import dev.memocode.domain.question.Question;
import org.springframework.data.domain.Page;

public interface SearchQuestionRepository {
    Page<Question> searchQuestion(String keyword, int page, int pageSize);
}
