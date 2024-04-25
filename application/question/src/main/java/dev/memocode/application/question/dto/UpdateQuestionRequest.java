package dev.memocode.application.question.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class UpdateQuestionRequest {
    private UUID userId;
    private UUID questionId;
    private String title;
    private String content;
    private Set<String> tags;
}
