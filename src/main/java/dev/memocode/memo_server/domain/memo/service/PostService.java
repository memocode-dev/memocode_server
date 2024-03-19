package dev.memocode.memo_server.domain.memo.service;

import dev.memocode.memo_server.domain.base.exception.GlobalException;
import dev.memocode.memo_server.domain.memo.dto.response.PostDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.PostsDTO;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.mapper.PostMapper;
import dev.memocode.memo_server.domain.memo.repository.MemoRepository;
import dev.memocode.memo_server.usecase.PostUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static dev.memocode.memo_server.domain.base.exception.GlobalErrorCode.MEMO_NOT_FOUND;
import static dev.memocode.memo_server.domain.base.exception.GlobalErrorCode.POST_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService implements PostUseCase {

    private final MemoRepository memoRepository;
    private final InternalMemoService internalMemoService;
    private final PostMapper postMapper;

    @Override
    public PostDetailDTO findPost(UUID memoId) {
        Memo memo = internalMemoService.findByMemoIdElseThrow(memoId);

        if (memo.getDeleted()){
            throw new GlobalException(MEMO_NOT_FOUND);
        }

        if (!memo.getVisibility()){
            throw new GlobalException(POST_NOT_FOUND);
        }

        return postMapper.entity_to_postDetailDTO(memo);
    }

    @Override
    public Page<PostsDTO> findAllPost(int page, int size) {
        Page<Memo> memos = memoRepository.findByPosts(PageRequest.of(page, size));

        return postMapper.entity_to_postsDTO(memos);
    }
}