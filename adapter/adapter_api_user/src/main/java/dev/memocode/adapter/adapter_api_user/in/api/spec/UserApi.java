package dev.memocode.adapter.adapter_api_user.in.api.spec;

import dev.memocode.application.core.PageResponse;
import dev.memocode.application.memo.dto.result.FindAllMemoComment_MemoCommentResult;
import dev.memocode.application.memo.dto.result.SearchMemo_MemoResult;
import dev.memocode.application.question.dto.FindAllQuestionComment_QuestionCommentResult;
import dev.memocode.application.question.dto.SearchQuestion_QuestionResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "users", description = "유저 API")
public interface UserApi {

    @Operation(summary = "메모 검색 (USERNAME으로 검색)")
    ResponseEntity<PageResponse<SearchMemo_MemoResult>> searchMemoByUsername(String username, int page, int pageSize);

    @Operation(summary = "메모 댓글 이름으로 전체조회")
    ResponseEntity<PageResponse<FindAllMemoComment_MemoCommentResult>> findAllMemoCommentByUsername(String username, int page, int pageSize);

    @Operation(summary = "QNA 검색 (USERNAME으로 검색)")
    ResponseEntity<PageResponse<SearchQuestion_QuestionResult>> searchQuestionByUsername(String username, int page, int pageSize);

    @Operation(summary = "QNA 댓글 이름으로 전체조회")
    ResponseEntity<PageResponse<FindAllQuestionComment_QuestionCommentResult>> findAllQuestionCommentByUsername(String username, int page, int pageSize);
}