package dev.memocode.memo_server.api.spec;

import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@Tag(name = "post", description = "게시글 API")
@SecurityRequirement(name = "bearer-key")
public interface PostApi {

    @Operation(summary = "게시글 생성")
    ResponseEntity<String> createPost(UUID memoId, UUID memoVersionId, Jwt jwt);

    @Operation(summary = "게시글 삭제")
    ResponseEntity<Void> deletePost(UUID memoId, UUID memVersionId, Jwt jwt);

    @Operation(summary = "게시글 단일 조회")
    ResponseEntity<Void> findPost(UUID memoId, UUID memoVersionId, Jwt jwt);

    @Operation(summary = "게시글 전체 조회")
    ResponseEntity<Void> findAllPost(UUID memoId, Jwt jwt, int page, int size);
}
