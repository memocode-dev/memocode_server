package dev.memocode.application.question.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DeleteQuestionCommentRequest {
    @NotNull
    private UUID userId;
    @NotNull
    private UUID questionId;
    @NotNull
    private UUID questionCommentId;
}
