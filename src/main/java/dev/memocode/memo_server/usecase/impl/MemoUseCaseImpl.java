package dev.memocode.memo_server.usecase.impl;

import dev.memocode.memo_server.domain.memo.entity.Memo;
import dev.memocode.memo_server.domain.memo.service.MemoService;
import dev.memocode.memo_server.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.usecase.MemoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MemoUseCaseImpl implements MemoUseCase {

    private final MemoService memoService;

    @Override
    public UUID createMemo(MemoCreateDTO dto) {
        Memo memo = memoService.createMemo(dto);

        return memo.getId();
    }
}
