package dev.memocode.memo_server.api;

import dev.memocode.memo_server.api.spec.MemoApi;
import dev.memocode.memo_server.dto.form.MemoCreateForm;
import dev.memocode.memo_server.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.dto.form.MemoUpdateForm;
import dev.memocode.memo_server.dto.response.MemoDetailDto;
import dev.memocode.memo_server.dto.response.MemoUpdateDto;
import dev.memocode.memo_server.dto.response.MemosDto;
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
                memoCreateDTOMapper.fromMemoCreateFormAndAccountId(form, UUID.fromString(jwt.getClaim(ACCOUNT_ID_CLAIM_NAME)));

        UUID memoId = memoUseCase.createMemo(memoCreateDTO);
        return ResponseEntity.created(URI.create(memoId.toString())).body(memoId.toString());
    }

    @DeleteMapping("/{memoId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable Long memoId, @AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{memoId}")
    public ResponseEntity<MemoUpdateDto> updateMemo(@PathVariable Long memoId, @RequestBody MemoUpdateForm form,
                                                    @AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{memoId}")
    public ResponseEntity<MemoDetailDto> findMemo(@PathVariable Long memoId, @AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok().body(null);
    }

    @GetMapping
    public ResponseEntity<MemosDto> findAllMemo(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok().body(null);
    }
}
