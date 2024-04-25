package dev.memocode.adapter.memo.in.api;

import dev.memocode.adapter.memo.in.api.spec.MemoVersionApi;
import dev.memocode.application.memo.dto.request.CreateMemoVersionRequest;
import dev.memocode.application.memo.dto.request.DeleteMemoVersionRequest;
import dev.memocode.application.memo.usecase.MemoVersionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memos/{memoId}/versions")
public class MemoVersionController implements MemoVersionApi {

    private final MemoVersionUseCase memoVersionUseCase;

    @PostMapping
    public ResponseEntity<String> createMemoVersion(@PathVariable UUID memoId, @AuthenticationPrincipal Jwt jwt) {
        CreateMemoVersionRequest request = CreateMemoVersionRequest.builder()
                .memoId(memoId)
                .userId(UUID.fromString(jwt.getSubject()))
                .build();

        UUID memoVersionId = memoVersionUseCase.createMemoVersion(request);
        return ResponseEntity.created(URI.create(memoVersionId.toString())).body(memoVersionId.toString());
    }

    @DeleteMapping("/{memoVersionId}")
    public ResponseEntity<Void> deleteMemoVersion(
            @PathVariable UUID memoId, @PathVariable UUID memoVersionId, @AuthenticationPrincipal Jwt jwt) {

        DeleteMemoVersionRequest request = DeleteMemoVersionRequest.builder()
                .memoId(memoId)
                .memoVersionId(memoVersionId)
                .userId(UUID.fromString(jwt.getSubject()))
                .build();

        memoVersionUseCase.deleteMemoVersion(request);
        return ResponseEntity.notFound().build();
    }
}
