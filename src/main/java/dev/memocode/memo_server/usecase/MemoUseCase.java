package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.dto.request.MemoDeleteDTO;
import dev.memocode.memo_server.dto.response.MemoDetailDTO;

import java.util.UUID;

public interface MemoUseCase {

    UUID createMemo(MemoCreateDTO dto);

    void deleteMemo(MemoDeleteDTO memoDeleteDTO);

    MemoDetailDTO findMemo(UUID memoId, UUID accountID);
}
