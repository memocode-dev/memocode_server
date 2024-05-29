package dev.memocode.adapter.question.in.api;

import dev.memocode.adapter.BaseDatabaseContainer;
import dev.memocode.adapter.TestConfig;
import dev.memocode.adapter.adapter_meilisearch_core.MeiliSearchConfig;
import dev.memocode.adapter.question.out.MeilisearchSearchQuestionRepository;
import dev.memocode.application.question.dto.CreateChildQuestionCommentRequest;
import dev.memocode.application.question.dto.CreateQuestionCommentRequest;
import dev.memocode.application.question.dto.CreateQuestionRequest;
import dev.memocode.application.question.service.QuestionCommentService;
import dev.memocode.application.question.service.QuestionService;
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
class UserQuestionCommentIntegrationTest extends BaseDatabaseContainer {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionCommentService questionCommentService;
    @MockBean
    private MeilisearchSearchQuestionRepository meilisearchSearchQuestionRepository;
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
        UUID questionId = questionService.createQuestion(
                CreateQuestionRequest.builder()
                        .userId(user.getId())
                        .title("question title")
                        .content("question content")
                        .tags(Set.of("tag1", "tag2"))
                        .build());
        UUID questionParantCommentId = questionCommentService.createQuestionComment(
                CreateQuestionCommentRequest.builder()
                        .userId(user.getId())
                        .questionId(questionId)
                        .content("question parant comment")
                        .build());
        em.flush();
        questionCommentService.createChildQuestionComment(
                CreateChildQuestionCommentRequest.builder()
                        .userId(user.getId())
                        .questionId(questionId)
                        .questionCommentId(questionParantCommentId)
                        .content("question child comment")
                        .build());

        int page = 0;  // 첫 번째 페이지
        int pageSize = 10;  // 페이지당 10개의 댓글
        // when
        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{username}/questionComments", user.getUsername())
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
                .andExpect(jsonPath("$.content[0].id").value(questionParantCommentId.toString()))
                .andExpect(jsonPath("$.content[0].content").value("question parant comment"))
                .andExpect(jsonPath("$.content[0].user.username").value("test"))
                .andExpect(jsonPath("$.content[0].childQuestionComments[0].content").value("question child comment"))
                .andExpect(jsonPath("$.content[1].content").value("question child comment"));
    }
}