package dev.memocode.adapter.adapter_batch_question.in;

import dev.memocode.application.application_batch_question.usecase.QuestionBatchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeilisearchQuestionSchedulerConfiguration {

    private final QuestionBatchUseCase questionBatchUseCase;

    @Value("${custom.meilisearch.index.questions.version}")
    private Long version;

    @Scheduled(fixedDelay = 1000)
    public void meilisearchQuestionScheduler() {
        questionBatchUseCase.syncDBToMeilisearch(version);
    }
}
