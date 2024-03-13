package dev.memocode.memo_server.api;

import dev.memocode.memo_server.api.spec.PostApi;
import dev.memocode.memo_server.domain.memo.dto.response.PostDetailDTO;
import dev.memocode.memo_server.domain.memo.mapper.PostDtoMapper;
import dev.memocode.memo_server.usecase.PostUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController implements PostApi {

    private final PostUseCase postUseCase;
    private final PostDtoMapper postDtoMapper;
    private static final String ACCOUNT_ID_CLAIM_NAME = "account_id";

    @GetMapping("/{memoId}")
    public ResponseEntity<PostDetailDTO> findPost(@PathVariable("memoId") UUID memoId) {
        return null;
    }

    @GetMapping("/all")
    public ResponseEntity<Void> findAllPost() {
        return null;
    }
}
