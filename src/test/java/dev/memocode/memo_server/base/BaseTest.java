package dev.memocode.memo_server.base;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import dev.memocode.memo_server.domain.author.entity.Author;
import dev.memocode.memo_server.domain.author.repository.AuthorRepository;
import dev.memocode.memo_server.testcontainer.LogstashContainer;
import dev.memocode.memo_server.testcontainer.MeilisearchContainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
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
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    @Autowired
    private Client client;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    protected final static String POSTGRESQL_DATABASE = "testdb";
    protected final static String POSTGRESQL_USERNAME = "test";
    protected final static String POSTGRESQL_PASSWORD = "test";

    protected final static String MEILISEARCH_MASTER_KEY = "masterKey";

    protected static final String PIPELINE_PATH = "/usr/share/logstash/pipeline/";
    protected static final String CONFIG_PATH = "/usr/share/logstash/config/logstash.yml";
    protected static final String DRIVER_PATH = "/opt/logstash/vendor/jar/jdbc/";

    protected static String POSTGRESQL_PORT;
    protected static String MEILISEARCH_PORT;

    @ClassRule
    public static final Network network = Network.newNetwork();

    @ClassRule
    public static final PostgreSQLContainer<?> postgresql =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"))
                    .withNetwork(network)
                    .withNetworkAliases("test_postgresql")
                    .withDatabaseName(POSTGRESQL_DATABASE)
                    .withUsername(POSTGRESQL_USERNAME)
                    .withPassword(POSTGRESQL_PASSWORD);

    @ClassRule
    public final static MeilisearchContainer meilisearch = new MeilisearchContainer()
            .withNetwork(network)
            .withNetworkAliases("meilisearch")
            .withMasterKey(MEILISEARCH_MASTER_KEY);

    @ClassRule
    public static LogstashContainer logstash;

    public BaseTest() {
        postgresql.start();
        meilisearch.start();

        POSTGRESQL_PORT = String.valueOf(postgresql.getMappedPort(5432));
        MEILISEARCH_PORT = String.valueOf(meilisearch.getMappedPort(7700));

        logstash = new LogstashContainer()
                .dependsOn(postgresql, meilisearch)
                .withNetwork(network)
                .withNetworkAliases("logstash")
                .withEnv("POSTGRESQL_DATABASE", POSTGRESQL_DATABASE)
                .withEnv("POSTGRESQL_USERNAME", POSTGRESQL_USERNAME)
                .withEnv("POSTGRESQL_PASSWORD", POSTGRESQL_PASSWORD)
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
        registry.add("spring.datasource.url", postgresql::getJdbcUrl);
        registry.add("spring.datasource.password", postgresql::getPassword);
        registry.add("spring.datasource.username", postgresql::getUsername);
        registry.add("custom.meilisearch.url", () -> "http://localhost:%s".formatted(MEILISEARCH_PORT));
        registry.add("custom.meilisearch.api-key", () -> MEILISEARCH_MASTER_KEY);
        registry.add("custom.meilisearch.index.memos", () -> "memos");
    }

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
                .enabled(true)
                .build();
        authorRepository.save(this.author);
    }

    @AfterEach
    void afterEach() {
        Index memosIndex = client.getIndex("memos");
        memosIndex.deleteAllDocuments();

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        entityManager.createNativeQuery("TRUNCATE TABLE comments RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE memo_version RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE memos RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE series RESTART IDENTITY CASCADE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE user_entity RESTART IDENTITY CASCADE").executeUpdate();
        transaction.commit();
    }
}
