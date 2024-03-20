package dev.memocode.memo_server.in.api.spec;

import dev.memocode.memo_server.domain.memocomment.entity.Comment;
import dev.memocode.memo_server.in.api.form.CommentCreateForm;
import dev.memocode.memo_server.in.api.form.MemoCreateForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@Tag(name = "post-comments", description = "게시글 댓글 API")
@SecurityRequirement(name = "bearer-key")
public interface PostCommentApi {

    @Operation
    ResponseEntity<String> createComments(UUID memoId, CommentCreateForm form, Jwt jwt);
}
