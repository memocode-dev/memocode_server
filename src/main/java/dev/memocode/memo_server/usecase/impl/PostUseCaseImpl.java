package dev.memocode.memo_server.usecase.impl;

import dev.memocode.memo_server.domain.memo.dto.request.PostDetailRequestDTO;
import dev.memocode.memo_server.domain.memo.dto.response.PostDetailDTO;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import dev.memocode.memo_server.domain.memo.entity.SelectedMemoVersion;
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
        Memo memo = memoService.findByMemoId(dto.getMemoId());
        MemoVersion memoVersion = memoVersionService.findByMemoVersion(dto.getMemoVersionId());
        SelectedMemoVersion post = postService.findPost(memo, memoVersion);

        return PostDetailDTO.from(post);
    }
}
