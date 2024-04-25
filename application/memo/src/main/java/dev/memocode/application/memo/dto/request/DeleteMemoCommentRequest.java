package dev.memocode.application.memo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DeleteMemoCommentRequest {
    @NotNull
    private UUID memoId;
    @NotNull
    private UUID memoCommentId;
    @NotNull
    private UUID userId;
}
