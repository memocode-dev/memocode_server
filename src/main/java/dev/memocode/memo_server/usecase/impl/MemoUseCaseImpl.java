package dev.memocode.memo_server.usecase.impl;

import dev.memocode.memo_server.domain.external.user.entity.Author;
import dev.memocode.memo_server.domain.external.user.service.AuthorService;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.service.MemoService;
import dev.memocode.memo_server.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.dto.request.MemoDeleteDTO;
import dev.memocode.memo_server.dto.response.MemoDetailDTO;
import dev.memocode.memo_server.usecase.MemoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

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
    public MemoDetailDTO findMemo(UUID memoId, UUID accountId) {
        Memo memo = memoService.findMemo(memoId);
        Author author = authorService.findByAccountIdElseThrow(accountId);

        return MemoDetailDTO.from(memo, author);
    }


}
