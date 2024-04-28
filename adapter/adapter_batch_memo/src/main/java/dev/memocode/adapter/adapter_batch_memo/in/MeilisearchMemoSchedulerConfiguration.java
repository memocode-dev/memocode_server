package dev.memocode.adapter.adapter_batch_memo.in;

import dev.memocode.application.application_batch_memo.usecase.MemoBatchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeilisearchMemoSchedulerConfiguration {

    private final MemoBatchUseCase memoBatchUseCase;

    @Value("${custom.meilisearch.index.memos.version}")
    private Long version;

    @Scheduled(fixedDelay = 1000)
    public void meilisearchMemoScheduler() {
        memoBatchUseCase.syncDBToMeilisearch(version);
    }
}
