package dev.memocode.application.application_batch_memo.service;

import dev.memocode.application.application_batch_memo.batch_requester.BatchJobRequester;
import dev.memocode.application.application_batch_memo.repository.MemoRepository;
import dev.memocode.application.application_batch_memo.usecase.MemoBatchUseCase;
import dev.memocode.domain.core.NotFoundException;
import dev.memocode.domain.memo.Memo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static dev.memocode.domain.memo.MemoDomainErrorCode.MEMO_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MemoBatchService implements MemoBatchUseCase {
    private static final String MEMOS_JOB_NAME = "meilisearch-memos-job";

    private final BatchJobRequester batchJobRequester;
    private final MemoRepository memoRepository;

    @Override
    public void syncDBToMeilisearch(Long version) {
        Memo lastUpdatedMemo = memoRepository.findFirstByOrderByUpdatedAtDesc()
                .orElseThrow(() -> new NotFoundException(MEMO_NOT_FOUND));
        batchJobRequester.syncDBToMeilisearch(MEMOS_JOB_NAME, lastUpdatedMemo.getUpdatedAt(), version);
    }
}
