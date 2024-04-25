package dev.memocode.adapter.question.in.api.spec;

import dev.memocode.adapter.question.in.api.form.CreateQuestionCommentForm;
import dev.memocode.adapter.question.in.api.form.UpdateQuestionCommentForm;
import dev.memocode.application.question.dto.FindAllQuestionComment_QuestionCommentResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

@Tag(name = "questions-comments", description = "질문 댓글 API")
public interface QuestionCommentApi {

    @Operation(summary = "질문 댓글 생성")
    @SecurityRequirement(name = "bearer-key")
    ResponseEntity<String> createQuestionComment(UUID questionId, CreateQuestionCommentForm form, Jwt jwt);

    @Operation(summary = "질문 댓글 수정")
    @SecurityRequirement(name = "bearer-key")
    ResponseEntity<Void> updateComment(UUID questionId, UUID questionCommentId, UpdateQuestionCommentForm form, Jwt jwt);

    @Operation(summary = "질문 댓글 삭제")
    @SecurityRequirement(name = "bearer-key")
    ResponseEntity<Void> deleteQuestionComment(UUID questionId, UUID questionCommentId, Jwt jwt);

    @Operation(summary = "질문 대댓글 생성")
    @SecurityRequirement(name = "bearer-key")
    ResponseEntity<String> createChildQuestionComment(
            UUID questionId, UUID questionCommentId, CreateQuestionCommentForm form, Jwt jwt);

    @Operation(summary = "질문 댓글 전체조회")
    ResponseEntity<List<FindAllQuestionComment_QuestionCommentResult>> findAllQuestionComment(UUID questionId);
}