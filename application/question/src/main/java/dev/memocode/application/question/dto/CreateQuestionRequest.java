package dev.memocode.application.question.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class CreateQuestionRequest {
    @NotNull
    private UUID userId;

    private String title;
    private String content;
    @Builder.Default
    private Set<String> tags = new HashSet<>();
}
