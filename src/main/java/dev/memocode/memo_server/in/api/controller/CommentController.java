package dev.memocode.memo_server.in.api.controller;

import dev.memocode.memo_server.domain.memocomment.dto.request.ChildCommentCreateDTO;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentCreateDTO;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentDeleteDto;
import dev.memocode.memo_server.domain.memocomment.dto.request.CommentUpdateDTO;
import dev.memocode.memo_server.domain.memocomment.dto.response.CommentsDTO;
import dev.memocode.memo_server.in.api.form.CommentCreateForm;
import dev.memocode.memo_server.in.api.form.CommentUpdateForm;
import dev.memocode.memo_server.in.api.spec.PostCommentApi;
import dev.memocode.memo_server.usecase.CommentUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    @PostMapping
    public ResponseEntity<String> createComment(@PathVariable("memoId") UUID memoId,
                                                 @RequestBody CommentCreateForm form,
                                                 @AuthenticationPrincipal Jwt jwt) {

        CommentCreateDTO dto = CommentCreateDTO.builder()
                .memoId(memoId)
                .content(form.getContent())
                .authorId(UUID.fromString(jwt.getSubject()))
                .build();

        UUID commentId = commentUseCase.createComment(dto);
        return ResponseEntity.created(URI.create(commentId.toString())).body(commentId.toString());
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable("memoId") UUID memoId,
                                               @PathVariable("commentId") UUID commentId,
                                               @RequestBody CommentUpdateForm form,
                                               @AuthenticationPrincipal Jwt jwt) {

        CommentUpdateDTO dto = CommentUpdateDTO.builder()
                .memoId(memoId)
                .commentId(commentId)
                .content(form.getContent())
                .authorId(UUID.fromString(jwt.getSubject()))
                .build();

        commentUseCase.updateComments(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("memoId") UUID memoId,
                                               @PathVariable("commentId") UUID commentId,
                                               @AuthenticationPrincipal Jwt jwt) {

        CommentDeleteDto dto = CommentDeleteDto.builder()
                .memoId(memoId)
                .commentId(commentId)
                .authorId(UUID.fromString(jwt.getSubject()))
                .build();

        commentUseCase.deleteComments(dto);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<CommentsDTO>> findAllComment(@PathVariable("memoId") UUID memoId,
                                                             @RequestParam(name = "page", defaultValue = "0") int page,
                                                             @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<CommentsDTO> dto = commentUseCase.findAllComments(memoId, page, size);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/{commentId}")
    public ResponseEntity<String> createChildComment(@PathVariable("memoId") UUID memoId,
                                                      @PathVariable("commentId") UUID commentId,
                                                      @RequestBody CommentCreateForm form,
                                                      @AuthenticationPrincipal Jwt jwt) {

        ChildCommentCreateDTO dto = ChildCommentCreateDTO.builder()
                .memoId(memoId)
                .commentId(commentId)
                .content(form.getContent())
                .authorId(UUID.fromString(jwt.getSubject()))
                .build();

        UUID childCommentId = commentUseCase.createChildComment(dto);

        return ResponseEntity.created(URI.create(childCommentId.toString())).body(childCommentId.toString());
    }

}
