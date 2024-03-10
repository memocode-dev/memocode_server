package dev.memocode.memo_server.usecase.impl;

import dev.memocode.memo_server.domain.external.author.entity.Author;
import dev.memocode.memo_server.domain.external.author.service.AuthorService;
import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionDeleteDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionRequestDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionDetailDTO;
import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.entity.MemoVersion;
import dev.memocode.memo_server.domain.memo.service.MemoService;
import dev.memocode.memo_server.domain.memo.service.MemoVersionService;
import dev.memocode.memo_server.usecase.MemoVersionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MemoVersionUseCaseImpl implements MemoVersionUseCase {

    private final MemoVersionService memoVersionService;
    private final MemoService memoService;
    private final AuthorService authorService;

    @Override
    public UUID createMemoVersion(MemoVersionCreateDTO dto) {
        Memo memo = memoService.findByMemoId(dto.getMemoId());
        MemoVersion memoVersion = memoVersionService.createMemoVersion(memo, dto.getAccountId());

        return memoVersion.getId();
    }

    @Override
    public void deleteMemoVersion(MemoVersionDeleteDTO dto) {
        Memo memo = memoService.findByMemoId(dto.getMemoId());
        memoVersionService.deleteMemoVersion(memo, dto);
    }

    @Override
    public MemoVersionDetailDTO findMemoVersionDetail(MemoVersionRequestDetailDTO dto) {
        Author author = authorService.findByAccountIdElseThrow(dto.getAccountId());
        Memo memo = memoService.findByMemoId(dto.getMemoId());
        MemoVersion memoVersion = memoVersionService
                .findMemoVersionDetail(memo, dto.getMemoVersionId(), dto.getAccountId());

        return MemoVersionDetailDTO.of(memoVersion, author);
    }
}
