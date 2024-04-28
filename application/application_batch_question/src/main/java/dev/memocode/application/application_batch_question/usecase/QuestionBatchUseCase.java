package dev.memocode.application.application_batch_question.usecase;

public interface QuestionBatchUseCase {
    void syncDBToMeilisearch(Long version);
}
