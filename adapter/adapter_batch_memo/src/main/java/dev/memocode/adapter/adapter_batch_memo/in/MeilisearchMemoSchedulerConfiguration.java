package dev.memocode.adapter.adapter_batch_memo.in;

import dev.memocode.application.application_batch_memo.usecase.MemoBatchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MeilisearchMemoSchedulerConfiguration {

    private final MemoBatchUseCase memoBatchUseCase;

    @Scheduled(fixedDelay = 60000)
    public void meilisearchMemoScheduler() {
        memoBatchUseCase.syncDBToMeilisearch(4L);
    }
}
