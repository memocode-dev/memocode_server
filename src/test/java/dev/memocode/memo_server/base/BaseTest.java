package dev.memocode.memo_server.base;

import dev.memocode.memo_server.domain.author.entity.Author;
import dev.memocode.memo_server.domain.author.repository.AuthorRepository;
import dev.memocode.memo_server.testcontainer.LogstashContainer;
import dev.memocode.memo_server.testcontainer.MeilisearchContainer;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;

@Sql("/data.sql")
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {
    protected final static String MYSQL_DATABASE = "testdb";
    protected final static String MYSQL_USERNAME = "test";
    protected final static String MYSQL_PASSWORD = "test";

    protected final static String MEILISEARCH_MASTER_KEY = "masterKey";

    protected static final String PIPELINE_PATH = "/usr/share/logstash/pipeline/";
    protected static final String CONFIG_PATH = "/usr/share/logstash/config/logstash.yml";
    protected static final String DRIVER_PATH = "/opt/logstash/vendor/jar/jdbc/";

    @ClassRule
    final static MySQLContainer<?> mysql =
            new MySQLContainer<>(DockerImageName.parse("mysql:8.3.0"))
                    .withDatabaseName(MYSQL_DATABASE)
                    .withUsername(MYSQL_USERNAME)
                    .withPassword(MYSQL_PASSWORD);

    @ClassRule
    final static MeilisearchContainer meilisearch = new MeilisearchContainer()
            .withMasterKey(MEILISEARCH_MASTER_KEY);

    @ClassRule
    private final LogstashContainer logstash;

    public BaseTest() {
        mysql.start();
        meilisearch.start();

        logstash = new LogstashContainer()
                .dependsOn(mysql, meilisearch)
                .withEnv("MYSQL_PORT", String.valueOf(mysql.getMappedPort(3306)))
                .withEnv("MYSQL_DATABASE", MYSQL_DATABASE)
                .withEnv("MYSQL_USERNAME", MYSQL_USERNAME)
                .withEnv("MYSQL_PASSWORD", MYSQL_PASSWORD)
                .withEnv("MYSQL_PASSWORD", MEILISEARCH_MASTER_KEY)
                .withEnv("MEILISEARCH_PORT", String.valueOf(meilisearch.getMappedPort(7700)))
                .withClasspathResourceMapping(
                        "docker/logstash/pipeline", PIPELINE_PATH, BindMode.READ_ONLY)
                .withClasspathResourceMapping(
                        "docker/logstash/config/logstash.yml", CONFIG_PATH, BindMode.READ_ONLY)
                .withClasspathResourceMapping(
                        "docker/logstash/drivers", DRIVER_PATH, BindMode.READ_ONLY);

        logstash.start();
    }

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.username", mysql::getUsername);
    }

    @Autowired
    private AuthorRepository authorRepository;

    protected Author author;

    @BeforeEach
    void beforeAll() {
        // 유저 생성
        this.author = Author.builder()
                .username("테스트이름")
                .nickname("테스트닉네임")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .deleted(false)
                .deletedAt(null)
                .build();
        authorRepository.save(this.author);
    }
}
