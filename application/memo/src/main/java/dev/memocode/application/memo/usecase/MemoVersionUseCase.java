package dev.memocode.application.memo.usecase;

import dev.memocode.application.memo.dto.request.CreateMemoVersionRequest;
import dev.memocode.application.memo.dto.request.DeleteMemoVersionRequest;
import dev.memocode.application.memo.dto.request.FindAllMyMemoVersionRequest;
import dev.memocode.application.memo.dto.request.FindMyMemoVersionRequest;
import dev.memocode.application.memo.dto.result.FindAllMyMemoVersion_MemoVersionResult;
import dev.memocode.application.memo.dto.result.FindMyMemoVersion_MemoVersionResult;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
public interface MemoVersionUseCase {

    UUID createMemoVersion(@Valid CreateMemoVersionRequest request);
    void deleteMemoVersion(@Valid DeleteMemoVersionRequest request);
    FindMyMemoVersion_MemoVersionResult findMyMemoVersion(@Valid FindMyMemoVersionRequest request);
    List<FindAllMyMemoVersion_MemoVersionResult> findAllMyMemoVersion(@Valid FindAllMyMemoVersionRequest request);
}
