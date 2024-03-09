package dev.memocode.memo_server.domain.memo.service;

import dev.memocode.memo_server.domain.external.author.entity.Author;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import dev.memocode.memo_server.domain.memo.repository.MemoVersionRepository;
import dev.memocode.memo_server.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static dev.memocode.memo_server.exception.GlobalErrorCode.NOT_VALID_MEMO_OWNER;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemoVersionService {

    private final MemoVersionRepository memoVersionRepository;
    @Transactional
    public MemoVersion createMemoVersion(Memo memo, UUID accountId) {
        validOwner(memo.getAuthor().getAccountId(), accountId);

        Integer lastVersion = memoVersionRepository
                .findVersionByMemoId(memo.getId());

        int version = (lastVersion != null) ? lastVersion + 1 : 1;

        MemoVersion memoVersion = MemoVersion.builder()
                .memo(memo)
                .title(memo.getTitle())
                .content(memo.getContent())
                .version(version)
                .build();

        return memoVersionRepository.save(memoVersion);
    }

    /**
     * owner 체크
     */
    private void validOwner(UUID ownerId, UUID accountId) {
        if (!accountId.equals(ownerId)){
            throw new GlobalException(NOT_VALID_MEMO_OWNER);
        }
    }
}
