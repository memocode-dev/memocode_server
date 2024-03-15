package dev.memocode.memo_server.usecase;

import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionCreateDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionDeleteDTO;
import dev.memocode.memo_server.domain.memo.dto.request.MemoVersionRequestDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionDetailDTO;
import dev.memocode.memo_server.domain.memo.dto.response.MemoVersionsDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
public interface MemoVersionUseCase {

    UUID createMemoVersion(@Valid MemoVersionCreateDTO dto);

    void deleteMemoVersion(@Valid MemoVersionDeleteDTO dto);

    MemoVersionDetailDTO findMemoVersionDetail(@Valid MemoVersionRequestDetailDTO dto);

    Page<MemoVersionsDTO> findMemoVersions(UUID memoId, UUID authorId, int page, int size);
}
