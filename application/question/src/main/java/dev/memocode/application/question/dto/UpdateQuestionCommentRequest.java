package dev.memocode.application.question.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UpdateQuestionCommentRequest {
    @NotNull
    private UUID userId;
    @NotNull
    private UUID questionId;
    @NotNull
    private UUID questionCommentId;
    private String content;

}
