package dev.memocode.application.application_batch_memo.batch_requester;

import java.time.Instant;

public interface BatchJobRequester {
    void syncDBToMeilisearch(String jobName, Instant lastUpdatedAt, Long version);
}
