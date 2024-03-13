package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.domain.memo.dto.request.PostDetailRequestDTO;
import dev.memocode.memo_server.domain.memo.dto.response.PostDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.PostsDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(readOnly = true)
public interface PostUseCase {


    PostDetailDTO findPost(UUID memoId);

    Page<PostsDTO> findAllPost(int page, int size);
}
