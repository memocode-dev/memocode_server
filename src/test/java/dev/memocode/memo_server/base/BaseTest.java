package dev.memocode.memo_server.base;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import dev.memocode.memo_server.domain.author.entity.Author;
import dev.memocode.memo_server.domain.author.repository.AuthorRepository;
import dev.memocode.memo_server.testcontainer.LogstashContainer;
import dev.memocode.memo_server.testcontainer.MeilisearchContainer;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.UUID;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DisabledInAotMode
public abstract class BaseTest {

    protected final static String MYSQL_DATABASE = "testdb";
    protected final static String MYSQL_USERNAME = "test";
    protected final static String MYSQL_PASSWORD = "test";

    protected final static String MEILISEARCH_MASTER_KEY = "masterKey";

    protected static final String PIPELINE_PATH = "/usr/share/logstash/pipeline/";
    protected static final String CONFIG_PATH = "/usr/share/logstash/config/logstash.yml";
    protected static final String DRIVER_PATH = "/opt/logstash/vendor/jar/jdbc/";

    protected static String MYSQL_PORT;
    protected static String MEILISEARCH_PORT;

    @ClassRule
    static final Network network = Network.newNetwork();

    @ClassRule
    final static MySQLContainer<?> mysql =
            new MySQLContainer<>(DockerImageName.parse("mysql:8.3.0"))
                    .withNetwork(network)
                    .withNetworkAliases("mysql")
                    .withDatabaseName(MYSQL_DATABASE)
                    .withUsername(MYSQL_USERNAME)
                    .withPassword(MYSQL_PASSWORD);

    @ClassRule
    final static MeilisearchContainer meilisearch = new MeilisearchContainer()
            .withNetwork(network)
            .withNetworkAliases("meilisearch")
            .withMasterKey(MEILISEARCH_MASTER_KEY);

    @ClassRule
    protected final LogstashContainer logstash;

    public BaseTest() {
        mysql.start();
        meilisearch.start();

        MYSQL_PORT = String.valueOf(mysql.getMappedPort(3306));
        MEILISEARCH_PORT = String.valueOf(meilisearch.getMappedPort(7700));

        logstash = new LogstashContainer()
                .dependsOn(mysql, meilisearch)
                .withNetwork(network)
                .withNetworkAliases("logstash")
                .withEnv("MYSQL_DATABASE", MYSQL_DATABASE)
                .withEnv("MYSQL_USERNAME", MYSQL_USERNAME)
                .withEnv("MYSQL_PASSWORD", MYSQL_PASSWORD)
                .withEnv("MEILISEARCH_MASTER_KEY", MEILISEARCH_MASTER_KEY)
                .withClasspathResourceMapping(
                        "docker/logstash/pipeline", PIPELINE_PATH, BindMode.READ_ONLY)
                .withClasspathResourceMapping(
                        "docker/logstash/config/logstash.yml", CONFIG_PATH, BindMode.READ_ONLY)
                .withClasspathResourceMapping(
                        "docker/logstash/drivers", DRIVER_PATH, BindMode.READ_ONLY);
    }

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("custom.meilisearch.url", () -> "http://localhost:%s".formatted(MEILISEARCH_PORT));
        registry.add("custom.meilisearch.api-key", () -> MEILISEARCH_MASTER_KEY);
        registry.add("custom.meilisearch.index.memos", () -> "memos");
    }

    @Autowired
    private Client client;

    @Autowired
    private AuthorRepository authorRepository;

    protected Author author;

    @BeforeAll
    void beforeAll() {
        client.createIndex("memos", "id");
        Index memosIndex = client.getIndex("memos");
        memosIndex.updateSearchableAttributesSettings(new String[]{"title", "content"});
        memosIndex.updateFilterableAttributesSettings(new String[]{"is_deleted", "author_id"});
        memosIndex.updateDisplayedAttributesSettings(new String[]{"id", "title", "summary", "author_id"});
    }

    @BeforeEach
    void beforeEach() {

        // 유저 생성
        this.author = Author.builder()
                .id(UUID.randomUUID())
                .username("테스트이름")
                .nickname("테스트닉네임")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .deleted(false)
                .deletedAt(null)
                .build();
        authorRepository.save(this.author);
    }

    @AfterEach
    void afterEach() {
        Index memosIndex = client.getIndex("memos");
        memosIndex.deleteAllDocuments();
    }
}
