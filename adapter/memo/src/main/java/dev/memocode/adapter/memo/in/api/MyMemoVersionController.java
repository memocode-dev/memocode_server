package dev.memocode.adapter.memo.in.api;

import dev.memocode.adapter.memo.in.api.spec.MyMemoVersionApi;
import dev.memocode.application.memo.dto.request.FindAllMyMemoVersionRequest;
import dev.memocode.application.memo.dto.request.FindMyMemoVersionRequest;
import dev.memocode.application.memo.dto.result.FindAllMyMemoVersion_MemoVersionResult;
import dev.memocode.application.memo.dto.result.FindMyMemoVersion_MemoVersionResult;
import dev.memocode.application.memo.usecase.MemoVersionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/memos/{memoId}/versions")
public class MyMemoVersionController implements MyMemoVersionApi {

    private final MemoVersionUseCase memoVersionUseCase;

    @GetMapping("/{memoVersionId}")
    public ResponseEntity<FindMyMemoVersion_MemoVersionResult> findMyMemoVersion(
            @PathVariable UUID memoId,
            @PathVariable UUID memoVersionId,
            @AuthenticationPrincipal Jwt jwt) {

        FindMyMemoVersionRequest request = FindMyMemoVersionRequest.builder()
                .memoId(memoId)
                .memoVersionId(memoVersionId)
                .userId(UUID.fromString(jwt.getSubject()))
                .build();

        FindMyMemoVersion_MemoVersionResult body = memoVersionUseCase.findMyMemoVersion(request);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    public ResponseEntity<List<FindAllMyMemoVersion_MemoVersionResult>> findAllMyMemoVersion(
            @PathVariable UUID memoId,
            @AuthenticationPrincipal Jwt jwt
    ) {

        FindAllMyMemoVersionRequest request = FindAllMyMemoVersionRequest.builder()
                .memoId(memoId)
                .userId(UUID.fromString(jwt.getSubject()))
                .build();

        List<FindAllMyMemoVersion_MemoVersionResult> body = memoVersionUseCase.findAllMyMemoVersion(request);
        return ResponseEntity.ok(body);
    }
}
