package dev.memocode.memo_server.domain.memo.service;

import dev.memocode.memo_server.domain.memo.dto.request.PostCreateDTO;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import dev.memocode.memo_server.domain.memo.entity.SelectedMemoVersion;
import dev.memocode.memo_server.domain.memo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public SelectedMemoVersion createPost(Memo memo, MemoVersion memoVersion, UUID accountId) {

        SelectedMemoVersion post = SelectedMemoVersion.builder()
                .memo(memo)
                .memoVersion(memoVersion)
                .build();

        return postRepository.save(post);
    }
}
