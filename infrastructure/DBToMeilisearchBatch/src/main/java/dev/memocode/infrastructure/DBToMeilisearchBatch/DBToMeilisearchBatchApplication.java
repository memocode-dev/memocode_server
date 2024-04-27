package dev.memocode.infrastructure.DBToMeilisearchBatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {
        "dev.memocode.infrastructure.DBToMeilisearchBatch",
        "dev.memocode.domain.core",
        "dev.memocode.domain.user",
        "dev.memocode.domain.memo",
        "dev.memocode.application.user",
        "dev.memocode.application.application_batch_memo",
        "dev.memocode.adapter.adapter_meilisearch_core",
        "dev.memocode.adapter.adapter_batch_core",
        "dev.memocode.adapter.adapter_batch_memo",
})
@EntityScan(basePackages = {
        "dev.memocode.domain.core",
        "dev.memocode.domain.user",
        "dev.memocode.domain.memo",
})
@EnableJpaRepositories(basePackages = {
        "dev.memocode.application.user",
        "dev.memocode.application.application_batch_memo",
})
public class DBToMeilisearchBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(DBToMeilisearchBatchApplication.class, args);
    }
}
