package dev.memocode.memo_server.usecase.impl;

import dev.memocode.memo_server.domain.external.author.entity.Author;
import dev.memocode.memo_server.domain.external.author.service.AuthorService;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.service.MemoService;
import dev.memocode.memo_server.domain.memo.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoDeleteDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoUpdateDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemosDTO;
import dev.memocode.memo_server.exception.GlobalErrorCode;
import dev.memocode.memo_server.exception.GlobalException;
import dev.memocode.memo_server.usecase.MemoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static dev.memocode.memo_server.exception.GlobalErrorCode.*;

@Component
@RequiredArgsConstructor
public class MemoUseCaseImpl implements MemoUseCase {

    private final MemoService memoService;
    private final AuthorService authorService;

    @Override
    public UUID createMemo(MemoCreateDTO dto) {
        Memo memo = memoService.createMemo(dto);

        return memo.getId();
    }

    @Override
    public void deleteMemo(MemoDeleteDTO dto) {
        memoService.deleteMemo(dto);
    }

    @Override
    public UUID updateMemo(MemoUpdateDTO dto) {
        return memoService.updateMemo(dto);
    }

    @Override
    public MemoDetailDTO findMemo(UUID memoId, UUID accountId) {
        Memo memo = memoService.findMemo(memoId, accountId);

        return MemoDetailDTO.from(memo);
    }

    @Override
    public MemosDTO findMemos(UUID accountId) {
        Author author = authorService.findByAccountId(accountId)
                .orElseThrow(() -> new GlobalException(AUTHOR_NOT_FOUND));
        List<Memo> memos = memoService.findMemos(author.getId());

        return MemosDTO.from(memos);
    }
}
