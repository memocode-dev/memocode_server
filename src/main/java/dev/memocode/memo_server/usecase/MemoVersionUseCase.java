package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionDeleteDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionRequestDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionsDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(readOnly = true)
public interface MemoVersionUseCase {

    @Transactional
    UUID createMemoVersion(MemoVersionCreateDTO dto);

    @Transactional
    void deleteMemoVersion(MemoVersionDeleteDTO dto);

    MemoVersionDetailDTO findMemoVersionDetail(MemoVersionRequestDetailDTO dto);

    MemoVersionsDTO findMemoVersions(UUID memoId, UUID uuid, int page, int size);
}
