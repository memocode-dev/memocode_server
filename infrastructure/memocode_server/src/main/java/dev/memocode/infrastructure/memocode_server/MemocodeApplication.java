package dev.memocode.infrastructure.memocode_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "dev.memocode.infrastructure.memocode_server",
        "dev.memocode.domain.core",
        "dev.memocode.domain.user",
        "dev.memocode.domain.memo",
        "dev.memocode.domain.tag",
        "dev.memocode.domain.question",
        "dev.memocode.application.user",
        "dev.memocode.application.memo",
        "dev.memocode.application.tag",
        "dev.memocode.application.question",
        "dev.memocode.adapter.core",
        "dev.memocode.adapter.user",
        "dev.memocode.adapter.memo",
        "dev.memocode.adapter.tag",
        "dev.memocode.adapter.question"
})
@EnableJpaAuditing
@EntityScan(basePackages = {
        "dev.memocode.domain.core",
        "dev.memocode.domain.user",
        "dev.memocode.domain.memo",
        "dev.memocode.domain.tag",
        "dev.memocode.domain.question"
})
@EnableJpaRepositories(basePackages = {
        "dev.memocode.application.user",
        "dev.memocode.application.memo",
        "dev.memocode.application.tag",
        "dev.memocode.application.question"
})
public class MemocodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemocodeApplication.class, args);
    }

}
