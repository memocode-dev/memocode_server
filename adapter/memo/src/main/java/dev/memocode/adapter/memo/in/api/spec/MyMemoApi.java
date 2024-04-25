package dev.memocode.adapter.memo.in.api.spec;

import dev.memocode.application.core.PageResponse;
import dev.memocode.application.memo.dto.result.FindAllMyMemo_MemoResult;
import dev.memocode.application.memo.dto.result.FindMyMemo_MemoResult;
import dev.memocode.application.memo.dto.result.SearchMyMemo_MemoResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

@Tag(name = "users-memos", description = "나의 메모 API")
@SecurityRequirement(name = "bearer-key")
public interface MyMemoApi {
    @Operation(summary = "나의 메모 단일 조회")
    ResponseEntity<FindMyMemo_MemoResult> findMyMemo(UUID memoId, Jwt jwt);

    @Operation(summary = "나의 메모 전체 조회")
    ResponseEntity<List<FindAllMyMemo_MemoResult>> findAllMyMemo(Jwt jwt);

    @Operation(summary = "나의 메모 검색")
    ResponseEntity<PageResponse<SearchMyMemo_MemoResult>> searchMyMemo(String keyword, int page, int pageSize, Jwt jwt);
}
