package dev.memocode.adapter.memo.in.api;

import dev.memocode.adapter.memo.in.api.form.CreateChildMemoCommentForm;
import dev.memocode.adapter.memo.in.api.form.CreateMemoCommentForm;
import dev.memocode.adapter.memo.in.api.form.UpdateMemoCommentForm;
import dev.memocode.adapter.memo.in.api.spec.MemoCommentApi;
import dev.memocode.application.memo.dto.request.CreateChildMemoCommentRequest;
import dev.memocode.application.memo.dto.request.CreateMemoCommentRequest;
import dev.memocode.application.memo.dto.request.DeleteMemoCommentRequest;
import dev.memocode.application.memo.dto.request.UpdateMemoCommentRequest;
import dev.memocode.application.memo.dto.result.FindAllMemoComment_MemoCommentResult;
import dev.memocode.application.memo.usecase.MemoCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memos/{memoId}/comments")
public class MemoCommentController implements MemoCommentApi {

    private final MemoCommentUseCase memoCommentUseCase;

    @PostMapping
    public ResponseEntity<String> createMemoComment(
            @PathVariable UUID memoId, @RequestBody CreateMemoCommentForm form, @AuthenticationPrincipal Jwt jwt) {
        CreateMemoCommentRequest request = CreateMemoCommentRequest.builder()
                .memoId(memoId)
                .userId(UUID.fromString(jwt.getSubject()))
                .content(form.getContent())
                .build();

        UUID memoCommentId = memoCommentUseCase.createMemoComment(request);
        return ResponseEntity.created(URI.create(memoCommentId.toString())).body(memoCommentId.toString());
    }

    @PatchMapping("/{memoCommentId}")
    public ResponseEntity<Void> updateMemoComment(
            @PathVariable UUID memoId, @PathVariable UUID memoCommentId,
            @RequestBody UpdateMemoCommentForm form, @AuthenticationPrincipal Jwt jwt) {
        UpdateMemoCommentRequest request = UpdateMemoCommentRequest.builder()
                .memoId(memoId)
                .memoCommentId(memoCommentId)
                .userId(UUID.fromString(jwt.getSubject()))
                .content(form.getContent())
                .build();

        memoCommentUseCase.updateMemoComment(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{memoCommentId}")
    public ResponseEntity<Void> deleteMemoComment(
            @PathVariable UUID memoId, @PathVariable UUID memoCommentId, @AuthenticationPrincipal Jwt jwt) {
        DeleteMemoCommentRequest request = DeleteMemoCommentRequest.builder()
                .memoId(memoId)
                .memoCommentId(memoCommentId)
                .userId(UUID.fromString(jwt.getSubject()))
                .build();

        memoCommentUseCase.deleteMemoComment(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{memoCommentId}/childComments")
    public ResponseEntity<String> createChildMemoComment(
            @PathVariable UUID memoId, @PathVariable UUID memoCommentId,
            @RequestBody CreateChildMemoCommentForm form, @AuthenticationPrincipal Jwt jwt) {
        CreateChildMemoCommentRequest request = CreateChildMemoCommentRequest.builder()
                .memoId(memoId)
                .memoCommentId(memoCommentId)
                .userId(UUID.fromString(jwt.getSubject()))
                .content(form.getContent())
                .build();

        UUID childMemoCommentId = memoCommentUseCase.createChildMemoComment(request);
        return ResponseEntity.created(URI.create(childMemoCommentId.toString())).body(childMemoCommentId.toString());
    }

    @GetMapping
    public ResponseEntity<List<FindAllMemoComment_MemoCommentResult>> findAllMemoComment(@PathVariable UUID memoId) {
        List<FindAllMemoComment_MemoCommentResult> body = memoCommentUseCase.findAllMemoComment(memoId);
        return ResponseEntity.ok(body);
    }
}
