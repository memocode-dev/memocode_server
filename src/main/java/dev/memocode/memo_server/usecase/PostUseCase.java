package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.domain.memo.dto.request.PostDetailRequestDTO;
import dev.memocode.memo_server.domain.memo.dto.response.PostDetailDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface PostUseCase {

    PostDetailDTO findPost(PostDetailRequestDTO dto);
}
