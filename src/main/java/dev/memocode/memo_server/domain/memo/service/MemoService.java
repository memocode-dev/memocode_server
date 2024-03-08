package dev.memocode.memo_server.domain.memo.service;

import dev.memocode.memo_server.domain.external.author.entity.Author;
import dev.memocode.memo_server.domain.external.author.service.AuthorService;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.repository.MemoRepository;
import dev.memocode.memo_server.domain.memo.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoDeleteDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoUpdateDTO;
import dev.memocode.memo_server.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Transactional
    public UUID updateMemo(MemoUpdateDTO dto) {
        Memo memo = findByMemoId(dto.getMemoId());
        validOwner(memo.getAuthor().getAccountId(), dto.getAccountId());

        memo.updateMemo(dto.getTitle(), dto.getContent());

        return memo.getId();
    }

    public Memo findMemo(UUID memoId) {
        return findByMemoId(memoId);
    }

    public Page<Memo> findMemos(UUID accountId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return memoRepository.findByAuthorAccountId(accountId, pageRequest);

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
