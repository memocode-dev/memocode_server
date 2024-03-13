package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.PostCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.PostDeleteDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(readOnly = true)
public interface PostUseCase {

    @Transactional
    UUID createPost(PostCreateDTO dto);

    @Transactional
    void deletePost(PostDeleteDTO dto);
}
