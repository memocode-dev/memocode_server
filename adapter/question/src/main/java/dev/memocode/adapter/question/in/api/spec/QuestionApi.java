package dev.memocode.adapter.question.in.api.spec;

import dev.memocode.adapter.question.in.api.form.CreateQuestionForm;
import dev.memocode.adapter.question.in.api.form.UpdateQuestionForm;
import dev.memocode.application.core.PageResponse;
import dev.memocode.application.question.dto.FindQuestion_QuestionResult;
import dev.memocode.application.question.dto.SearchQuestion_QuestionResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@Tag(name = "questions", description = "질문 API")
public interface QuestionApi {

    @Operation(summary = "QNA 생성")
    @SecurityRequirement(name = "bearer-key")
    ResponseEntity<String> createQuestion(CreateQuestionForm form, Jwt jwt);

    @Operation(summary = "QNA 삭제")
    @SecurityRequirement(name = "bearer-key")
    ResponseEntity<Void> deleteQuestion(UUID questionId, Jwt jwt);

    @Operation(summary = "QNA 수정")
    @SecurityRequirement(name = "bearer-key")
    ResponseEntity<Void> updateQuestion(UUID questionId, UpdateQuestionForm form, Jwt jwt);

    @Operation(summary = "QNA 단건 조회")
    ResponseEntity<FindQuestion_QuestionResult> findQuestion(UUID questionId);

    @Operation(summary = "QNA 검색")
    ResponseEntity<PageResponse<SearchQuestion_QuestionResult>> searchQuestionByKeyword(String keyword, int page, int pageSize);
}