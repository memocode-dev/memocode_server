package dev.memocode.adapter.memo;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.Settings;
import com.meilisearch.sdk.model.TaskInfo;
import dev.memocode.adapter.memo.out.MeiliSearchConfig;
import dev.memocode.adapter.memo.out.MeilisearchSearchMemoRepository;
import dev.memocode.adapter.memo.test_container.MeilisearchContainer;
import dev.memocode.domain.memo.Memo;
import dev.memocode.domain.user.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MeiliSearchConfig.class, MeilisearchSearchMemoRepository.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchMemoTest {
    private final static String MEILISEARCH_MASTER_KEY = "masterKey";

    private final static String MEILISEARCH_MEMO_INDEX_NAME = "memos";

    @Container
    public final static MeilisearchContainer meilisearch = new MeilisearchContainer()
            .withNetworkAliases("meilisearch")
            .withMasterKey(MEILISEARCH_MASTER_KEY);

    @Autowired
    private Client client;

    @Autowired
    private MeilisearchSearchMemoRepository meilisearchMemoRepository;

    private final UUID MEMO_ID = UUID.randomUUID();
    private final UUID MEMO_ID_2 = UUID.randomUUID();
    private final User user;

    public SearchMemoTest() {
        meilisearch.start();

        this.user = User.builder()
                .id(UUID.randomUUID())
                .build();
    }

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        String meilisearchHost = meilisearch.getHost();
        int meilisearchPort = meilisearch.getFirstMappedPort();

        registry.add("custom.meilisearch.url", () -> "http://" + meilisearchHost + ":" + meilisearchPort);
        registry.add("custom.meilisearch.api-key", () -> MEILISEARCH_MASTER_KEY);
        registry.add("custom.meilisearch.index.memos", () -> MEILISEARCH_MEMO_INDEX_NAME);
    }

    @BeforeAll
    void setUp() throws JSONException {
        // 인덱스 생성
        TaskInfo taskInfo = client.createIndex(MEILISEARCH_MEMO_INDEX_NAME, "id");
        client.waitForTask(taskInfo.getTaskUid());

        // 인덱스 설정
        Index index = client.getIndex(MEILISEARCH_MEMO_INDEX_NAME);
        Settings settings = new Settings();
        settings.setDisplayedAttributes(new String[]{"id", "title", "content", "summary", "author_id", "visibility", "created_at", "updated_at"});
        settings.setSearchableAttributes(new String[]{"title", "content", "summary"});
        settings.setFilterableAttributes(new String[]{"user_id", "is_deleted", "visibility"});
        TaskInfo updateSettingsTaskInfo = index.updateSettings(settings);
        client.waitForTask(updateSettingsTaskInfo.getTaskUid());

        // 문서 생성
        JSONArray array = new JSONArray()
                .put(new JSONObject()
                        .put("id", MEMO_ID)
                        .put("title", "제목1")
                        .put("content", "내용1")
                        .put("summary", "요약1")
                        .put("user_id", user.getId())
                        .put("visibility", true)
                        .put("created_at", Instant.now())
                        .put("updated_at", Instant.now())
                        .put("deleted_at", null)
                        .put("is_deleted", false)
                )
                .put(new JSONObject()
                        .put("id", MEMO_ID_2)
                        .put("title", "제목2")
                        .put("content", "내용2")
                        .put("summary", "요약2")
                        .put("user_id", user.getId())
                        .put("visibility", false)
                        .put("created_at", Instant.now())
                        .put("updated_at", Instant.now())
                        .put("deleted_at", Instant.now())
                        .put("is_deleted", true)
                )
                ;
        String documents = array.getJSONObject(0).toString();
        TaskInfo addDocumentsTaskInfo = index.addDocuments(documents);
        client.waitForTask(addDocumentsTaskInfo.getTaskUid());
    }

    @Test
    void searchMemo() {

        Page<Memo> page = meilisearchMemoRepository.searchMyMemo(user, "제목", 0, 10);
        List<Memo> content = page.getContent();

        assertThat(content.size()).isEqualTo(1);
        assertThat(content.get(0)).isNotNull();
        assertThat(content.get(0).getId()).isEqualTo(MEMO_ID);
    }
}
