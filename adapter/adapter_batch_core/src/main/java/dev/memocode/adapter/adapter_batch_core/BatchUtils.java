package dev.memocode.adapter.adapter_batch_core;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BatchUtils {

    private final JobExplorer jobExplorer;

    private static final Instant DEFAULT_BEFORE_LAST_UPDATED_AT = Instant.parse("1970-01-01T00:00:00Z");
    public static final String VERSION_NAME = "version";
    public static final String BEFORE_LAST_UPDATED_AT_NAME = "before_last_updated_at";
    public static final String LAST_UPDATED_AT_NAME = "last_updated_at";

    public JobParameters determineJobParameters(String jobName, long version, Instant newLastUpdatedAt) {
        Optional<JobInstance> _lastJobInstance =
                Optional.ofNullable(jobExplorer.getLastJobInstance(jobName));

        if (_lastJobInstance.isEmpty()) {
            return createJobParameters(DEFAULT_BEFORE_LAST_UPDATED_AT, version, newLastUpdatedAt);
        } else {
            JobInstance jobInstance = _lastJobInstance.get();
            JobExecution lastJobExecution = jobExplorer.getLastJobExecution(jobInstance);
            assert lastJobExecution != null;
            JobParameters lastJobParameters = lastJobExecution.getJobParameters();
            String lastUpdatedAt = lastJobParameters.getString(LAST_UPDATED_AT_NAME);
            Long lastVersion = lastJobParameters.getLong(VERSION_NAME);
            assert lastUpdatedAt != null;
            assert lastVersion != null;

            if (version != lastVersion) {
                return createJobParameters(DEFAULT_BEFORE_LAST_UPDATED_AT, version, newLastUpdatedAt);
            } else {
                if (lastJobExecution.getStatus() != BatchStatus.COMPLETED) {
                    return lastJobParameters;
                } else {
                    return createJobParameters(Instant.parse(lastUpdatedAt), version, newLastUpdatedAt);
                }
            }
        }
    }

    private JobParameters createJobParameters(Instant beforeLastUpdatedAt, Long version, Instant lastUpdatedAt) {
        assert beforeLastUpdatedAt != null : "beforeLastUpdatedAt is null";
        assert version != null : "version is null";

        return new JobParametersBuilder()
                .addString(BEFORE_LAST_UPDATED_AT_NAME, beforeLastUpdatedAt.toString())
                .addString(LAST_UPDATED_AT_NAME, lastUpdatedAt.toString())
                .addLong(VERSION_NAME, version)
                .toJobParameters();
    }
}
