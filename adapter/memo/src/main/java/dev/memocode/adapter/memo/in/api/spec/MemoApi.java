package dev.memocode.adapter.memo.in.api.spec;

import dev.memocode.adapter.memo.in.api.form.CreateMemoForm;
import dev.memocode.adapter.memo.in.api.form.UpdateMemoForm;
import dev.memocode.application.core.PageResponse;
import dev.memocode.application.memo.dto.result.FindMemo_MemoResult;
import dev.memocode.application.memo.dto.result.SearchMemo_MemoResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

// http://localhost:8080/swagger-ui/index.html#
@Tag(name = "memos", description = "메모 API")
@SecurityRequirement(name = "bearer-key")
public interface MemoApi {
    @Operation(summary = "메모 생성")
    ResponseEntity<String> createMemo(CreateMemoForm form, Jwt jwt);

    @Operation(summary = "메모 수정")
    ResponseEntity<Void> updateMemo(UUID memoId, UpdateMemoForm form, Jwt jwt);

    @Operation(summary = "메모 삭제")
    ResponseEntity<Void> deleteMemo(UUID memoId, Jwt jwt);

    @Operation(summary = "메모 단건 조회")
    ResponseEntity<FindMemo_MemoResult> findMemo(UUID memoId);

    @Operation(summary = "메모 검색")
    ResponseEntity<PageResponse<SearchMemo_MemoResult>> searchMemoByKeyword(String keyword, int page, int pageSize);

    @Operation(summary = "메모 검색 (USERNAME으로 검색)")
    ResponseEntity<PageResponse<SearchMemo_MemoResult>> searchMemoByUsername(String username, int page, int pageSize);
}
