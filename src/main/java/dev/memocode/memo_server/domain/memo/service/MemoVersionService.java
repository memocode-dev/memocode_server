package dev.memocode.memo_server.domain.memo.service;

import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionDeleteDTO;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import dev.memocode.memo_server.domain.memo.repository.MemoVersionRepository;
import dev.memocode.memo_server.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static dev.memocode.memo_server.exception.GlobalErrorCode.*;

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

    @Transactional
    public void deleteMemoVersion(Memo memo, MemoVersionDeleteDTO dto) {
        validOwner(memo.getAuthor().getAccountId(), dto.getAccountId());
        MemoVersion memoVersion = findByMemoVersion(dto);

        memoVersion.delete();
    }

    public MemoVersion findMemoVersionDetail(Memo memo, UUID memoVersionId, UUID accountId) {
        MemoVersion memoVersion = memoVersionRepository.findByIdAndMemo(memoVersionId, memo)
                .orElseThrow(() -> new GlobalException(MEMO_VERSION_NOT_FOUND));

        if (memoVersion.getDeleted()){
            throw new GlobalException(MEMO_NOT_FOUND);
        }

        validOwner(memo.getAuthor().getAccountId(), accountId);

        return memoVersion;
    }

    public Page<MemoVersion> findMemoVersions(UUID accountId, Memo memo, int page, int size) {
        validOwner(memo.getAuthor().getAccountId(), accountId);
        PageRequest pageRequest = PageRequest.of(page, size);

        return memoVersionRepository.findAllByMemoVersion(memo.getId(), pageRequest);

    }

    /**
     * 메모 버전 찾기
     */
    private MemoVersion findByMemoVersion(MemoVersionDeleteDTO dto) {
        return memoVersionRepository.findById(dto.getMemoVersionId())
                .orElseThrow(() -> new GlobalException(MEMO_VERSION_NOT_FOUND));
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
