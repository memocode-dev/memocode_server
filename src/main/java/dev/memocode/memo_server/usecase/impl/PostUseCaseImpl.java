package dev.memocode.memo_server.usecase.impl;

import dev.memocode.memo_server.domain.memo.dto.request.PostDetailRequestDTO;
import dev.memocode.memo_server.domain.memo.dto.response.PostDetailDTO;
import dev.memocode.memo_server.domain.memo.service.MemoService;
import dev.memocode.memo_server.domain.memo.service.MemoVersionService;
import dev.memocode.memo_server.domain.memo.service.PostService;
import dev.memocode.memo_server.usecase.PostUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostUseCaseImpl implements PostUseCase {
    private final PostService postService;
    private final MemoService memoService;
    private final MemoVersionService memoVersionService;

    @Override
    public PostDetailDTO findPost(PostDetailRequestDTO dto) {


        return null;
    }
}
