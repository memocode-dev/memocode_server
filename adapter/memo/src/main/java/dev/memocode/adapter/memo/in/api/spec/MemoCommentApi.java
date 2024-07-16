package dev.memocode.adapter.memo.in.api.spec;

import dev.memocode.adapter.memo.in.api.form.CreateChildMemoCommentForm;
import dev.memocode.adapter.memo.in.api.form.CreateMemoCommentForm;
import dev.memocode.adapter.memo.in.api.form.UpdateMemoCommentForm;
import dev.memocode.application.memo.dto.result.FindAllMemoComment_MemoCommentResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

@Tag(name = "memos", description = "메모 API")
@SecurityRequirement(name = "bearer-key")
public interface MemoCommentApi {

    @Operation(summary = "메모 댓글 생성")
    ResponseEntity<String> createMemoComment(UUID memoId, CreateMemoCommentForm form, Jwt jwt);

    @Operation(summary = "메모 댓글 수정")
    ResponseEntity<Void> updateMemoComment(UUID memoId, UUID memoCommentId, UpdateMemoCommentForm form, Jwt jwt);

    @Operation(summary = "메모 댓글 삭제")
    ResponseEntity<Void> deleteMemoComment(UUID memoId, UUID memoCommentId, Jwt jwt);

    @Operation(summary = "메모 대댓글 생성")
    ResponseEntity<String> createChildMemoComment(UUID memoId, UUID memoCommentId, CreateChildMemoCommentForm form, Jwt jwt);

    @Operation(summary = "메모 댓글 전체 조회")
    ResponseEntity<List<FindAllMemoComment_MemoCommentResult>> findAllMemoComment(UUID memoId);
}
