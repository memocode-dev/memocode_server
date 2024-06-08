package dev.memocode.adapter.adapter_memo;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.TaskInfo;
import dev.memocode.adapter.adapter_meilisearch_core.MeiliSearchConfig;
import dev.memocode.adapter.memo.out.meilisearch.MeilisearchSearchMemoRepository;
import dev.memocode.domain.memo.ImmutableMemo;
import dev.memocode.domain.user.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MeiliSearchConfig.class, MeilisearchSearchMemoRepository.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchMemoTest {
    private final static String MEILISEARCH_MASTER_KEY = "masterKey";

    private final static String MEILISEARCH_MEMO_INDEX_NAME = "SearchMemoTest";

    @Container
    public final static MeilisearchContainer meilisearch = new MeilisearchContainer()
            .withNetworkAliases("meilisearch")
            .withMasterKey(MEILISEARCH_MASTER_KEY);

    @Autowired
    private Client client;

    @Autowired
    private MeilisearchSearchMemoRepository meilisearchSearchMemoRepository;

    private final UUID MEMO_ID = UUID.randomUUID();
    private final UUID MEMO_ID_2 = UUID.randomUUID();
    private final UUID MEMO_ID_3 = UUID.randomUUID();
    private final User user;

    public SearchMemoTest() {
        meilisearch.start();

        this.user = User.builder()
                .id(UUID.randomUUID())
                .username("memocode")
                .email("memocode@memocode.com")
                .firstName("홍")
                .lastName("길동")
                .enabled(true)
                .createdAt(Instant.now().getEpochSecond())
                .build();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        String meilisearchHost = meilisearch.getHost();
        int meilisearchPort = meilisearch.getFirstMappedPort();

        registry.add("custom.meilisearch.url", () -> "http://" + meilisearchHost + ":" + meilisearchPort);
        registry.add("custom.meilisearch.api-key", () -> MEILISEARCH_MASTER_KEY);
        registry.add("custom.meilisearch.index.memos.name", () -> MEILISEARCH_MEMO_INDEX_NAME);
    }

    @BeforeAll
    void setUp() throws JSONException {
        // 인덱스 생성
        TaskInfo taskInfo = client.createIndex(MEILISEARCH_MEMO_INDEX_NAME, "id");
        client.waitForTask(taskInfo.getTaskUid());
        Index memosIndex = client.getIndex(MEILISEARCH_MEMO_INDEX_NAME);
        memosIndex.updateSearchableAttributesSettings(new String[]{"title", "content"});
        memosIndex.updateFilterableAttributesSettings(new String[]{"deleted", "userId", "username", "visibility"});
        memosIndex.updateDisplayedAttributesSettings(
                new String[]{"id", "title", "content", "summary", "user", "visibility", "createdAt", "updatedAt"});
        memosIndex.updateSortableAttributesSettings(new String[]{"updatedAt"});

        // 문서 생성
        JSONArray array = new JSONArray()
                .put(new JSONObject()
                        .put("id", MEMO_ID)
                        .put("username", user.getUsername())
                        .put("title", "공개 제목")
                        .put("content", "내용1")
                        .put("summary", "요약1")
                        .put("userId", user.getId())
                        .put("user", convertUserToJSONObject(user))
                        .put("visibility", true)
                        .put("createdAt", Instant.now())
                        .put("updatedAt", Instant.now())
                        .put("deletedAt", null)
                        .put("deleted", false)
                )
                .put(new JSONObject()
                        .put("id", MEMO_ID_2)
                        .put("username", user.getUsername())
                        .put("title", "공개 제목")
                        .put("content", "내용2")
                        .put("summary", "요약2")
                        .put("userId", user.getId())
                        .put("user", convertUserToJSONObject(user))
                        .put("visibility", true)
                        .put("createdAt", Instant.now())
                        .put("updatedAt", Instant.now())
                        .put("deletedAt", null)
                        .put("deleted", false)
                )
                .put(new JSONObject()
                        .put("id", MEMO_ID_3)
                        .put("username", user.getUsername())
                        .put("title", "비공개 제목")
                        .put("content", "내용3")
                        .put("summary", "요약3")
                        .put("userId", user.getId())
                        .put("user", convertUserToJSONObject(user))
                        .put("visibility", false)
                        .put("createdAt", Instant.now())
                        .put("updatedAt", Instant.now())
                        .put("deletedAt", null)
                        .put("deleted", false)
                )
                ;
        String documents = array.toString();
        TaskInfo addDocumentsTaskInfo = memosIndex.addDocuments(documents);
        client.waitForTask(addDocumentsTaskInfo.getTaskUid());
    }

    // user 객체를 JSONObject로 변환하는 메소드 예시
    private JSONObject convertUserToJSONObject(User user) throws JSONException {
        return new JSONObject()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("enabled", user.getEnabled());
    }

    @Test
    @DisplayName("자신의 메모를 조회한다.")
    void searchMyMemo() {
        // DB에 내 메모만 있는 상태인지? 내 메모, 다른 사람 메모가 있는 상태에서 내것만 조회가 도는지
        String writer = "memocode";
        Page<ImmutableMemo> page = meilisearchSearchMemoRepository.searchMyMemo(user, "제목", 0, 10);
        List<ImmutableMemo> content = page.getContent();
        assertThat(content).hasSize(3);
        assertThat(content).allMatch(memo -> writer.equals(memo.getUser().getUsername()));
        assertThat(content).isSortedAccordingTo(Comparator.comparing(ImmutableMemo::getUpdatedAt));
    }

    @Test
    @DisplayName("등록된 메모를 키워드로 검색한다.")
    void searchQuestionByKeyword() {

        Page<ImmutableMemo> page = meilisearchSearchMemoRepository.searchMemoByKeyword("제목", 0, 10);
        List<ImmutableMemo> content = page.getContent();
        assertThat(content).hasSize(2);
        assertThat(content).allMatch(Objects::nonNull);
        assertThat(content).isSortedAccordingTo(Comparator.comparing(ImmutableMemo::getUpdatedAt));
    }

    @Test
    @DisplayName("비공개된 메모를 키워드로 검색할 때")
    void searchQuestionByKeywordWithVisibilityFalse() {

        Page<ImmutableMemo> page = meilisearchSearchMemoRepository.searchMemoByKeyword("비공개 제목", 0, 10);
        List<ImmutableMemo> content = page.getContent();
        assertThat(content).hasSize(0);
        assertThat(content).isSortedAccordingTo(Comparator.comparing(ImmutableMemo::getUpdatedAt));
    }

    @Test
    @DisplayName("등록된 메모를 이름으로 검색한다.")
    void searchQuestionByUsername() {

        Page<ImmutableMemo> page = meilisearchSearchMemoRepository.searchMemoByUsername("memocode", 0, 10);
        List<ImmutableMemo> content = page.getContent();
        assertThat(content).hasSize(2);
        assertThat(content).allMatch(memo -> memo.getUser().getUsername().equals("memocode"));
        assertThat(content).isSortedAccordingTo(Comparator.comparing(ImmutableMemo::getUpdatedAt));
    }
}
