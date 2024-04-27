package dev.memocode.adapter.adapter_batch_memo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "dev.memocode.domain.core",
        "dev.memocode.domain.user",
        "dev.memocode.domain.memo",
        "dev.memocode.application.core",
        "dev.memocode.application.user",
        "dev.memocode.adapter.user",
        "dev.memocode.adapter.adapter_meilisearch_core",
        "dev.memocode.adapter.adapter_db_core",
        "dev.memocode.adapter.adapter_batch_core",
        "dev.memocode.adapter.adapter_batch_memo",
})
@EnableJpaAuditing
@EntityScan(basePackages = {
        "dev.memocode.domain.core",
        "dev.memocode.domain.user",
        "dev.memocode.domain.memo",
})
@EnableJpaRepositories(basePackages = {
        "dev.memocode.application.user",
        "dev.memocode.application.application_batch_memo",
})
@EnableBatchProcessing
public class TestConfiguration {
}
