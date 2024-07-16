package dev.memocode.adapter.memo.in.api.spec;

import dev.memocode.application.memo.dto.result.FindAllMyMemoVersion_MemoVersionResult;
import dev.memocode.application.memo.dto.result.FindMyMemoVersion_MemoVersionResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

@Tag(name = "users", description = "유저 API")
@SecurityRequirement(name = "bearer-key")
public interface MyMemoVersionApi {
    @Operation(summary = "나의 메모 버전 단일 조회")
    ResponseEntity<FindMyMemoVersion_MemoVersionResult> findMyMemoVersion(UUID memoId, UUID memoVersionId, Jwt jwt);

    @Operation(summary = "나의 메모 버전 전체 조회")
    ResponseEntity<List<FindAllMyMemoVersion_MemoVersionResult>> findAllMyMemoVersion(UUID memoId, Jwt jwt);
}
