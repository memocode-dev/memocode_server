package dev.memocode.adapter.adapter_batch_memo.out;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.Task;
import com.meilisearch.sdk.model.TaskInfo;
import com.meilisearch.sdk.model.TaskStatus;
import dev.memocode.adapter.adapter_batch_core.QuerydslPagingItemReader;
import dev.memocode.adapter.adapter_batch_memo.out.converter.MeilisearchMemoConverter;
import dev.memocode.adapter.adapter_batch_memo.out.dto.MeilisearchMemo;
import dev.memocode.adapter.adapter_meilisearch_core.MeiliSearchConfig;
import dev.memocode.domain.memo.Memo;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Instant;

import static dev.memocode.domain.memo.QMemo.memo;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MemoConfiguration {
    public static final String MEILISEARCH_MEMOS_JOB_NAME = "meilisearch-memos-job";
    public static final String MEILISEARCH_MEMOS_STEP_NAME = "meilisearch-memos-step";

    private final JobRepository jobRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final PlatformTransactionManager transactionManager;

    private final MeilisearchMemoConverter meilisearchMemoConverter;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    private final Client meilisearchClient;

    private final MeiliSearchConfig meiliSearchConfig;

    private final int chunkSize = 100;

    @Bean(name = "meilisearch-memos-job")
    public Job job(@Qualifier(value = "meilisearch-memos-step") Step step) {
        return new JobBuilder(MEILISEARCH_MEMOS_JOB_NAME, jobRepository)
                .start(step)
                .build();
    }

    @Bean(name = "meilisearch-memos-step")
    public Step step(
            @Qualifier(value = "meilisearch-memos-job-item-reader") QuerydslPagingItemReader<Memo> itemReader
    ) {
        return new StepBuilder(MEILISEARCH_MEMOS_STEP_NAME, jobRepository)
                .<Memo, MeilisearchMemo>chunk(chunkSize, transactionManager)
                .reader(itemReader)
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(name = "meilisearch-memos-job-item-reader")
    @StepScope
    public QuerydslPagingItemReader<Memo> reader(
            @Value("#{jobParameters['before_last_updated_at']}") String before_last_updated_at,
            @Value("#{jobParameters['last_updated_at']}") String last_updated_at
            ) {
        return new QuerydslPagingItemReader<>(entityManagerFactory, chunkSize, queryFactory -> queryFactory
                .selectFrom(memo)
                .where(memo.updatedAt.between(
                        Instant.parse(before_last_updated_at),
                        Instant.parse(last_updated_at)))
        );
    }

    private ItemProcessor<Memo, MeilisearchMemo> processor() {
        return meilisearchMemoConverter::toMeilisearchMemo;
    }


    private ItemWriter<MeilisearchMemo> writer() {
        return chunk -> {
            String json = objectMapper.writeValueAsString(chunk.getItems());
            Index index = meilisearchClient.getIndex(meiliSearchConfig.getMemoIndexName());

            TaskInfo taskInfo = index.addDocuments(json, "id");
            meilisearchClient.waitForTask(taskInfo.getTaskUid());

            int count = 0;
            boolean isSuccess = false;
            while (count < 1000) {
                Task task = meilisearchClient.getTask(taskInfo.getTaskUid());
                if (task.getStatus() == TaskStatus.SUCCEEDED) {
                    isSuccess = true;
                    break;
                }

                Thread.sleep(100);
                ++count;
            }

            if (!isSuccess) {
                throw new RuntimeException("meilisearchClient failed");
            }
        };
    }
}
