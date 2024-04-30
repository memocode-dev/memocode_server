package dev.memocode.application.application_batch_question.service;

import dev.memocode.application.application_batch_core.batch_requester.BatchJobRequester;
import dev.memocode.application.application_batch_question.repository.QuestionRepository;
import dev.memocode.application.application_batch_question.usecase.QuestionBatchUseCase;
import dev.memocode.domain.core.NotFoundException;
import dev.memocode.domain.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static dev.memocode.domain.question.QuestionDomainErrorCode.QUESTION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class QuestionBatchService implements QuestionBatchUseCase {
    private static final String QUESTIONS_JOB_NAME = "meilisearch-questions-job";

    private final BatchJobRequester batchJobRequester;
    private final QuestionRepository questionRepository;

    @Override
    public void syncDBToMeilisearch(Long version) {
        Question lastUpdatedQuestion = questionRepository.findFirstByOrderByUpdatedAtDesc()
                .orElseThrow(() -> new NotFoundException(QUESTION_NOT_FOUND));
        batchJobRequester.syncDBToMeilisearch(QUESTIONS_JOB_NAME, lastUpdatedQuestion.getUpdatedAt(), version);
    }
}
