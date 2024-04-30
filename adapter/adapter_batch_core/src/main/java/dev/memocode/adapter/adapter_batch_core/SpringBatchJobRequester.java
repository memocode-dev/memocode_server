package dev.memocode.adapter.adapter_batch_core;

import dev.memocode.application.application_batch_core.batch_requester.BatchJobRequester;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class SpringBatchJobRequester implements BatchJobRequester {

    private final JobLauncher jobLauncher;
    private final BatchUtils batchUtils;
    private final JobRegistry jobRegistry;

    @Override
    public void syncDBToMeilisearch(String jobName, Instant lastUpdatedAt, Long version) {
        try {
            Job job = jobRegistry.getJob(jobName);
            JobParameters jobParameters =
                    batchUtils.determineJobParameters(jobName, version, lastUpdatedAt);
            jobLauncher.run(job, jobParameters);
        } catch (NoSuchJobException | JobParametersInvalidException | JobRestartException |
                 JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        }
    }
}
