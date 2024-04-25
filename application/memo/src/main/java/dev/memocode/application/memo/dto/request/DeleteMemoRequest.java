package dev.memocode.application.memo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DeleteMemoRequest {
    @NotNull
    private UUID authorId;
    @NotNull
    private UUID memoId;
}
