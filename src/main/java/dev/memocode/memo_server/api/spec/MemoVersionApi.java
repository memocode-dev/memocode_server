package dev.memocode.memo_server.api.spec;

import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@Tag(name = "memo_version", description = "메모 버전 API")
@SecurityRequirement(name = "bearer-key")
public interface MemoVersionApi {

    @Operation(summary = "메모 버전 생성")
    ResponseEntity<String> createMemoVersion(UUID memoId, Jwt jwt);

    @Operation(summary = "메모 버전 삭제")
    ResponseEntity<Void> deleteMemoVersion(UUID memoId, UUID memVersionId, Jwt jwt);

    @Operation(summary = "메모 단일 조회")
    ResponseEntity<MemoVersionDetailDTO> findMemoVersion(UUID memoId, UUID memoVersionId, Jwt jwt);

    @Operation(summary = "메모 버전 전체 조회")
    ResponseEntity<MemoVersionsDTO> findAllMemoVersion(UUID memoId, Jwt jwt, int page, int size);
}
