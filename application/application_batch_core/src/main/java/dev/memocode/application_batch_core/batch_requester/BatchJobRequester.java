package dev.memocode.application_batch_core.batch_requester;

import java.time.Instant;

public interface BatchJobRequester {
    void syncDBToMeilisearch(String jobName, Instant lastUpdatedAt, Long version);
}
