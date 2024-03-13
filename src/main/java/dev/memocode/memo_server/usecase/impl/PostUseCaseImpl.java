package dev.memocode.memo_server.usecase.impl;

import dev.memocode.memo_server.domain.memo.dto.request.PostDetailRequestDTO;
import dev.memocode.memo_server.domain.memo.dto.response.PostDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.PostsDTO;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.service.MemoService;
import dev.memocode.memo_server.domain.memo.service.MemoVersionService;
import dev.memocode.memo_server.domain.memo.service.PostService;
import dev.memocode.memo_server.usecase.PostUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PostUseCaseImpl implements PostUseCase {

    private final PostService postService;

    @Override
    public PostDetailDTO findPost(UUID memoId) {
        Memo post = postService.findPost(memoId);

        return PostDetailDTO.from(post);
    }

    @Override
    public Page<PostsDTO> findAllPost(int page, int size) {
        Page<Memo> posts = postService.findAllPost(page, size);
        return PostsDTO.from(posts);
    }
}
