package dev.memocode.application.application_batch_memo.usecase;

public interface MemoBatchUseCase {
    void syncDBToMeilisearch(Long version);
}
