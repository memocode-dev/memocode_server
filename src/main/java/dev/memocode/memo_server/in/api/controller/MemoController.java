package dev.memocode.memo_server.in.api.controller;

import dev.memocode.memo_server.domain.memo.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoDeleteDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoUpdateDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoDetailDTO;
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
    private static final String USER_ID_CLAIM_NAME = "user_id";

    /**
     * 메모 생성
     */
    @PostMapping
    public ResponseEntity<String> createMemo(@RequestBody MemoCreateForm form, @AuthenticationPrincipal Jwt jwt) {
        MemoCreateDTO dto = MemoCreateDTO.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .authorId(UUID.fromString(jwt.getClaim(USER_ID_CLAIM_NAME)))
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
                .authorId(UUID.fromString(jwt.getClaim(USER_ID_CLAIM_NAME)))
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
                .authorId(UUID.fromString(jwt.getClaim(USER_ID_CLAIM_NAME)))
                .title(form.getTitle())
                .content(form.getContent())
                .visibility(form.getVisibility())
                .security(form.getSecurity())
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
                UUID.fromString(jwt.getClaim(USER_ID_CLAIM_NAME)));
        return ResponseEntity.ok().body(dto);
    }

    /**
     * 메모 전체 조회
     */
    @GetMapping
    public ResponseEntity<MemosDTO> findAllMemo(@AuthenticationPrincipal Jwt jwt){
        MemosDTO memos =
                memoUseCase.findMemos(UUID.fromString(jwt.getClaim(USER_ID_CLAIM_NAME)));
        return ResponseEntity.ok().body(memos);
    }
}
