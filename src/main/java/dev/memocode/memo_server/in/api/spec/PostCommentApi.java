package dev.memocode.memo_server.in.api.spec;

import dev.memocode.memo_server.domain.memocomment.dto.response.CommentsDTO;
import dev.memocode.memo_server.in.api.form.CommentCreateForm;
import dev.memocode.memo_server.in.api.form.CommentUpdateForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@Tag(name = "post-comments", description = "블로그 댓글 API")
@SecurityRequirement(name = "bearer-key")
public interface PostCommentApi {

    @Operation(summary = "블로그 댓글 생성")
    ResponseEntity<String> createComments(UUID memoId, CommentCreateForm form, Jwt jwt);

    @Operation(summary = "블로그 댓글 수정")
    ResponseEntity<Void> updateComments(UUID memoId, UUID commentId, CommentUpdateForm form, Jwt jwt);

    @Operation(summary = "블로그 댓글 삭제")
    ResponseEntity<Void> deleteComments(UUID memoId, UUID commentId, Jwt jwt);

    @Operation(summary = "블로그 댓글 전체 조회")
    ResponseEntity<Page<CommentsDTO>> findAllComments(UUID memoId, int page, int size);

    @Operation(summary = "블로그 대댓글 생성")
    ResponseEntity<String> createChildComments(UUID memoId, UUID commentId, CommentCreateForm form, Jwt jwt);
}
