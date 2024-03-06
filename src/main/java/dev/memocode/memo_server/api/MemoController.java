package dev.memocode.memo_server.api;

import dev.memocode.memo_server.api.spec.MemoApi;
import dev.memocode.memo_server.dto.form.MemoCreateForm;
import dev.memocode.memo_server.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.dto.form.MemoUpdateForm;
import dev.memocode.memo_server.dto.request.MemoDeleteDTO;
import dev.memocode.memo_server.dto.response.MemoDetailDTO;
import dev.memocode.memo_server.dto.request.MemoUpdateDTO;
import dev.memocode.memo_server.dto.response.MemosDTO;
import dev.memocode.memo_server.mapper.MemoCreateDTOMapper;
import dev.memocode.memo_server.usecase.MemoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Page;
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
    private final MemoCreateDTOMapper memoCreateDTOMapper;
    private static final String ACCOUNT_ID_CLAIM_NAME = "account_id";

    /**
     * 메모 생성
     */
    @PostMapping
    public ResponseEntity<String> createMemo(@RequestBody MemoCreateForm form, @AuthenticationPrincipal Jwt jwt) {
        MemoCreateDTO dto =
                memoCreateDTOMapper.fromMemoCreateFormAndAccountId(form,
                        UUID.fromString(jwt.getClaim(ACCOUNT_ID_CLAIM_NAME)));

        UUID memoId = memoUseCase.createMemo(dto);
        return ResponseEntity.created(URI.create(memoId.toString())).body(memoId.toString());
    }

    /**
     * 메모 삭제
     */
    @DeleteMapping("/{memoId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable UUID memoId, @AuthenticationPrincipal Jwt jwt){
        MemoDeleteDTO dto =
                memoCreateDTOMapper.fromMemoDeleteMemoIdAndAccountId(memoId,
                        UUID.fromString(jwt.getClaim(ACCOUNT_ID_CLAIM_NAME)));

        memoUseCase.deleteMemo(dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 메모 수정
     */
    @PatchMapping("/{memoId}")
    public ResponseEntity<String> updateMemo(@PathVariable UUID memoId, @RequestBody MemoUpdateForm form,
                                                    @AuthenticationPrincipal Jwt jwt){
        MemoUpdateDTO dto =
                memoCreateDTOMapper.fromMemoUpdateMemoIdAndAccountId(memoId,
                        form, UUID.fromString(jwt.getClaim(ACCOUNT_ID_CLAIM_NAME)));

        UUID updateMemoId = memoUseCase.updateMemo(dto);

        return ResponseEntity.ok().body(updateMemoId.toString());
    }

    /**
     * 메모 단일 조회
     */
    @GetMapping("/{memoId}")
    public ResponseEntity<MemoDetailDTO> findMemo(@PathVariable UUID memoId, @AuthenticationPrincipal Jwt jwt){
        MemoDetailDTO dto = memoUseCase.findMemo(memoId,
                UUID.fromString(jwt.getClaim(ACCOUNT_ID_CLAIM_NAME)));
        return ResponseEntity.ok().body(dto);
    }

    /**
     * 메모 전체 조회
     */
    @GetMapping
    public ResponseEntity<MemosDTO> findAllMemo(@AuthenticationPrincipal Jwt jwt,
                                                @RequestParam(name = "page", defaultValue = "0") int page,
                                                @RequestParam(name = "size", defaultValue = "10") int size){
        MemosDTO memos =
                memoUseCase.findMemos(UUID.fromString(jwt.getClaim(ACCOUNT_ID_CLAIM_NAME)), page, size);
        return ResponseEntity.ok().body(memos);
    }
}
