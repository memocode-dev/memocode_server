package dev.memocode.memo_server.domain.memo.service;

import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.repository.MemoRepository;
import dev.memocode.memo_server.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.grammars.graph.GraphLanguageParserBaseListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static dev.memocode.memo_server.exception.GlobalErrorCode.MEMO_NOT_FOUND;
import static dev.memocode.memo_server.exception.GlobalErrorCode.POST_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final MemoRepository memoRepository;

    public Memo findPost(UUID memoId) {
        Memo memo = findByMemoId(memoId);

        if (memo.getDeleted()){
            throw new GlobalException(MEMO_NOT_FOUND);
        }

        if (!memo.getVisibility()){
            throw new GlobalException(POST_NOT_FOUND);
        }

        return memo;
    }

    public Page<Memo> findAllPost(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return memoRepository.findByPost(pageRequest);
    }

    /**
     * 게시글(메모) 찾기
     */
    public Memo findByMemoId(UUID memoId) {
        return memoRepository.findById(memoId)
                .orElseThrow(() -> new GlobalException(MEMO_NOT_FOUND));
    }
}
