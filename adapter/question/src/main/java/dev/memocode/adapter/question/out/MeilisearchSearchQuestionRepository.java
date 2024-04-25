package dev.memocode.adapter.question.out;

import dev.memocode.application.question.repository.SearchQuestionRepository;
import dev.memocode.domain.question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class MeilisearchSearchQuestionRepository implements SearchQuestionRepository {

    @Override
    public Page<Question> searchQuestion(String keyword, int page, int pageSize) {
        return PageableExecutionUtils.getPage(new ArrayList<>(), PageRequest.of(page, pageSize), () -> 0);
    }
}
