package dev.memocode.adapter.adapter_batch_memo;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.TaskInfo;
import dev.memocode.adapter.adapter_batch_core.BatchUtils;
import dev.memocode.adapter.adapter_batch_memo.test_container.MeilisearchContainer;
import dev.memocode.application.application_batch_memo.repository.MemoRepository;
import dev.memocode.application.user.UserRepository;
import dev.memocode.domain.core.NotFoundException;
import dev.memocode.domain.memo.Memo;
import dev.memocode.domain.user.User;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.IntStream;

import static dev.memocode.adapter.adapter_batch_memo.out.MemoBatchConfiguration.MEILISEARCH_MEMOS_JOB_NAME;
import static dev.memocode.domain.memo.MemoDomainErrorCode.MEMO_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@ContextConfiguration(classes = {TestConfiguration.class})
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MeilisearchMemoBatchConfigurationTest {

    protected final static String POSTGRESQL_DATABASE = "testdb";
    protected final static String POSTGRESQL_USERNAME = "test";
    protected final static String POSTGRESQL_PASSWORD = "test";

    protected final static String MEILISEARCH_MASTER_KEY = "masterKey";

    private final static String MEILISEARCH_MEMO_INDEX_NAME = "MeilisearchMemoBatchConfigurationTest";

    protected static String POSTGRESQL_PORT;
    protected static String MEILISEARCH_PORT;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private BatchUtils batchUtils;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier(value = "meilisearch-memos-job")
    private Job job;

    @Autowired
    private Client client;

    public MeilisearchMemoBatchConfigurationTest() {
        postgresql.start();
        meilisearch.start();

        POSTGRESQL_PORT = String.valueOf(postgresql.getMappedPort(5432));
        MEILISEARCH_PORT = String.valueOf(meilisearch.getMappedPort(7700));
    }

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresql::getJdbcUrl);
        registry.add("spring.datasource.password", postgresql::getPassword);
        registry.add("spring.datasource.username", postgresql::getUsername);
        registry.add("custom.meilisearch.url", () -> "http://localhost:%s".formatted(MEILISEARCH_PORT));
        registry.add("custom.meilisearch.api-key", () -> MEILISEARCH_MASTER_KEY);
        registry.add("custom.meilisearch.index.memos.name", () -> MEILISEARCH_MEMO_INDEX_NAME);
    }

    @ClassRule
    public static final Network network = Network.newNetwork();

    @Container
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

    @Test
    public void DBToMeilisearchBatchTest() throws Exception {

        createMeilisearchIndex();
        User user = createUser();
        createMemos(user);

        Memo memo = memoRepository.findFirstByOrderByUpdatedAtDesc()
                .orElseThrow(() -> new NotFoundException(MEMO_NOT_FOUND));

        JobParameters jobParameters =
                batchUtils.determineJobParameters(MEILISEARCH_MEMOS_JOB_NAME, 1, memo.getUpdatedAt());
        jobLauncherTestUtils.setJob(job);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

    private void createMeilisearchIndex() {
        TaskInfo taskInfo = client.createIndex(MEILISEARCH_MEMO_INDEX_NAME, "id");
        client.waitForTask(taskInfo.getTaskUid());
        Index memosIndex = client.getIndex(MEILISEARCH_MEMO_INDEX_NAME);
        memosIndex.updateSearchableAttributesSettings(new String[]{"title", "content"});
        memosIndex.updateFilterableAttributesSettings(new String[]{"deleted", "userId"});
        memosIndex.updateDisplayedAttributesSettings(
                new String[]{"id", "title", "content", "summary", "user", "visibility", "createdAt", "updatedAt"});
        memosIndex.updateSortableAttributesSettings(new String[]{"updatedAt"});
    }

    private User createUser() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("memocode")
                .firstName("홍")
                .lastName("길동")
                .email("memocode@memocode.com")
                .enabled(true)
                .createdAt(Instant.now().getEpochSecond())
                .build();
        return userRepository.save(user);
    }

    private void createMemos(User user) {
        IntStream.range(1, 50)
                .forEach(i ->  {
                    Memo memo = Memo.builder()
                            .id(UUID.randomUUID())
                            .title("메모제목" + i)
                            .content("메모내용" + i)
                            .summary("메모요약" + i)
                            .user(user)
                            .visibility(false)
                            .security(false)
                            .bookmarked(false)
                            .build();
                    memoRepository.save(memo);
                });
    }
}
