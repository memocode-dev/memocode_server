package dev.memocode.memo_server.domain.memo.service;

import dev.memocode.memo_server.domain.author.entity.Author;
import dev.memocode.memo_server.domain.base.exception.GlobalException;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import dev.memocode.memo_server.domain.memo.repository.MemoVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static dev.memocode.memo_server.domain.base.exception.GlobalErrorCode.MEMO_AND_MEMO_VERSION_NOT_MATCH;
import static dev.memocode.memo_server.domain.base.exception.GlobalErrorCode.MEMO_VERSION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class InternalMemoVersionService {

    private final MemoVersionRepository memoVersionRepository;

    /**
     * 메모 버전 찾기
     */
    MemoVersion findByIdElseThrow(UUID memoVersionId) {
        return findById(memoVersionId)
                .orElseThrow(() -> new GlobalException(MEMO_VERSION_NOT_FOUND));
    }

    Optional<MemoVersion> findById(UUID memoVersionId) {
        return memoVersionRepository.findById(memoVersionId);
    }

    Integer getLastVersion(Memo memo) {
        return memoVersionRepository
                .findVersionByMemoId(memo.getId());
    }

    void validMemoVersionOwner(UUID memoVersionId, UUID memoId, UUID authorId) {
        MemoVersion memoVersion = findByIdElseThrow(memoVersionId);

        Memo memo = memoVersion.getMemo();
        if (!memo.getId().equals(memoId)) {
            throw new GlobalException(MEMO_AND_MEMO_VERSION_NOT_MATCH);
        }

        Author author = memoVersion.getMemo().getAuthor();
        if (!author.getId().equals(authorId)) {
            throw new GlobalException(MEMO_AND_MEMO_VERSION_NOT_MATCH);
        }
    }
}
