package dev.memocode.adapter.memo.in.api.spec;

import dev.memocode.application.core.PageResponse;
import dev.memocode.application.memo.dto.result.FindAllMemoComment_MemoCommentResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "user-memos-memoComments", description = "유저 메모 댓글 API")
public interface UserMemoCommentApi {

//    @Operation(summary = "유저 메모 댓글 이름으로 전체조회")
//    ResponseEntity<PageResponse<FindAllMemoComment_MemoCommentResult>> findAllQuestionCommentByUsername(String username, Pageable pageable);
}