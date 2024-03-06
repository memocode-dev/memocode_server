package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.dto.request.MemoDeleteDTO;
import dev.memocode.memo_server.dto.request.MemoUpdateDTO;
import dev.memocode.memo_server.dto.response.MemoDetailDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(readOnly = true)
public interface MemoUseCase {

    @Transactional
    UUID createMemo(MemoCreateDTO dto);

    @Transactional
    void deleteMemo(MemoDeleteDTO memoDeleteDTO);

    MemoDetailDTO findMemo(UUID memoId, UUID accountID);

    @Transactional
    UUID updateMemo(MemoUpdateDTO dto);
}
