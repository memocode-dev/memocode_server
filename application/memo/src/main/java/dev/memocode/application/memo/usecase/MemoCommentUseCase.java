package dev.memocode.application.memo.usecase;

import dev.memocode.application.memo.dto.request.CreateChildMemoCommentRequest;
import dev.memocode.application.memo.dto.request.CreateMemoCommentRequest;
import dev.memocode.application.memo.dto.request.DeleteMemoCommentRequest;
import dev.memocode.application.memo.dto.request.UpdateMemoCommentRequest;
import dev.memocode.application.memo.dto.result.FindAllMemoComment_MemoCommentResult;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
public interface MemoCommentUseCase {
    UUID createMemoComment(@Valid CreateMemoCommentRequest request);

    void updateMemoComment(@Valid UpdateMemoCommentRequest request);

    void deleteMemoComment(@Valid DeleteMemoCommentRequest request);
    UUID createChildMemoComment(@Valid CreateChildMemoCommentRequest request);

    List<FindAllMemoComment_MemoCommentResult> findAllMemoComment(UUID memoId);
}
