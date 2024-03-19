package dev.memocode.memo_server.domain.memo.service;

import dev.memocode.memo_server.domain.base.exception.GlobalException;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static dev.memocode.memo_server.domain.base.exception.GlobalErrorCode.MEMO_NOT_FOUND;
import static dev.memocode.memo_server.domain.base.exception.GlobalErrorCode.NOT_VALID_MEMO_OWNER;

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

    Memo findByMemoIdElseThrow(UUID memoId) {
        return findById(memoId)
                .orElseThrow(() -> new GlobalException(MEMO_NOT_FOUND));
    }

    Optional<Memo> findById(UUID memoId) {
        return memoRepository.findById(memoId);
    }

    Integer getLastSequence(UUID authorId) {
        return memoRepository.getLastSequence(authorId, DEFAULT_LAST_SEQUENCE_DEFAULT_VALUE);
    }
}
