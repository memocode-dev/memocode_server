package dev.memocode.memo_server.in.api.controller;

import dev.memocode.memo_server.domain.memocomment.dto.request.CommentCreateDTO;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentUpdateDTO;
import dev.memocode.memo_server.in.api.form.CommentCreateForm;
import dev.memocode.memo_server.in.api.form.CommentUpdateForm;
import dev.memocode.memo_server.in.api.spec.PostCommentApi;
import dev.memocode.memo_server.usecase.CommentUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/{memoId}/comments")
public class CommentController implements PostCommentApi {

    private final CommentUseCase commentUseCase;
    private static final String USER_ID_CLAIM_NAME = "user_id";

    @PostMapping
    public ResponseEntity<String> createComments(@PathVariable("memoId") UUID memoId,
                                                 @RequestBody CommentCreateForm form,
                                                 @AuthenticationPrincipal Jwt jwt) {

        CommentCreateDTO dto = CommentCreateDTO.builder()
                .memoId(memoId)
                .content(form.getContent())
                .authorId(UUID.fromString(jwt.getClaim(USER_ID_CLAIM_NAME)))
                .build();

        UUID commentId = commentUseCase.createComments(dto);
        return ResponseEntity.created(URI.create(commentId.toString())).body(commentId.toString());
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComments(@PathVariable("memoId") UUID memoId,
                                               @PathVariable("commentId") UUID commentId,
                                               @RequestBody CommentUpdateForm form,
                                               @AuthenticationPrincipal Jwt jwt) {

        CommentUpdateDTO dto = CommentUpdateDTO.builder()
                .memoId(memoId)
                .commentId(commentId)
                .content(form.getContent())
                .authorId(UUID.fromString(jwt.getClaim(USER_ID_CLAIM_NAME)))
                .build();

        commentUseCase.updateComments(dto);
        return ResponseEntity.noContent().build();
    }
}
