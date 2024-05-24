package dev.memocode.adapter.question;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.model.TaskInfo;
import dev.memocode.adapter.adapter_meilisearch_core.MeiliSearchConfig;
import dev.memocode.adapter.question.out.MeilisearchSearchQuestionRepository;
import dev.memocode.domain.question.immutable.ImmutableQuestion;
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
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MeiliSearchConfig.class, MeilisearchSearchQuestionRepository.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchQuestionTest {
    private final static String MEILISEARCH_MASTER_KEY = "masterKey";

    private final static String MEILISEARCH_QUESTION_INDEX_NAME = "SearchQuestionTest";

    @Container
    public final static MeilisearchContainer meilisearch = new MeilisearchContainer()
            .withNetworkAliases("meilisearch")
            .withMasterKey(MEILISEARCH_MASTER_KEY);

    @Autowired
    private Client client;

    @Autowired
    private MeilisearchSearchQuestionRepository meilisearchSearchMemoRepository;

    private final UUID QUESTION_ID = UUID.randomUUID();
    private final UUID QUESTION_ID_2 = UUID.randomUUID();
    private final User user;

    public SearchQuestionTest() {
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
    static void posgreProperties(DynamicPropertyRegistry registry) {
        String meilisearchHost = meilisearch.getHost();
        int meilisearchPort = meilisearch.getFirstMappedPort();

        registry.add("custom.meilisearch.url", () -> "http://" + meilisearchHost + ":" + meilisearchPort);
        registry.add("custom.meilisearch.api-key", () -> MEILISEARCH_MASTER_KEY);
        registry.add("custom.meilisearch.index.questions.name", () -> MEILISEARCH_QUESTION_INDEX_NAME);
    }

    @BeforeAll
    void setUp() throws JSONException {
        // 인덱스 생성
        TaskInfo taskInfo = client.createIndex(MEILISEARCH_QUESTION_INDEX_NAME, "id");
        client.waitForTask(taskInfo.getTaskUid());
        Index questionsIndex = client.getIndex(MEILISEARCH_QUESTION_INDEX_NAME);
        questionsIndex.updateSearchableAttributesSettings(new String[]{"title", "content"});
        questionsIndex.updateFilterableAttributesSettings(new String[]{"deleted", "username"});
        questionsIndex.updateDisplayedAttributesSettings(
                new String[]{"id", "title", "content", "tags", "user", "createdAt", "updatedAt", "deleted", "deletedAt"});
        questionsIndex.updateSortableAttributesSettings(new String[]{"updatedAt"});

        // 문서 생성
        JSONArray array = new JSONArray()
                .put(new JSONObject()
                        .put("id", QUESTION_ID)
                        .put("username", user.getUsername())
                        .put("title", "제목1")
                        .put("content", "내용1")
                        .put("tags", Set.of("tag1", "tag2"))
                        .put("user", convertUserToJSONObject(user))
                        .put("createdAt", Instant.now())
                        .put("updatedAt", Instant.now())
                        .put("deleted", false)
                        .put("deletedAt", null)
                )
                .put(new JSONObject()
                        .put("id", QUESTION_ID_2)
                        .put("username", user.getUsername())
                        .put("title", "제목2")
                        .put("content", "내용2")
                        .put("tags", Set.of("tag3", "tag4"))
                        .put("user", convertUserToJSONObject(user))
                        .put("createdAt", Instant.now())
                        .put("updatedAt", Instant.now())
                        .put("deleted", true)
                        .put("deletedAt", Instant.now())
                )
                ;
        String documents = array.getJSONObject(0).toString();
        TaskInfo addDocumentsTaskInfo = questionsIndex.addDocuments(documents);
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
    @DisplayName("등록된 질문을 키워드로 검색한다.")
    void searchQuestionByKeyword() {

        Page<ImmutableQuestion> page = meilisearchSearchMemoRepository.searchQuestionByKeyword("제목1", 0, 10);
        List<ImmutableQuestion> content = page.getContent();
        assertThat(content.size()).isEqualTo(1);
        assertThat(content.getFirst()).isNotNull();
        assertThat(content.getFirst().getId()).isEqualTo(QUESTION_ID);
    }

    @Test
    @DisplayName("등록된 질문을 이름으로 검색한다.")
    void searchQuestionByUsername() {

        Page<ImmutableQuestion> page = meilisearchSearchMemoRepository.searchQuestionByUsername("memocode", 0, 10);
        List<ImmutableQuestion> content = page.getContent();
        assertThat(content.size()).isEqualTo(1);
        assertThat(content.getFirst()).isNotNull();
        assertThat(content.getFirst().getId()).isEqualTo(QUESTION_ID);
    }

}
