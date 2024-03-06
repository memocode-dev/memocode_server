package dev.memocode.memo_server.api;

import dev.memocode.memo_server.api.spec.MemoApi;
import dev.memocode.memo_server.dto.form.MemoCreateForm;
import dev.memocode.memo_server.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.dto.form.MemoUpdateForm;
import dev.memocode.memo_server.dto.request.MemoDeleteDTO;
import dev.memocode.memo_server.dto.response.MemoDetailDTO;
import dev.memocode.memo_server.dto.response.MemoUpdateDTO;
import dev.memocode.memo_server.dto.response.MemosDTO;
import dev.memocode.memo_server.mapper.MemoCreateDTOMapper;
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
    private final MemoCreateDTOMapper memoCreateDTOMapper;

    private static final String ACCOUNT_ID_CLAIM_NAME = "account_id";

    @PostMapping
    public ResponseEntity<String> createMemo(@RequestBody MemoCreateForm form, @AuthenticationPrincipal Jwt jwt) {
        MemoCreateDTO memoCreateDTO =
                memoCreateDTOMapper.fromMemoCreateFormAndAccountId(form,
                        UUID.fromString(jwt.getClaim(ACCOUNT_ID_CLAIM_NAME)));

        UUID memoId = memoUseCase.createMemo(memoCreateDTO);
        return ResponseEntity.created(URI.create(memoId.toString())).body(memoId.toString());
    }

    @DeleteMapping("/{memoId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable UUID memoId, @AuthenticationPrincipal Jwt jwt){
        MemoDeleteDTO memoDeleteDTO =
                memoCreateDTOMapper.fromMemoDeleteMemoIdAndAccountId(memoId,
                        UUID.fromString(jwt.getClaim(ACCOUNT_ID_CLAIM_NAME)));

        memoUseCase.deleteMemo(memoDeleteDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{memoId}")
    public ResponseEntity<MemoUpdateDTO> updateMemo(@PathVariable UUID memoId, @RequestBody MemoUpdateForm form,
                                                    @AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{memoId}")
    public ResponseEntity<MemoDetailDTO> findMemo(@PathVariable UUID memoId, @AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok().body(null);
    }

    @GetMapping
    public ResponseEntity<MemosDTO> findAllMemo(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok().body(null);
    }
}
