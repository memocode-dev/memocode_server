package dev.memocode.memo_server;

import dev.memocode.memo_server.domain.base.config.HibernateNativeHints;
import org.hibernate.id.uuid.UuidGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ImportRuntimeHints(value = HibernateNativeHints.class)
public class MemoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemoServerApplication.class, args);
    }

}
