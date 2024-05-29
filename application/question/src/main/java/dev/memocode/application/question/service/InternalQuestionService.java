package dev.memocode.application.question.service;

import dev.memocode.application.question.repository.QuestionRepository;
import dev.memocode.domain.core.NotFoundException;
import dev.memocode.domain.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static dev.memocode.domain.question.QuestionDomainErrorCode.QUESTION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class InternalQuestionService {

    private final QuestionRepository questionRepository;

    public Question findByIdElseThrow(UUID questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException(QUESTION_NOT_FOUND));
    }

}
