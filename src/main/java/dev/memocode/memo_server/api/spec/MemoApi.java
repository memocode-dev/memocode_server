package dev.memocode.memo_server.api.spec;

import dev.memocode.memo_server.dto.form.MemoCreateForm;
import dev.memocode.memo_server.dto.form.MemoUpdateForm;
import dev.memocode.memo_server.dto.response.MemoDetailDto;
import dev.memocode.memo_server.dto.response.MemoUpdateDto;
import dev.memocode.memo_server.dto.response.MemosDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestBody;

// http://localhost:8080/swagger-ui/index.html#
@Tag(name = "memos", description = "메모 API")
@SecurityRequirement(name = "bearer-key")
public interface MemoApi {

    @Operation(summary = "메모 생성")
    ResponseEntity<String> createMemo(MemoCreateForm form,  Jwt jwt);

    @Operation(summary = "메모 삭제")
    ResponseEntity<Void> deleteMemo(Long memoId, Jwt jwt);

    @Operation(summary = "메모 수정")
    ResponseEntity<MemoUpdateDto> updateMemo(Long memoId, MemoUpdateForm form, Jwt jwt);

    @Operation(summary = "메모 단일 조회")
    ResponseEntity<MemoDetailDto> findMemo(Long memoId, Jwt jwt);

    @Operation(summary = "메모 전체 조회")
    ResponseEntity<MemosDto> findAllMemo(Jwt jwt);
}
