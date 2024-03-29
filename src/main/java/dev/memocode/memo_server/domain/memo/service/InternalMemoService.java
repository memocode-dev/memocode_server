package dev.memocode.memo_server.domain.memo.service;

import dev.memocode.memo_server.domain.base.exception.GlobalException;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static dev.memocode.memo_server.domain.base.exception.GlobalErrorCode.*;

@Service
@RequiredArgsConstructor
public class InternalMemoService {

    private final MemoRepository memoRepository;
    private final static int DEFAULT_LAST_SEQUENCE_DEFAULT_VALUE = 0;

    void validMemoOwner(UUID memoId, UUID authorId) {
        Memo memo = findByMemoIdElseThrow(memoId);

        if (!memo.getAuthor().getId().equals(authorId)){
            throw new GlobalException(NOT_VALID_MEMO_OWNER);
        }
    }

    void validPost(UUID memoId) {
        Memo memo = findByMemoIdElseThrow(memoId);

        if (memo.getDeleted()){
            throw new GlobalException(MEMO_NOT_FOUND);
        }

        if (!memo.getVisibility()){
            throw new GlobalException(POST_NOT_FOUND);
        }
    }

    public Memo findByMemoIdElseThrow(UUID memoId) {
        return findById(memoId)
                .orElseThrow(() -> new GlobalException(MEMO_NOT_FOUND));
    }

    public Optional<Memo> findById(UUID memoId) {
        return memoRepository.findById(memoId);
    }

    Integer getLastSequence(UUID authorId) {
        return memoRepository.getLastSequenceOrDefault(authorId, DEFAULT_LAST_SEQUENCE_DEFAULT_VALUE);
    }
}
