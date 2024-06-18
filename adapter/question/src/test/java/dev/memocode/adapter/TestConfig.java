package dev.memocode.adapter;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "dev.memocode.domain",
        "dev.memocode.application",
        "dev.memocode.adapter",
})
@EnableJpaAuditing
@EntityScan(basePackages = {
        "dev.memocode.domain",
        "dev.memocode.application",
        "dev.memocode.adapter",
})
@EnableJpaRepositories(basePackages = {
        "dev.memocode.domain",
        "dev.memocode.application",
        "dev.memocode.adapter",
})

public class TestConfig {
}
