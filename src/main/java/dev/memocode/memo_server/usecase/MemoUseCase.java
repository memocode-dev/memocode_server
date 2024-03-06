package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.dto.request.MemoCreateDTO;
import dev.memocode.memo_server.dto.request.MemoDeleteDTO;

import java.util.UUID;

public interface MemoUseCase {

    UUID createMemo(MemoCreateDTO dto);

    void deleteMemo(MemoDeleteDTO memoDeleteDTO);
}
