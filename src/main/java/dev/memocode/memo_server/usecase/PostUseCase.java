package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.domain.memo.dto.response.PostDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.PostsDTO;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface PostUseCase {
    PostDetailDTO findPost(UUID memoId);

    Page<PostsDTO> findAllPost(int page, int size);
}
