package dev.memocode.application.memo.usecase;

import dev.memocode.application.core.PageResponse;
import dev.memocode.application.memo.dto.reque.SearchMemoByUsernameRequest;
import dev.memocode.application.memo.dto.reque.SearchMemoRequest;
import dev.memocode.application.memo.dto.request.*;
import dev.memocode.application.memo.dto.result.*;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
public interface MemoUseCase {
    UUID createMemo(@Valid CreateMemoRequest request);
    void updateMemo(@Valid UpdateMemoRequest request);
    void deleteMemo(@Valid DeleteMemoRequest request);
    FindMyMemo_MemoResult findMyMemo(@Valid FindMyMemoRequest request);

    List<FindAllMyMemo_MemoResult> findAllMyMemo(@Valid FindAllMyMemoRequest request);

    PageResponse<SearchMyMemo_MemoResult> searchMyMemo(@Valid SearchMyMemoRequest request);

    FindMemo_MemoResult findMemo(@Valid FindMemoRequest request);

    PageResponse<SearchMemo_MemoResult> searchMemoByUsername(@Valid SearchMemoByUsernameRequest request);

    PageResponse<SearchMemo_MemoResult> searchMemoByKeyword(@Valid SearchMemoRequest request);

    CreateMemoImage_MemoImageResult createMemoImageUploadURL(UUID userId, UUID memoId, String mimeType);
    String findMemoImageUploadURL(UUID userId, UUID memoId, UUID memoImageId, String extension);
}
