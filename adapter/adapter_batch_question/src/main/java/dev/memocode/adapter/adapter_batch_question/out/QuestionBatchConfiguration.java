package dev.memocode.adapter.adapter_batch_question.out;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.Task;
import com.meilisearch.sdk.model.TaskInfo;
import com.meilisearch.sdk.model.TaskStatus;
import dev.memocode.adapter.adapter_batch_core.QuerydslPagingItemReader;
import dev.memocode.adapter.adapter_batch_question.out.converter.MeilisearchQuestionConverter;
import dev.memocode.adapter.adapter_batch_question.out.dto.MeilisearchQuestion;
import dev.memocode.domain.question.Question;
import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

import static dev.memocode.domain.question.QQuestion.question;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class QuestionBatchConfiguration {
    public static final String MEILISEARCH_QUESTIONS_JOB_NAME = "meilisearch-questions-job";
    public static final String MEILISEARCH_QUESTIONS_STEP_NAME = "meilisearch-questions-step";

    private final JobRepository jobRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final PlatformTransactionManager transactionManager;

    private final MeilisearchQuestionConverter meilisearchMemoConverter;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    private final Client meilisearchClient;

    @Getter
    @Setter
    @Value("${custom.meilisearch.index.questions.name}")
    private String questionIndexName;

    private final int chunkSize = 100;

    @Bean(name = "meilisearch-questions-job")
    public Job job(@Qualifier(value = "meilisearch-questions-step") Step step) {
        return new JobBuilder(MEILISEARCH_QUESTIONS_JOB_NAME, jobRepository)
                .start(step)
                .build();
    }

    @Bean(name = "meilisearch-questions-step")
    public Step step(
        @Qualifier(value = "meilisearch-questions-job-item-reader") QuerydslPagingItemReader<Question> itemReader
    ) {
        return new StepBuilder(MEILISEARCH_QUESTIONS_STEP_NAME, jobRepository)
                .<Question, MeilisearchQuestion>chunk(chunkSize, transactionManager)
                .reader(itemReader)
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(name = "meilisearch-questions-job-item-reader")
    @StepScope
    public QuerydslPagingItemReader<Question> reader(
            @Value("#{jobParameters['before_last_updated_at']}") String before_last_updated_at,
            @Value("#{jobParameters['last_updated_at']}") String last_updated_at
            ) {
        return new QuerydslPagingItemReader<>(entityManagerFactory, chunkSize, queryFactory -> queryFactory
                .selectFrom(question)
                .where(question.updatedAt.between(
                        Instant.parse(before_last_updated_at),
                        Instant.parse(last_updated_at)))
        );
    }

    private ItemProcessor<Question, MeilisearchQuestion> processor() {
        return meilisearchMemoConverter::toMeilisearchQuestion;
    }


    private ItemWriter<MeilisearchQuestion> writer() {
        return chunk -> {
            String json = objectMapper.writeValueAsString(chunk.getItems());
            Index index = meilisearchClient.getIndex(questionIndexName);

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
