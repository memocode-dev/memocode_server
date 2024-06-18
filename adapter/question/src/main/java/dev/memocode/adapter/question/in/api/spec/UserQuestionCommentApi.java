package dev.memocode.adapter.question.in.api.spec;

import dev.memocode.application.core.PageResponse;
import dev.memocode.application.question.dto.FindAllQuestionComment_QuestionCommentResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "user-questions-comments", description = "유저 질문 댓글 API")
public interface UserQuestionCommentApi {

    @Operation(summary = "유저 질문 댓글 이름으로 전체조회")
    ResponseEntity<PageResponse<FindAllQuestionComment_QuestionCommentResult>> findAllQuestionCommentByUsername(String username, Pageable pageable);
}