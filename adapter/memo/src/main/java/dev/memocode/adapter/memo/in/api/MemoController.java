package dev.memocode.adapter.memo.in.api;

import dev.memocode.adapter.memo.in.api.form.CreateMemoForm;
import dev.memocode.adapter.memo.in.api.form.CreateMemoImageURLForm;
import dev.memocode.adapter.memo.in.api.form.UpdateMemoForm;
import dev.memocode.adapter.memo.in.api.spec.MemoApi;
import dev.memocode.application.core.PageResponse;
import dev.memocode.application.memo.dto.reque.SearchMemoByUsernameRequest;
import dev.memocode.application.memo.dto.reque.SearchMemoRequest;
import dev.memocode.application.memo.dto.request.CreateMemoRequest;
import dev.memocode.application.memo.dto.request.DeleteMemoRequest;
import dev.memocode.application.memo.dto.request.FindMemoRequest;
import dev.memocode.application.memo.dto.request.UpdateMemoRequest;
import dev.memocode.application.memo.dto.result.CreateMemoImage_MemoImageResult;
import dev.memocode.application.memo.dto.result.FindMemo_MemoResult;
import dev.memocode.application.memo.dto.result.SearchMemo_MemoResult;
import dev.memocode.application.memo.usecase.MemoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/memos")
public class MemoController implements MemoApi{

    private final MemoUseCase memoUseCase;

    @PostMapping
    public ResponseEntity<String> createMemo(@RequestBody CreateMemoForm form, @AuthenticationPrincipal Jwt jwt) {
        CreateMemoRequest dto = CreateMemoRequest.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .summary(form.getSummary())
                .userId(UUID.fromString(jwt.getSubject()))
                .security(form.getSecurity())
                .tags(form.getTags())
                .build();

        UUID memoId = memoUseCase.createMemo(dto);

        return ResponseEntity.created(URI.create(memoId.toString())).body(memoId.toString());
    }

    @PatchMapping("/{memoId}")
    public ResponseEntity<Void> updateMemo(@PathVariable("memoId") UUID memoId,
                                           @RequestBody UpdateMemoForm form,
                                           @AuthenticationPrincipal Jwt jwt) {
        UpdateMemoRequest dto = UpdateMemoRequest.builder()
                .memoId(memoId)
                .userId(UUID.fromString(jwt.getSubject()))
                .title(form.getTitle())
                .content(form.getContent())
                .summary(form.getSummary())
                .thumbnailUrl(form.getThumbnailUrl())
                .visibility(form.getVisibility())
                .security(form.getSecurity())
                .bookmarked(form.getBookmarked())
                .tags(form.getTags())
                .build();

        memoUseCase.updateMemo(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{memoId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable("memoId") UUID memoId, @AuthenticationPrincipal Jwt jwt) {
        DeleteMemoRequest dto = DeleteMemoRequest.builder()
                .memoId(memoId)
                .userId(UUID.fromString(jwt.getSubject()))
                .build();

        memoUseCase.deleteMemo(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{memoId}")
    public ResponseEntity<FindMemo_MemoResult> findMemo(@PathVariable UUID memoId) {
        FindMemoRequest request = FindMemoRequest.builder()
                .memoId(memoId)
                .build();

        FindMemo_MemoResult body = memoUseCase.findMemo(request);
        return ResponseEntity.ok(body);
    }

    @GetMapping
    public ResponseEntity<PageResponse<SearchMemo_MemoResult>> searchMemoByKeyword(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize
    ) {
        SearchMemoRequest request = SearchMemoRequest.builder()
                .keyword(keyword)
                .page(page)
                .pageSize(pageSize)
                .build();

        PageResponse<SearchMemo_MemoResult> body = memoUseCase.searchMemoByKeyword(request);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/{memoId}/images")
    public ResponseEntity<CreateMemoImage_MemoImageResult> createMemoImage(@PathVariable UUID memoId, @AuthenticationPrincipal Jwt jwt, @RequestBody CreateMemoImageURLForm form) {
        CreateMemoImage_MemoImageResult result = memoUseCase.createMemoImageUploadURL(UUID.fromString(jwt.getSubject()), memoId, form.getMimeType());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{memoId}/images/{memoImageId}.{extension}")
    public ResponseEntity<Void> findMemoImage(@PathVariable UUID memoId, @AuthenticationPrincipal Jwt jwt, @PathVariable UUID memoImageId, @PathVariable String extension) {
        String memoImageUploadURL = memoUseCase.findMemoImageUploadURL(jwt == null ? null : UUID.fromString(jwt.getSubject()), memoId, memoImageId, extension);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(memoImageUploadURL))
                .build();
    }
}
