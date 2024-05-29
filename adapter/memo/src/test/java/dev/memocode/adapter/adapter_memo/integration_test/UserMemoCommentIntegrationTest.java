package dev.memocode.adapter.adapter_memo.integration_test;

import dev.memocode.adapter.BaseDatabaseContainer;
import dev.memocode.adapter.TestConfig;
import dev.memocode.adapter.adapter_meilisearch_core.MeiliSearchConfig;
import dev.memocode.adapter.memo.out.meilisearch.MeilisearchSearchMemoRepository;
import dev.memocode.application.memo.dto.request.CreateChildMemoCommentRequest;
import dev.memocode.application.memo.dto.request.CreateMemoCommentRequest;
import dev.memocode.application.memo.dto.request.CreateMemoRequest;
import dev.memocode.application.memo.dto.request.UpdateMemoRequest;
import dev.memocode.application.memo.service.MemoCommentService;
import dev.memocode.application.memo.service.MemoService;
import dev.memocode.application.user.UserRepository;
import dev.memocode.domain.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = {TestConfig.class})
@Transactional
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMemoCommentIntegrationTest extends BaseDatabaseContainer {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemoService memoService;
    @Autowired
    private MemoCommentService memoCommentService;
    @MockBean
    private MeilisearchSearchMemoRepository meilisearchSearchMemoRepository;
    @MockBean
    private MeiliSearchConfig meiliSearchConfig;
    @PersistenceContext
    private EntityManager em;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(
                User.builder()
                        .id(UUID.randomUUID())
                        .username("test")
                        .enabled(true)
                        .build()
        );
    }

    @Test
    @DisplayName("username으로 전체 댓글 조회")
    void findAllByUserName() throws Exception {
        // given
        UUID memoId = memoService.createMemo(
                CreateMemoRequest.builder()
                        .title("memo title")
                        .content("memo content")
                        .summary("memo summary")
                        .userId(user.getId())
                        .security(false)
                        .tags(Set.of("tag1", "tag2"))
                        .build());
        memoService.updateMemo(
                UpdateMemoRequest.builder()
                        .memoId(memoId)
                        .userId(user.getId())
                        .visibility(true)
                        .build()
        );
        UUID memoParantCommentId = memoCommentService.createMemoComment(
                CreateMemoCommentRequest.builder()
                        .userId(user.getId())
                        .memoId(memoId)
                        .content("memo parant comment")
                        .build());
        em.flush();
        memoCommentService.createChildMemoComment(
                CreateChildMemoCommentRequest.builder()
                        .userId(user.getId())
                        .memoId(memoId)
                        .memoCommentId(memoParantCommentId)
                        .content("memo child comment")
                        .build());

        int page = 0;  // 첫 번째 페이지
        int pageSize = 10;  // 페이지당 10개의 댓글
        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/memoComments", user.getUsername())
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize))
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> {
                            jwt.claim("sub", UUID.randomUUID()); // 사용자 식별자 설정
                        })))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.pageSize").value(10))
                .andExpect(jsonPath("$.totalCount").value(2))
                .andExpect(jsonPath("$.content[0].id").value(memoParantCommentId.toString()))
                .andExpect(jsonPath("$.content[0].content").value("memo parant comment"))
                .andExpect(jsonPath("$.content[0].user.username").value("test"))
                .andExpect(jsonPath("$.content[0].childMemoComments[0].content").value("memo child comment"))
                .andExpect(jsonPath("$.content[1].content").value("memo child comment"));
    }
}