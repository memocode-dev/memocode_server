package dev.memocode.memo_server.in.api.spec;

import dev.memocode.memo_server.domain.memo.dto.response.MemoDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemosDTO;
import dev.memocode.memo_server.in.api.form.MemoCreateForm;
import dev.memocode.memo_server.in.api.form.MemoUpdateForm;
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
    ResponseEntity<Void> updateMemo(UUID memoId, MemoUpdateForm form, Jwt jwt);

    @Operation(summary = "메모 단일 조회")
    ResponseEntity<MemoDetailDTO> findMemo(UUID memoId, Jwt jwt);

    @Operation(summary = "메모 전체 조회")
    ResponseEntity<MemosDTO> findAllMemo(Jwt jwt);
}
