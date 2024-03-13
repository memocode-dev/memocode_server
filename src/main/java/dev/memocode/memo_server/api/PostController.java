package dev.memocode.memo_server.api;

import dev.memocode.memo_server.api.spec.PostApi;
import dev.memocode.memo_server.domain.memo.dto.request.PostCreateDTO;
import dev.memocode.memo_server.domain.memo.mapper.PostDtoMapper;
import dev.memocode.memo_server.usecase.PostUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/{memoId}")
public class PostController implements PostApi {

    private final PostUseCase postUseCase;
    private final PostDtoMapper postDtoMapper;
    private static final String ACCOUNT_ID_CLAIM_NAME = "account_id";

    @PostMapping("/create/{memoVersionId}")
    public ResponseEntity<String> createPost(@PathVariable("memoId") UUID memoId,
                                             @PathVariable("memoVersionId") UUID memoVersionId,
                                             @AuthenticationPrincipal Jwt jwt) {
        PostCreateDTO dto = postDtoMapper
                .createPost(memoId, memoVersionId, UUID.fromString(jwt.getClaim(ACCOUNT_ID_CLAIM_NAME)));

        UUID postId = postUseCase.createPost(dto);

        return ResponseEntity.created(URI.create(postId.toString())).body(postId.toString());
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePost(UUID memoId, UUID memVersionId, Jwt jwt) {
        return null;
    }

    @GetMapping
    public ResponseEntity<Void> findPost(UUID memoId, UUID memoVersionId, Jwt jwt) {
        return null;
    }

    @GetMapping("/all")
    public ResponseEntity<Void> findAllPost(UUID memoId, Jwt jwt, int page, int size) {
        return null;
    }
}
