package dev.memocode.memo_server.usecase.impl;

import dev.memocode.memo_server.domain.external.author.service.AuthorService;
import dev.memocode.memo_server.domain.memo.dto.request.PostCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.PostDeleteDTO;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import dev.memocode.memo_server.domain.memo.entity.SelectedMemoVersion;
import dev.memocode.memo_server.domain.memo.service.MemoService;
import dev.memocode.memo_server.domain.memo.service.MemoVersionService;
import dev.memocode.memo_server.domain.memo.service.PostService;
import dev.memocode.memo_server.usecase.MemoVersionUseCase;
import dev.memocode.memo_server.usecase.PostUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PostUseCaseImpl implements PostUseCase {
    private final PostService postService;
    private final MemoService memoService;
    private final MemoVersionService memoVersionService;
    private final AuthorService authorService;

    @Override
    public UUID createPost(PostCreateDTO dto) {
        Memo memo = memoService.findByMemoId(dto.getMemoId());
        MemoVersion memoVersion = memoVersionService.findByMemoVersion(dto.getMemoVersionId());
        SelectedMemoVersion post = postService.createPost(memo, memoVersion, dto.getAccountId());

        return post.getId();
    }

    @Override
    public void deletePost(PostDeleteDTO dto) {
        Memo memo = memoService.findByMemoId(dto.getMemoId());
        MemoVersion memoVersion = memoVersionService.findByMemoVersion(dto.getMemoVersionId());
        postService.deletePost(memo, memoVersion, dto.getAccountId());
    }
}
