package dev.memocode.memo_server.in.api.controller;

import com.meilisearch.sdk.model.SearchResultPaginated;
import dev.memocode.memo_server.domain.memo.dto.MemoSearchRequestDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoDeleteDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoUpdateDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemosBookmarkedDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemosDTO;
import dev.memocode.memo_server.in.api.form.MemoCreateForm;
import dev.memocode.memo_server.in.api.form.MemoUpdateForm;
import dev.memocode.memo_server.in.api.spec.MemoApi;
import dev.memocode.memo_server.usecase.MemoUseCase;
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
@RequestMapping("/memos")
public class MemoController implements MemoApi {

    private final MemoUseCase memoUseCase;

    /**
     * 메모 생성
     */
    @PostMapping
    public ResponseEntity<String> createMemo(@RequestBody MemoCreateForm form, @AuthenticationPrincipal Jwt jwt) {
        MemoCreateDTO dto = MemoCreateDTO.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .summary(form.getSummary())
                .authorId(UUID.fromString(jwt.getSubject()))
                .build();

        UUID memoId = memoUseCase.createMemo(dto);
        return ResponseEntity.created(URI.create(memoId.toString())).body(memoId.toString());
    }

    /**
     * 메모 삭제
     */
    @DeleteMapping("/{memoId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable("memoId") UUID memoId, @AuthenticationPrincipal Jwt jwt){
        MemoDeleteDTO dto = MemoDeleteDTO.builder()
                .memoId(memoId)
                .authorId(UUID.fromString(jwt.getSubject()))
                .build();

        memoUseCase.deleteMemo(dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 메모 수정
     */
    @PatchMapping("/{memoId}")
    public ResponseEntity<Void> updateMemo(@PathVariable("memoId") UUID memoId,
                                             @RequestBody MemoUpdateForm form,
                                             @AuthenticationPrincipal Jwt jwt){
        MemoUpdateDTO dto = MemoUpdateDTO.builder()
                .memoId(memoId)
                .authorId(UUID.fromString(jwt.getSubject()))
                .title(form.getTitle())
                .content(form.getContent())
                .summary(form.getSummary())
                .visibility(form.getVisibility())
                .security(form.getSecurity())
                .bookmarked(form.getBookmarked())
                .build();

        memoUseCase.updateMemo(dto);

        return ResponseEntity.noContent().build();
    }

    /**
     * 메모 단일 조회
     */
    @GetMapping("/{memoId}")
    public ResponseEntity<MemoDetailDTO> findMemo(@PathVariable("memoId") UUID memoId, @AuthenticationPrincipal Jwt jwt){
        MemoDetailDTO dto = memoUseCase.findMemo(memoId,
                UUID.fromString(jwt.getSubject()));
        return ResponseEntity.ok().body(dto);
    }

    /**
     * 메모 전체 조회
     */
    @GetMapping
    public ResponseEntity<MemosDTO> findAllMemo(@AuthenticationPrincipal Jwt jwt){
        MemosDTO memos =
                memoUseCase.findMemos(UUID.fromString(jwt.getSubject()));
        return ResponseEntity.ok().body(memos);
    }

    @GetMapping("/bookmarked")
    public ResponseEntity<MemosBookmarkedDTO> findAllBookmarkedMemos(@AuthenticationPrincipal Jwt jwt) {
        MemosBookmarkedDTO bookmarkedMemos
                = memoUseCase.findBookmarkedMemos(UUID.fromString(jwt.getSubject()));

        return ResponseEntity.ok().body(bookmarkedMemos);
    }

    /**
     * 메모 검색
     */
    @Override
    @GetMapping("/search")
    public ResponseEntity<SearchResultPaginated> searchMemos(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @AuthenticationPrincipal Jwt jwt) {

        MemoSearchRequestDTO dto = MemoSearchRequestDTO.builder()
                .keyword(keyword)
                .page(page)
                .pageSize(pageSize)
                .authorId(UUID.fromString(jwt.getSubject()))
                .build();

        SearchResultPaginated searchResultPaginated = memoUseCase.searchMemos(dto);

        return ResponseEntity.ok(searchResultPaginated);
    }
}
