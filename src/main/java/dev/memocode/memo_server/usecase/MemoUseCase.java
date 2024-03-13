package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.domain.memo.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoDeleteDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoUpdateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoUpdateVisibilityDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemosDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(readOnly = true)
public interface MemoUseCase {

    @Transactional
    UUID createMemo(MemoCreateDTO dto);

    @Transactional
    void deleteMemo(MemoDeleteDTO memoDeleteDTO);

    @Transactional
    UUID updateMemo(MemoUpdateDTO dto);

    MemoDetailDTO findMemo(UUID memoId, UUID accountID);

    MemosDTO findMemos(UUID uuid);
}
