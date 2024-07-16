package dev.memocode.adapter.memo.in.api.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@Tag(name = "memos", description = "메모 API")
@SecurityRequirement(name = "bearer-key")
public interface MemoVersionApi {

    @Operation(summary = "메모 버전 생성")
    ResponseEntity<String> createMemoVersion(UUID memoId, Jwt jwt);

    @Operation(summary = "메모 버전 삭제")
    ResponseEntity<Void> deleteMemoVersion(UUID memoId, UUID memoVersionId, Jwt jwt);
}
