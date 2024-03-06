package dev.memocode.memo_server.domain.memo.service;

import dev.memocode.memo_server.domain.external.user.entity.Author;
import dev.memocode.memo_server.domain.external.user.service.AuthorService;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.repository.MemoRepository;
import dev.memocode.memo_server.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.dto.request.MemoDeleteDTO;
import dev.memocode.memo_server.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static dev.memocode.memo_server.exception.GlobalErrorCode.MEMO_NOT_FOUND;
import static dev.memocode.memo_server.exception.GlobalErrorCode.NOT_VALID_MEMO_OWNER;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final AuthorService authorService;

    @Transactional
    public Memo createMemo(MemoCreateDTO dto) {
        Author author = authorService.findByAccountIdElseThrow(dto.getAccountId());
        Memo memo = Memo.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(author)
                .build();
        return memoRepository.save(memo);
    }

    @Transactional
    public void deleteMemo(MemoDeleteDTO dto) {
        Memo memo = findByMemoId(dto.getMemoId());
        validOwner(memo.getAuthor().getAccountId(), dto.getAccountId());

        // soft delete 적용
        memo.delete();
    }

    /**
     * 메모 owner 체크
     */
    private void validOwner(UUID memoOwnerAccountId, UUID accountId) {
        if (!memoOwnerAccountId.equals(accountId)){
            throw new GlobalException(NOT_VALID_MEMO_OWNER);
        }
    }

    /**
     * 메모 찾기
     */
    public Memo findByMemoId(UUID memoId) {
        return memoRepository.findById(memoId)
                .orElseThrow(() -> new GlobalException(MEMO_NOT_FOUND));
    }
}
