package dev.memocode.memo_server.in.api.controller;

import dev.memocode.memo_server.domain.memo.dto.response.PostDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.PostsDTO;
import dev.memocode.memo_server.in.api.spec.PostApi;
import dev.memocode.memo_server.usecase.PostUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController implements PostApi {

    private final PostUseCase postUseCase;

    @GetMapping("/{memoId}")
    public ResponseEntity<PostDetailDTO> findPost(@PathVariable("memoId") UUID memoId) {
        PostDetailDTO post = postUseCase.findPost(memoId);
        return ResponseEntity.ok().body(post);
    }

    @GetMapping
    public ResponseEntity<Page<PostsDTO>> findAllPost(@RequestParam(name = "page", defaultValue = "0") int page,
                                                      @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<PostsDTO> dto = postUseCase.findAllPost(page, size);
        return ResponseEntity.ok().body(dto);
    }
}
