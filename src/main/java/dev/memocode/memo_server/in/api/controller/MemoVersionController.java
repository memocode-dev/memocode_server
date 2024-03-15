package dev.memocode.memo_server.in.api.controller;

import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionDeleteDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionRequestDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionsDTO;
import dev.memocode.memo_server.in.api.spec.MemoVersionApi;
import dev.memocode.memo_server.usecase.MemoVersionUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    private static final String USER_ID_CLAIM_NAME = "user_id";

    /**
     * 메모 버전 생성
     */
    @PostMapping
    public ResponseEntity<String> createMemoVersion(@PathVariable(name = "memoId") UUID memoId,
                                                    @AuthenticationPrincipal Jwt jwt) {

        MemoVersionCreateDTO dto = MemoVersionCreateDTO.builder()
                .memoId(memoId)
                .authorId(UUID.fromString(jwt.getClaim(USER_ID_CLAIM_NAME)))
                .build();

        UUID memoVersion = memoVersionUseCase.createMemoVersion(dto);
        log.info("memoVersion = {}", memoVersion);

        return ResponseEntity.created(URI.create(memoVersion.toString())).body(memoVersion.toString());
    }

    /**
     * 메모 버전 삭제
     */
    @DeleteMapping("/{memoVersionId}")
    public ResponseEntity<Void> deleteMemoVersion(@PathVariable("memoId") UUID memoId,
                                                  @PathVariable("memoVersionId") UUID memoVersionId,
                                                  @AuthenticationPrincipal Jwt jwt){
        MemoVersionDeleteDTO dto = MemoVersionDeleteDTO.builder()
                .memoId(memoId)
                .memoVersionId(memoVersionId)
                .authorId(UUID.fromString(jwt.getClaim(USER_ID_CLAIM_NAME)))
                .build();

        memoVersionUseCase.deleteMemoVersion(dto);

        return ResponseEntity.ok().build();
    }

    /**
     * 메모 버전 단일 조회
     */
    @GetMapping("/{memoVersionId}")
    public ResponseEntity<MemoVersionDetailDTO> findMemoVersion(@PathVariable("memoId") UUID memoId,
                                                                @PathVariable("memoVersionId") UUID memoVersionId,
                                                                @AuthenticationPrincipal Jwt jwt){
        MemoVersionRequestDetailDTO dto = MemoVersionRequestDetailDTO.builder()
                .memoId(memoId)
                .memoVersionId(memoVersionId)
                .authorId(UUID.fromString(jwt.getClaim(USER_ID_CLAIM_NAME)))
                .build();

        MemoVersionDetailDTO memoVersion = memoVersionUseCase.findMemoVersionDetail(dto);
        return ResponseEntity.ok().body(memoVersion);
    }

    /**
     * 메모 버전 전체 조회
     */
    @GetMapping
    public ResponseEntity<Page<MemoVersionsDTO>> findAllMemoVersion(@PathVariable("memoId") UUID memoId,
                                                                    @AuthenticationPrincipal Jwt jwt,
                                                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                                                    @RequestParam(name = "size", defaultValue = "10") int size){
        Page<MemoVersionsDTO> dto = memoVersionUseCase
                .findMemoVersions(memoId, UUID.fromString(jwt.getClaim(USER_ID_CLAIM_NAME)), page, size);

        return ResponseEntity.ok().body(dto);
    }

}
