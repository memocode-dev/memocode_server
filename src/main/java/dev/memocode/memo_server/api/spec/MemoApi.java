package dev.memocode.memo_server.api.spec;

import dev.memocode.memo_server.domain.memo.dto.form.MemoCreateForm;
import dev.memocode.memo_server.domain.memo.dto.form.MemoUpdateForm;
import dev.memocode.memo_server.domain.memo.dto.form.MemoUpdateVisibilityForm;
import dev.memocode.memo_server.domain.memo.dto.response.MemoDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemosDTO;
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
    ResponseEntity<String> createMemo(MemoCreateForm form,  Jwt jwt);

    @Operation(summary = "메모 삭제")
    ResponseEntity<Void> deleteMemo(UUID memoId, Jwt jwt);

    @Operation(summary = "메모 수정")
    ResponseEntity<String> updateMemo(UUID memoId, MemoUpdateForm form, Jwt jwt);

    @Operation(summary = "메모 단일 조회")
    ResponseEntity<MemoDetailDTO> findMemo(UUID memoId, Jwt jwt);

    @Operation(summary = "메모 전체 조회")
    ResponseEntity<MemosDTO> findAllMemo(Jwt jwt);

    @Operation(summary = "메모 visibility 수정")
    ResponseEntity<Void> updateMemoVisibility(UUID memoId, MemoUpdateVisibilityForm form, Jwt jwt);
}
