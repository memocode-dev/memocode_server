package dev.memocode.adapter.memo.in.api;

import dev.memocode.adapter.memo.in.api.spec.MyMemoApi;
import dev.memocode.application.core.PageResponse;
import dev.memocode.application.memo.dto.request.FindAllMyMemoRequest;
import dev.memocode.application.memo.dto.request.FindMyMemoRequest;
import dev.memocode.application.memo.dto.request.SearchMyMemoRequest;
import dev.memocode.application.memo.dto.result.FindAllMyMemo_MemoResult;
import dev.memocode.application.memo.dto.result.FindMyMemo_MemoResult;
import dev.memocode.application.memo.dto.result.SearchMyMemo_MemoResult;
import dev.memocode.application.memo.usecase.MemoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/memos")
public class MyMemoController implements MyMemoApi {

    private final MemoUseCase memoUseCase;

    @GetMapping("/{memoId}")
    public ResponseEntity<FindMyMemo_MemoResult> findMyMemo(
            @PathVariable("memoId") UUID memoId, @AuthenticationPrincipal Jwt jwt){

        FindMyMemoRequest request = FindMyMemoRequest.builder()
                .memoId(memoId)
                .userId(UUID.fromString(jwt.getSubject()))
                .build();

        FindMyMemo_MemoResult response = memoUseCase.findMyMemo(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<List<FindAllMyMemo_MemoResult>> findAllMyMemo(@AuthenticationPrincipal Jwt jwt){

        FindAllMyMemoRequest request = FindAllMyMemoRequest.builder()
                .userId(UUID.fromString(jwt.getSubject()))
                .build();

        List<FindAllMyMemo_MemoResult> body = memoUseCase.findAllMyMemo(request);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<SearchMyMemo_MemoResult>> searchMyMemo(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @AuthenticationPrincipal Jwt jwt) {

        SearchMyMemoRequest request = SearchMyMemoRequest.builder()
                .keyword(keyword)
                .page(page)
                .pageSize(pageSize)
                .userId(UUID.fromString(jwt.getSubject()))
                .build();

        PageResponse<SearchMyMemo_MemoResult> body = memoUseCase.searchMyMemo(request);

        return ResponseEntity.ok(body);
    }
}
