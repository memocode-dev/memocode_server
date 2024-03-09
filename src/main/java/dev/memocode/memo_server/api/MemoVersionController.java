package dev.memocode.memo_server.api;

import dev.memocode.memo_server.api.spec.MemoVersionApi;
import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionsDTO;
import dev.memocode.memo_server.domain.memo.mapper.MemoVersionDtoMapper;
import dev.memocode.memo_server.usecase.MemoVersionUseCase;
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
@RequestMapping("/memos/{memoId}/versions")
public class MemoVersionController implements MemoVersionApi {

    private final MemoVersionUseCase memoVersionUseCase;
    private final MemoVersionDtoMapper memoVersionDtoMapper;
    private static final String ACCOUNT_ID_CLAIM_NAME = "account_id";

    /**
     * 메모 버전 생성
     */
    @PostMapping
    public ResponseEntity<String> createMemoVersion(@PathVariable(name = "memoId") UUID memoId, @AuthenticationPrincipal Jwt jwt) {
        MemoVersionCreateDTO dto = memoVersionDtoMapper
                .createMemoVersion(memoId, UUID.fromString(jwt.getClaim(ACCOUNT_ID_CLAIM_NAME)));

        UUID memoVersion = memoVersionUseCase.createMemoVersion(dto);
        log.info("memoVersion = {}", memoVersion);

        return ResponseEntity.created(URI.create(memoVersion.toString())).body(memoVersion.toString());
    }

    /**
     * 메모 버전 삭제
     */
    @DeleteMapping("/{memoVersionId}")
    public ResponseEntity<Void> deleteMemoVersion(@PathVariable("memoId") UUID memoId, @PathVariable UUID memoVersionId, @AuthenticationPrincipal Jwt jwt){

        return ResponseEntity.ok().build();
    }

    /**
     * 메모 버전 단일 조회
     */
    @GetMapping("/{memoVersionId}")
    public ResponseEntity<MemoVersionDetailDTO> findMemoVersion(@PathVariable("memoId") UUID memoId, @PathVariable UUID memoVersionId, @AuthenticationPrincipal Jwt jwt){

        return ResponseEntity.ok().build();
    }

    /**
     * 메모 버전 전체 조회
     */
    @GetMapping
    public ResponseEntity<MemoVersionsDTO> findAllMemoVersion(@PathVariable("memoId") UUID memoId, @AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok().build();
    }

}
